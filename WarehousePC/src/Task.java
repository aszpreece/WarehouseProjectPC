
public class Task {
	
	/*
	 * ID of the task from 'a-z' (Use this to get item from item table)
	 */
	private String itemId;
	
	/*
	 * Number of items to pick from this location
	 */
	private int quantity;
	
	
	public Task(String itemId, int quantity){
		this.itemId = itemId;
		this.quantity = quantity;
	}
	
	public String getId(){
		return itemId;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
}
