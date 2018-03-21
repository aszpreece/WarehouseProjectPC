package types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Minhal - Job Selection
 */
public class Job{
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
	private Float TOTAL_REWARD;
	/**
	 * Scale factor to push a job down the queue
	 */
	private final int SCALE_FACTOR = 100;
	
	private int cancelPredictor; //1 = cancelled 0 = not to cancel
	
	public Job(ArrayList<Task> itemList, int cancelPredictor) {
		this.itemList = itemList;
		this.cancelPredictor = cancelPredictor;
		this.TOTAL_REWARD = getReward();
	}
	/*
	 * Getter for attribute itemList
	 */
	public ArrayList<Task> getItemList(){
		return itemList;
	}
	
	public Float getTotalReward(){
		return TOTAL_REWARD;
	}
	/*
	 * Calculation to get reward for this job
	 */
	private Float getReward(){
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
}
