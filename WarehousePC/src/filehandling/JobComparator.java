package filehandling;
import java.util.Comparator;

import types.Job;

/*
 * Created by Minhal - Job Selection
 */
public class JobComparator implements Comparator<Job> {
	/*
	 * Order based on highest reward (Greedy algorithm) 
	 */
	@Override
	public int compare(Job jobOne, Job jobTwo) {
		return Math.round(jobTwo.getPriority() - jobOne.getPriority());
	}

}
