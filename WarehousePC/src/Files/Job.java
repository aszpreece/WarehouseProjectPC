package Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

/*
 * Created by Minhal - Job Selection
 */
public class Job{
	/**
	 * Stores all the items that pertain to each job.
	 */
	private ArrayList<Task> itemList = new ArrayList<Task>();
	/*
	 * Total reward for this whole job
	 */
	private Float TOTAL_REWARD;
	
	public Job(ArrayList<Task> itemList) {
		this.itemList = itemList;
		this.TOTAL_REWARD = getReward();
		System.out.println(this.TOTAL_REWARD);
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
		
		return sumReward;
	}
}