package types;

import java.util.concurrent.BlockingQueue;

/**
 * Robot skeleton for other people to work with
 * @author b1999
 *
 */
public class RobotPC {
	
	public static final int NORTH = 0;
	public static final int EAST = 90;
	public static final int SOUTH = 180;
	public static final int WEST = 270;
	
	public enum Movement{
		FORWARD, LEFT, RIGHT, BACKWARD
	}
	

	/**
	 * Store the Queue of directions to be performed, which has the .
	 */
	private BlockingQueue<Movement> movementQueue;
	
	private Integer currentX;
	private Integer currentY;
	
	private Float currentWeight;
	private Float maxWeight = 50f; // Maybe finalise
	
	
	/** 
	 * Set to true to cancel the current job in progress.
	 */
	private boolean cancelJob = false;
	
	/**
	 * Current direction is degrees.
	 */
	private Integer currentDirection;
	
	private String robotName;
	
	/**
	 * Set to a value NORTH (0), EAST (90), SOUTH (180), WEST (270)
	 * @param direction The direction to set
	 */
	public void setDirectionCurrent(Integer direction) {
		currentDirection = direction;
	}
	
	public boolean checkJobRunning() {
		return !cancelJob;
	}

	/** 
	 * As a form a data storage the robot should know the current job its carrying out 
	 * */
	public Integer getCurrentJob() {
		// TODO Auto-generated method stub
		return 1000;
	}

	public Integer getCurrentX() {
		return currentX;
	}

	public void setCurrentX(Integer currentX) {
		this.currentX = currentX;
	}

	public Integer getCurrentY() {
		return currentY;
	}

	public void setCurrentY(Integer currentY) {
		this.currentY = currentY;
	}

	public Float getCurrentWeight() {
		return currentWeight;
	}

	public void setCurrentWeight(Float currentWeight) {
		this.currentWeight = currentWeight;
	}

	public Float getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Float maxWeight) {
		this.maxWeight = maxWeight;
	}
	
}
