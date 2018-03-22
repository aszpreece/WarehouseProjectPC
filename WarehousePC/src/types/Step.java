package types;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Step {

	private Node coordinate;

	private String command;
	
	private boolean complete = false;

	private int quantity;
	
	private ArrayList<Task> tasks;

	/**
	 * @param command the command (drop or an item name for pickup)
	 * @param coordinate A type node containing coordinates to travel to in the plan
	 */
	public Step(String command, Node coordinate) {
		this.command = command;
		this.coordinate = coordinate;
	}

	/**
	 * @param item the item code of the item to fetch
	 * @param quantity amount of items to pick up (may not be the same as task as robot can have a full inventory)
	 * @param coordinate coordinate A type node containing coordinates to travel to in the plan
	 */
	public Step(String item, int quantity, Node coordinate) {
		this.command = item;
		this.coordinate = coordinate;
		this.quantity = quantity;
	}

	public Node getCoordinate() {
		return coordinate;
	}

	public String getCommand() {
		return command;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "(" + coordinate.getX() +","+ coordinate.getY() + ")" + command + quantity;
	}

	@Override
	public boolean equals(Object s) {
		Step step = (Step)s;
		return coordinate.equals(step.getCoordinate()) && command.equals(step.getCommand())
				&& quantity == step.getQuantity();
	}
	
	
	public void setStepComplete() {
		if(command.equals("DROP")) {
			for(Task t: tasks) {
				t.setComplete(true);
			}
		}
		complete = true;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void setDropAssociatedTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
}
