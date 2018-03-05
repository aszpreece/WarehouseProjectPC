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
	
	public void getNextTask() {
		Job nextJob = queue.pop();
		
		ArrayList<Task> tasks = nextJob.getItemList();
		
		Float minDistance; 
		for(Task t: tasks) {
			
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
		}
	}
}
