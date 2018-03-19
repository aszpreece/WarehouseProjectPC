package pathfinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import filehandling.ItemTable;
import filehandling.JobTable;
import jobmanagement.JobAssignment;
import pathfinding.ShortestPathFinder;
import types.Job;
import types.RobotPC;
import types.Step;
import types.Task;

public class PathfinderTest {

	public static int[][] graph = { 
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
		{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
		{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
		{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
	};

	public static void main(String[] args) {

		try {
			ItemTable itemTable = new ItemTable();
			JobTable jobTable = new JobTable();

			RobotPC r1 = new RobotPC();
			r1.setCurrentX(0);
			r1.setCurrentY(0);
			r1.setCurrentWeight(0.0f);

			JobAssignment planner = new JobAssignment(itemTable);

			ShortestPathFinder pathFinder = new ShortestPathFinder(graph);

			while (true) {
				Job currentJob;
				try {
					currentJob = jobTable.popQueue();
				} catch (Exception e) {
					break;
				}
				ArrayList<Task> tasks = currentJob.getItemList();

				ArrayList<Step> steps = planner.getNextPlan(tasks, r1);

				BlockingQueue<Byte> messages = new LinkedBlockingQueue<Byte>();

				System.out.println("JobID: " + currentJob.getJobID());

				for (Step s : steps) {
					System.out.println("Robot Current X: " + r1.getCurrentX() + "\nRobot Current Y: " + r1.getCurrentY()
							+ "\nDestination X: " + s.getCoordinate().x + "\nDestination Y: " + s.getCoordinate().y);

					System.out.println(pathFinder.pathfind(r1.getCurrentX(), r1.getCurrentY(), s.getCoordinate().x,
							s.getCoordinate().y));

					messages.addAll(pathFinder.pathfind(r1.getCurrentX(), r1.getCurrentY(), s.getCoordinate().x,
							s.getCoordinate().y));

					r1.setCurrentX(s.getCoordinate().x);
					r1.setCurrentY(s.getCoordinate().y);
				}

			}

			System.out.println("All processed");

		} catch (IOException e) {
			e.printStackTrace();
			// This is bad
			System.exit(0);
		}
	}

}
