package Files;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * Created by Minhal - Job Selection
 */
public class JobQueue {
	
	private Queue<Job> queue = new PriorityQueue<Job>(new JobComparator());
	
	/*
	 * Adds passed job 
	 */
	public void push(Job j){
		queue.add(j);
	}
	/*
	 * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty. 
	 */
	public Job pop(){
		return queue.poll();
	}
	
	
	public Job peek(){
		return queue.peek();
	}
	

}
