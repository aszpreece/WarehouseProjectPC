package jobmanagement;

import java.util.Comparator;
import types.Job;
/**
 * @author Minhal - Job Selection
 */
public class JobComparator implements Comparator<Job> {
	/*
	 * Order based on highest reward (Greedy algorithm for now) 
	 */
	@Override
	public int compare(Job jobOne, Job jobTwo) {
		return Double.compare(jobOne.getTotalReward(), jobTwo.getTotalReward());
	}

}
