package jobmanagement;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import bluetooth.Robot;
import filehandling.ItemTable;
import types.Item;
import types.Node;
import types.Step;
import types.Task;

/**
 * @author timch
 *
 */
public class JobAssignment {

	private static final Logger logger = Logger.getLogger(JobAssignment.class);

	/**
	 * items in the warehouse
	 */
	private ItemTable items;

	/**
	 * the drop location
	 */
	private Node DROP_LOCATION = new Node(0, 0);

	/**
	 * @param items
	 *            an item table consisting of their locations and weights
	 */
	public JobAssignment(ItemTable items) {
		this.items = items;
	}

	/**
	 * Creates the Steps that the robot needs to carry out in order to complete
	 * these list of tasks.
	 * 
	 * @return
	 */
	/**
	 * @param tasks
	 *            tasks that needs to be completed
	 * @param robot
	 *            the robot to that needs a plan
	 * @return a queue of type steps specifying what location and in what order
	 *         these items should be picked up (specifying quantity to stop robot
	 *         from picking more items than its max weight) plus when it needs to
	 *         drop items off. USAGE: pop next step off queue to get the item
	 *         coordinates to go to and how much to pick up of it
	 */
	public Queue<Step> getNextPlan(ArrayList<Task> tasks, Robot robot) {
		float robotWeight = robot.getCurrentWeight();
		float maxWeight = robot.getMaxWeight();
		Task nextTask;
		int x = robot.getCurrentX();
		int y = robot.getCurrentY();
		Queue<Step> plan = new LinkedBlockingQueue<Step>();

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
							Step step = new Step(nextTask.getId(), itemsToTake,
									new Node(currentItem.getX(), currentItem.getY()));
							plan.add(step);
							nextTask.changeQuantity(-itemsToTake);
							logger.debug("sending robot with " + itemsToTake + " out of " + quantity + " items");
						}
						// the robot is now full so it needs to head towards the drop point
						logger.debug("sending robot to drop off point");
						plan.add(new Step("DROP", DROP_LOCATION));
						x = DROP_LOCATION.getX();
						y = DROP_LOCATION.getY();
						robotWeight = 0;
						break;
					}
				}
			}
			// all items of that task can be loaded onto the robot so that task is complete
			else {
				logger.trace("all quantity of item " + nextTask.getId() + " can be loaded onto robot");
				Step step = new Step(nextTask.getId(), quantity, new Node(currentItem.getX(), currentItem.getY()));
				plan.add(step);
				nextTask.setComplete(true);
				x = currentItem.getX();
				y = currentItem.getY();
				robotWeight += currentItem.getWeight() * quantity;
			}
		}
		plan.add(new Step("DROP", DROP_LOCATION));
		logger.debug("sending robot to drop off point");
		x = (int) Math.round(DROP_LOCATION.getX());
		y = (int) Math.round(DROP_LOCATION.getY());
		robot.setCurrentWeight(0f);

		// tasks were set to complete when trying to find out how to best complete
		// tasks, therefore set them back to not complete.
		for (Task t : tasks) {
			t.setComplete(false);
		}
		return plan;
	}

	/**
	 * @param tasks
	 *            the lists of tasks for the robot to complete
	 * @param x
	 *            current simulated x position of robot
	 * @param y
	 *            current simulated y position of robot
	 * @return the closest task to the robot at the given position
	 */
	private Task getClosestTask(ArrayList<Task> tasks, int x, int y) {
		Task closestTask = null;
		float shortestDistance = Float.MAX_VALUE;

		// look at all tasks and find the shortest distance from the item to a given
		// position
		for (Task t : tasks) {
			Item currentItem = items.getItem(t.getId());
			int itemX = currentItem.getX();
			int itemY = currentItem.getY();
			// naive heuristic for distance away from item (doesn't consider walls)
			float distance = (float) Math.sqrt(Math.pow(x - itemX, 2) + Math.pow(y - itemY, 2));
			// only looks at tasks that haven't been completed
			if (!t.getComplete() && distance < shortestDistance) {
				closestTask = t;
				shortestDistance = distance;

			}
		}
		if (closestTask != null) {
			logger.trace("next item is: " + closestTask.getId());
		}
		return closestTask;
	}

}
