import java.util.ArrayList;
import java.util.Map.Entry;

/*
 * Created by Minhal - Job Selection
 */
public class Job{
	/**
	 * Stores all the items that pertain to each job.
	 */
	private ArrayList<Task> itemList = new ArrayList<Task>();

	public Job(ArrayList<Task> itemList) {
		this.itemList = itemList;
	}
	/*
	 * Getter for attribute itemList
	 */
	public ArrayList<Task> getItemList(){
		return itemList;
	}
	
}
