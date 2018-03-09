package test;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import org.junit.Assert;

import org.junit.Test;

import com.sun.xml.internal.ws.policy.AssertionSet;

import filehandling.ItemTable;
import jobmanagement.JobAssignment;
import types.Item;
import types.RobotPC;
import types.Step;
import types.Task;

public class JobAssignmentTests {
	
	private final Point DROP_LOCATION = new Point(4,4);
	
	private final ItemTable items;
	
	public JobAssignmentTests() throws IOException {
		this.items = new ItemTable();
	}

	@Test
	public void simpleOneTaskTest() {
		Item item = items.getItem("aa");
		Task task = new Task("aa", 1, item );
		ArrayList<Task> tasks = new ArrayList<Task>() {
			{
				add(task);
			}
		};

		// what the plan should be:
		ArrayList<Step> percievedPlan = new ArrayList<Step>();
		percievedPlan.add(new Step("aa", 1, new Point(item.getX(),item.getY())));
		percievedPlan.add(new Step("DROP", DROP_LOCATION));
		ItemTable itemTable;
		try {
			itemTable = new ItemTable();

			RobotPC robot = new RobotPC();
			robot.setCurrentWeight(0f);
			robot.setCurrentX(0);
			robot.setCurrentY(0);

			JobAssignment jobAllocator = new JobAssignment(itemTable);
			ArrayList<Step> actualPlan = jobAllocator.getNextPlan(tasks, robot);
			
			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TwoTaskTest() {
		Item item1 = items.getItem("aa");
		Item item2 = items.getItem("ab");
		Task task1 = new Task("aa", 1, item1);
		Task task2 = new Task("ab", 1, item2);
		ArrayList<Task> tasks = new ArrayList<Task>() {
			{
				add(task1);
				add(task2);
			}
		};

		// what the plan should be:
		ArrayList<Step> percievedPlan = new ArrayList<Step>();
		percievedPlan.add(new Step("ab", 1, new Point(item2.getX(),item2.getY())));
		percievedPlan.add(new Step("aa", 1, new Point(item1.getX(),item1.getY())));
		percievedPlan.add(new Step("DROP", DROP_LOCATION));
		
		ItemTable itemTable;
		try {
			itemTable = new ItemTable();

			RobotPC robot = new RobotPC();
			robot.setCurrentWeight(0f);
			robot.setCurrentX(3);
			robot.setCurrentY(3);

			JobAssignment jobAllocator = new JobAssignment(itemTable);
			ArrayList<Step> actualPlan = jobAllocator.getNextPlan(tasks, robot);
			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
