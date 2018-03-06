package Files;

import java.util.ArrayList;

public class JobAssignment {
	
	private JobQueue queue;
	private ArrayList<Robot> robots;
	private ItemTable items;
	
	public JobAssignment(JobQueue queue, ArrayList<Robot> robots, ItemTable items) {
		this.queue = queue;
		this.robots = robots;
		this.items = items;
	}
	
	public void getNextPlan() {
		Job nextJob = queue.pop();
		float robotWeight = 0;
		float maxWeight = robot.getWeight();
		ArrayList<Task> tasks = nextJob.getItemList();
		Task nextTask;
		int x = robot.getX();
		int y = robot.getY();
		ArrayList<String> plan;
		while((nextTask = getClosestTask(tasks, x, y)) != null) {
			int quantity = nextTask.getQuantity();
			Item currentItem = items.getItem(nextTask.getId());
			if(currentItem.getWeight() * quantity + robotWeight > maxWeight) {
				for(int i = 1; i <= quantity; i++) {
					if(currentItem.getWeight() * i + robotWeight > maxWeight) {
						if(i != 1) {
							int itemsToTake = i - 1;
							plan.add("pick" + nextTask.getId() + itemsToTake);
							nextTask.changeQuantity(-itemsToTake);
						}
						plan.add("drop");
						x = 0; // DROP LOCATION
						y = 0; //DROP LOCATION
						break;
					}
				}
			}
			else {
				plan.add(nextTask.getId() + quantity);
				nextTask.setComplete(true);;
			}
		}
	}
	
	private  ArrayList<Task> travellingSalesman (ArrayList<Task> tasks, int robotX, int robotY) {
	}
	
	private Task getClosestTask(ArrayList<Task> tasks, int x, int y) {
		Task closestTask;
		Float shortestDistance = Float.MAX_VALUE;
		for(Task t: tasks) {
			Item currentItem = items.getItem(t.getId());
			int itemX = currentItem.getX();
			int itemY = currentItem.getY();
			Float distance = (float) Math.sqrt(Math.pow(x - itemX, 2) + Math.pow(y - itemY, 2));
			if (!t.getComplete() && distance < shortestDistance) {
				closestTask = t;
			}
		}
		if (shortestDistance == Float.MAX_VALUE){
			return closestTask;
		}
		else {
			return null;
		}
	}
}
