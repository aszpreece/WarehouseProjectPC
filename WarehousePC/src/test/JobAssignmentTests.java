//package test;
//
//import java.awt.Point;
//import java.io.IOException;
//import java.util.ArrayList;
//import org.junit.Assert;
//
//import org.junit.Test;
//
//import bluetooth.Robot;
//import filehandling.ItemTable;
//import jobmanagement.JobAssignment;
//import types.Item;
//import types.Node;
//import types.Step;
//import types.Task;
//
//public class JobAssignmentTests {
//	
//	private final Node DROP_LOCATION = new Node(4,4);
//	
//	private final ItemTable items;
//	
//	public JobAssignmentTests() throws IOException {
//		this.items = new ItemTable();
//	}
//
//	@Test
//	public void simpleOneTaskTest() {
//		Item item = items.getItem("aa");
//		Task task = new Task("aa", 1, item );
//		ArrayList<Task> tasks = new ArrayList<Task>() {
//			{
//				add(task);
//			}
//		};
//
//		// what the plan should be:
//		ArrayList<Step> percievedPlan = new ArrayList<Step>();
//		percievedPlan.add(new Step("aa", 1, new Node(item.getX(),item.getY())));
//		percievedPlan.add(new Step("DROP", DROP_LOCATION));
//		ItemTable itemTable;
//		try {
//			itemTable = new ItemTable();
//
//			Robot robot = new Robot(null, null);
//			robot.setCurrentWeight(0f);
//			robot.setCurrentX(0);
//			robot.setCurrentY(0);
//
//			JobAssignment jobAllocator = new JobAssignment(itemTable);
//			ArrayList<Step> actualPlan = jobAllocator.getNextPlan(tasks, robot);
//			
//			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	@Test
//	public void TwoTaskTest() {
//		Item item1 = items.getItem("aa");
//		Item item2 = items.getItem("ab");
//		Task task1 = new Task("aa", 1, item1);
//		Task task2 = new Task("ab", 1, item2);
//		ArrayList<Task> tasks = new ArrayList<Task>() {
//			{
//				add(task1);
//				add(task2);
//			}
//		};
//
//		// what the plan should be:
//		ArrayList<Step> percievedPlan = new ArrayList<Step>();
//		percievedPlan.add(new Step("ab", 1, new Node(item2.getX(),item2.getY())));
//		percievedPlan.add(new Step("aa", 1, new Node(item1.getX(),item1.getY())));
//		percievedPlan.add(new Step("DROP", DROP_LOCATION));
//		
//		ItemTable itemTable;
//		try {
//			itemTable = new ItemTable();
//
//			Robot robot = new Robot();
//			robot.setCurrentWeight(0f);
//			robot.setCurrentX(3);
//			robot.setCurrentY(3);
//
//			JobAssignment jobAllocator = new JobAssignment(itemTable);
//			ArrayList<Step> actualPlan = jobAllocator.getNextPlan(tasks, robot);
//			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	@Test
//	public void OverweightTwoTaskTest() {
//		Item item1 = items.getItem("aa"); // item of 1.23 weight (2,1)
//		Item item2 = items.getItem("fb"); //item of 5.01 weight (8,2)
//		Task task1 = new Task("aa", 1, item1);
//		Task task2 = new Task("fb", 10, item2);
//		ArrayList<Task> tasks = new ArrayList<Task>() {
//			{
//				add(task1);
//				add(task2);
//			}
//		};
//
//		// what the plan should be:
//		ArrayList<Step> percievedPlan = new ArrayList<Step>();
//		percievedPlan.add(new Step("aa", 1, new Node(item1.getX(),item1.getY())));
//		percievedPlan.add(new Step("fb", 9, new Node(item2.getX(),item2.getY())));
//		percievedPlan.add(new Step("DROP", DROP_LOCATION));
//		percievedPlan.add(new Step("fb", 1, new Node(item2.getX(),item2.getY())));
//		percievedPlan.add(new Step("DROP", DROP_LOCATION));
//		
//		ItemTable itemTable;
//		try {
//			itemTable = new ItemTable();
//
//			Robot robot = new Robot();
//			robot.setCurrentWeight(0f);
//			robot.setCurrentX(1);
//			robot.setCurrentY(1);
//
//			JobAssignment jobAllocator = new JobAssignment(itemTable);
//			ArrayList<Step> actualPlan = jobAllocator.getNextPlan(tasks, robot);
//			Assert.assertArrayEquals(percievedPlan.toArray(), actualPlan.toArray());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//}
