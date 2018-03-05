package Files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * Created by Minhal - Job Selection
 */
public class JobTable {
	/*
	 * Stores all items into a hash map, where the key is job ID which maps to its job class
	 */
	HashMap<String, Job> jobTable;
	private static JobQueue queue = new JobQueue();
	private static ItemTable itemTable;
	
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
	 * Gets total reward for a job with given id
	 */
	public Float getReward(String id){
		return jobTable.get(id).getTotalReward();
	}
	/*
	 * Pops top job from queue and returns it
	 */
	public Job popQueue(){
		return queue.pop();
	}
	/*
	 * Creates table based on CSV files
	 */
	public static HashMap<String, Job> createTable() throws IOException{
		HashMap<String, Job> jobTable = new HashMap<>();
		//Opening files to read
		BufferedReader jobs = new BufferedReader(FileHandling.getFileReader(FileHandling.JOBS_FILE_NAME));
		//current lines we are looping
		String jobLine = ""; 
		//Creating itemTable
		itemTable = new ItemTable();
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
			Job currentJob = new Job(taskList);
			//Adding job onto table
            jobTable.put(key, currentJob);
            //Adding job into queue
            queue.push(currentJob);
		}
		return jobTable;
	}

}
