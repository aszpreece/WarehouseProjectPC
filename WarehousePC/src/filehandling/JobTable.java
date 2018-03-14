package filehandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import types.Item;
import types.Job;
import types.Task;
/*
 * Created by Minhal - Job Selection
 */
public class JobTable {
	/*
	 * Stores all items into a hash map, where the key is job ID which maps to its job class
	 */
	private HashMap<String, Job> jobTable;
	/*
	 * Priorities all jobs into a queue based on the sum of rewards of each task
	 */
	private Queue<Job> queue = new PriorityQueue<Job>(new JobComparator());
	/*
	 * Creates item table, to pass item to each task
	 */
	private ItemTable itemTable = new ItemTable();;
	
	public JobTable() throws IOException {
		this.jobTable = createTable();
	}
	/*
	 * Gets jobs tasks with id
	 */
	public ArrayList<Task> getTaskList(String id){
		return jobTable.get(id).getItemList();
	}
	/*
	 * Get the table of jobs
	 */
	public HashMap<String, Job> getJobTable(){
		return this.jobTable;
	}
	/*
	 * Gets total reward for a job with given id
	 */
	public Float getReward(String id){
		return jobTable.get(id).getTotalReward();
	}
	/*
	 * Pops top job from queue and returns it
	 * returns null if queue is empty
	 */
	public Job popQueue(){
		Job j = queue.poll();
		j.setActive(true);
		return j; 
	}
	/*
	 * Cancels a job by removing it according
	 * to its JobID
	 */
	public void cancel(String id) {
		jobTable.get(id).setActive(false);
	}
	/*
	 * Creates table based on CSV files
	 */
	public HashMap<String, Job> createTable() throws IOException{
		HashMap<String, Job> jobTable = new HashMap<>();
		//Opening files to read
		BufferedReader jobs = new BufferedReader(FileHandling.getFileReader(FileHandling.JOBS_FILE_NAME));
		//current lines we are looping
		String jobLine = ""; 
		//Loop through each line
		while ((jobLine = jobs.readLine()) != null) {
			//[JobID, <itemID, quantity>] <> = more than one pair
			String[] line = jobLine.split(FileHandling.cvsSplitBy);
			String key = line[0];
			ArrayList<Task> taskList = new ArrayList<Task>();

			for(int i=1;i<line.length-1;i++){
				String itemId = line[i];
				int quantity = Integer.parseInt(line[++i]);
				//Getting item of current Task
				Item crntItm = itemTable.getItem(itemId);
				Task crntTask = new Task(itemId, quantity, crntItm);
				taskList.add(crntTask);
			}
			Job currentJob = new Job(key, taskList);
			//Adding job onto table
            jobTable.put(key, currentJob);
            //Adding job into queue
            queue.add(currentJob);
		}
		return jobTable;
	}
}
