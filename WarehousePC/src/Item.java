
public class Item {
	
	/**
	 * Reward for successfully moving the item.
	 */
	private Float reward;
	/**
	 * The weight of the item.
	 */
	private Float weight;
	
	/**
	 * x co-ordinate of the item.
	 */
	private int x;
	/**
	 * y co-ordinate of the item.
	 */
	private int y;
	
	
	public Item(int x, int y, Float reward, Float weight) {
		this.x = x;
		this.y = y;
		this.reward = reward;
		this.weight = weight;
	}
	
}
