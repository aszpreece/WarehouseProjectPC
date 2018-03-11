package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

import filehandling.ItemTable;
import filehandling.JobTable;
import types.Item;
import types.Job;

/**
 * @author Minhal - Job Selection
 * JUnit testing for job selection
 */
public class JobSelectionTests {

	JobTable jobTable = new JobTable();
	/*
	 * Tests priority ordering
	 */
	@Test
	public void priorityQueueOrderTest() {
		// Top job reward always greater than those below it
		Queue<Job> queue = jobTable.getQueue();
		Job tmpJob = queue.poll();
		boolean valid = true;

		while (queue != null) {
			Job crntJob = queue.poll();
			int comparison = Double.compare(tmpJob.getTotalReward(), crntJob.getTotalReward());

			if (comparison < 0) {
				valid = false;
				break; // stops unnecessary loops
			}
			tmpJob = crntJob;
		}
		Assert.assertTrue(valid);
	}

	// JOB INPUT TESTS
	/*
	 * Tests if files are read by ensuring there is data in there
	 */
	@Test
	public void checkJobTableReading() {
		HashMap<String, Job> table = jobTable.getJobTable();
		Assert.assertTrue(table.size() > 0);
	}

	/*
	 * Tests if files are read by ensuring there is data in there
	 */
	@Test
	public void checkitemTableReading() {
		ItemTable table = jobTable.getItemTable();
		Assert.assertTrue(table.itemTable.size() > 0);
	}

	@Test
	public void checkItemLocationCorrect() {
		ItemTable table = jobTable.getItemTable();
		Item aaItem = table.getItem("aa");
		int x = 2;
		int y = 1;
		Assert.assertTrue(aaItem.getX() == x && aaItem.getY() == y);
		
	}

}
