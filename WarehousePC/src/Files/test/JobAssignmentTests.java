package Files.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import org.junit.Assert;

import org.junit.Test;

import com.sun.xml.internal.ws.policy.AssertionSet;

import Files.Item;
import Files.ItemTable;
import Files.JobAssignment;
import Files.RobotPC;
import Files.Task;

public class JobAssignmentTests {

	@Test
	public void simpleOneTaskTest() {
		Item item = new Item(1, 1, 1.0f, 1.0f);
		Task task = new Task("aa", 1, item);
		ArrayList<Task> tasks = new ArrayList<Task>() {{add(task);}};
		ArrayList<String> percievedPlan = new ArrayList<String>();
		percievedPlan.add("aa1");
		percievedPlan.add("drop");
		ItemTable itemTable;
		try {
			itemTable = new ItemTable();
			
			RobotPC robot = new RobotPC();
			robot.setCurrentWeight(0f);
			robot.setCurrentX(0);
			robot.setCurrentY(0);
			
			JobAssignment jobAllocator = new JobAssignment(itemTable);
			ArrayList<String >actualPlan = jobAllocator.getNextPlan(tasks, robot);
			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
