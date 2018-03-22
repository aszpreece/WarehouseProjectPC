package types;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Minhal - Job Selection
 */
public class Job{
	private volatile boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	/*
	 * Indicates whether a job is active
	 */
	private boolean active = false;

	/*
	 * Job ID
	 */
	String jobID;
	/**
	 * Stores all the items that pertain to each job.
	 */
	private ArrayList<Task> itemList = new ArrayList<Task>();
	/*
	 * Total reward for this whole job
	 */
	private float totalReward;
	
	private float priority;
	/**
	 * Scale factor to push a job down the queue
	 */
	private final float SCALE_FACTOR = 0.01f;
	
	private int cancelPredictor; //1 = cancelled 0 = not to cancel
	
	public Job(ArrayList<Task> itemList, int cancelPredictor) {
		this.totalReward = calculateReward();
		this.itemList = itemList;
		this.cancelPredictor = cancelPredictor;
		this.priority = calculatePriority();
	}
	/*
	 * Getter for attribute itemList
	 */
	public ArrayList<Task> getItemList(){
		return itemList;
	}
	
	public float getPriority(){
		return priority;
	}
	
	private float calculateReward() {
		float total = 0;
		for (Task t : itemList) {
			total += t.getReward();
		}
		return total;
	}
	
	/*
	 * Calculation to get reward for this job
	 */
	private float calculatePriority(){
		Float sumReward = 0f;
		Iterator<Task> i = itemList.iterator();
		
		while (i.hasNext()){
			Task crntTask = i.next();
			sumReward += crntTask.getReward();
		}
		
		if (this.cancelPredictor == 1) {
			//Most likely to get cancelled hence a scale factor to push down the queue
			sumReward *= SCALE_FACTOR;
		}

		return sumReward;
	}
	
	public float getTotalReward() {
		return totalReward;
	}
	
	/*
	 * Determines whether the job is active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/*
	 * Ascertains whether the job is active
	 */
	public boolean getActive() {
		return this.active && !isCancelled();
	}
	
	/*
	 * Gives the percentage completion of this job
	 */
	public float getPercentageComplete() { 
		int numTasksCompleted=0;
		
		for(int i=0; i<itemList.size(); i++) {
			if(itemList.get(i).getComplete() == true) {
				numTasksCompleted++;
			}
		}
		
		return new Float((numTasksCompleted/itemList.size())*100);
	}
	
}
