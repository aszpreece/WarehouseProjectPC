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
		ArrayList<Task> tasks = new ArrayList<Task>() {
			{
				add(task);
			}
		};

		// what the plan should be:
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
			ArrayList<String> actualPlan = jobAllocator.getNextPlan(tasks, robot);
			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TwoTaskTest() {
		Item item1 = new Item(1, 1, 1.0f, 1.0f);
		Item item2 = new Item(2, 2, 1.0f, 1.0f);
		Task task1 = new Task("aa", 1, item1);
		Task task2 = new Task("ab", 1, item2);
		ArrayList<Task> tasks = new ArrayList<Task>() {
			{
				add(task1);
				add(task2);
			}
		};

		// what the plan should be:
		ArrayList<String> percievedPlan = new ArrayList<String>();
		percievedPlan.add("ab1");
		percievedPlan.add("aa1");
		percievedPlan.add("drop");
		ItemTable itemTable;
		try {
			itemTable = new ItemTable();

			RobotPC robot = new RobotPC();
			robot.setCurrentWeight(0f);
			robot.setCurrentX(3);
			robot.setCurrentY(3);

			JobAssignment jobAllocator = new JobAssignment(itemTable);
			ArrayList<String> actualPlan = jobAllocator.getNextPlan(tasks, robot);
			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
