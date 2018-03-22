package types;
/**
 * @author Minhal - Job Selection
 */
public class Task {
	
	boolean completed = false;
	/*
	 * ID of the task from 'a-z' (Use this to get item from item table)
	 */
	private String itemId;
	/*
	 * Number of items to pick from this location
	 */
	private int quantity;
	/*
	 * Reward for completing this task
	 */
	private Float reward;
	/*
	 * Item of task
	 */
	private Item myItem;
	
	public Task(String itemId, int quantity, Item myItem){
		this.itemId = itemId;
		this.quantity = quantity;
		this.myItem = myItem;
		setReward(myItem);
	}
	/*
	 * Calculates reward for completing this task
	 */
	public void setReward(Item myItem){
		//How much reward per weight
		this.reward = (myItem.getReward()*quantity)/myItem.getWeight(); 
	}
	public boolean getComplete() {
		return completed;
	}
	
	public void setComplete(boolean b){
		this.completed = b;
	}
	
	public String getId(){
		return this.itemId;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	
	public void changeQuantity(int change){
		this.quantity += change; 
	}
	
	public Float getReward(){
		return this.reward;
	}
	
	public String getItemId() {
		return itemId;
	}
	
}
