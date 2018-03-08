package Files;

import java.awt.Robot;
import java.util.ArrayList;

/**
 * @author timch
 *
 */
public class JobAssignment {


	/**
	 * items in the warehouse
	 */
	private ItemTable items;

	/**
	 * @param queue
	 * @param robots
	 * @param items
	 */
	public JobAssignment( ItemTable items) {
		this.items = items;
	}

	/**
	 * @return a string of steps the robot should take
	 */
	public ArrayList<String> getNextPlan(ArrayList<Task> tasks, RobotPC robot) {
		float robotWeight = robot.getCurrentWeight();
		float maxWeight = robot.getMaxWeight();
		Task nextTask;
		int x = robot.getCurrentX();
		int y = robot.getCurrentY();
		ArrayList<String> plan = new ArrayList<String>();

		// try to create a plan that includes all tasks within a job
		while ((nextTask = getClosestTask(tasks, x, y)) != null) {
			int quantity = nextTask.getQuantity();
			Item currentItem = items.getItem(nextTask.getId());

			// check if all quantities of the item can be loaded into the robot without
			// going over the max weight
			if (currentItem.getWeight() * quantity + robotWeight > maxWeight) {
				// try to find the max quantity of that item it can load into the robot
				for (int i = 1; i <= quantity; i++) {
					if (currentItem.getWeight() * i + robotWeight > maxWeight) {
						// if even one of the item cannot be loaded then that means the robot should
						// just head to the drop point
						if (i != 1) {
							int itemsToTake = i - 1;
							plan.add(nextTask.getId() + itemsToTake);
							nextTask.changeQuantity(-itemsToTake);
						}
						// the robot is now full so it needs to head towards the drop point
						plan.add("drop");
						x = 0; // DROP LOCATION
						y = 0; // DROP LOCATION
						break;
					}
				}
			}
			// all items of that task can be loaded onto the robot so that task is complete
			else {
				plan.add(nextTask.getId() + quantity);
				nextTask.setComplete(true);
				x = currentItem.getX();
				y = currentItem.getY();
			}
		}
		plan.add("drop");
		return plan;
	}

	/**
	 * @param tasks
	 *            the lists of available tasks for the robot to complete
	 * @param x
	 *            current simulated x position of robot
	 * @param y
	 *            current simulated y position of robot
	 * @return the closest task to the robot at the given position
	 */
	private Task getClosestTask(ArrayList<Task> tasks, int x, int y) {
		Task closestTask = null;
		Float shortestDistance = Float.MAX_VALUE;

		// look at all tasks and find the shortest distance from the item to a given
		// position
		for (Task t : tasks) {
			Item currentItem = items.getItem(t.getId());
			int itemX = currentItem.getX();
			int itemY = currentItem.getY();
			// naive heuristic for distance away from item (doesn't consider walls)
			Float distance = (float) Math.sqrt(Math.pow(x - itemX, 2) + Math.pow(y - itemY, 2));
			// only looks at tasks that haven't been completed
			if (!t.getComplete() && distance < shortestDistance) {
				closestTask = t;
			}
		}
		return closestTask;
	}

}
