package jobmanagement;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.whshared.network.NetworkMessage;

import bluetooth.RobotManager;

import filehandling.ItemTable;
import filehandling.JobTable;
import pathfinding.ShortestPathFinder;
import types.Job;
import types.RobotPC;
import types.Step;
import types.Task;
import ui.PCGUI;

public class JobManagerServer {

	ItemTable itemTable;
	JobTable jobTable;

	public JobManagerServer() {

		try {
			this.jobTable = new JobTable();
			this.itemTable = new ItemTable();

		} catch (FileNotFoundException i) {
			// File not found
			i.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		try {
			ItemTable itemTable = new ItemTable();
			JobTable jobTable = new JobTable();
			ArrayList<RobotPC> robotList  = new ArrayList<RobotPC>();

			
			
			RobotManager robotManager = new RobotManager();
			robotManager.addNXT("LilBish", "00165317B895");
			// robotManager.addNXT("Poppy", "001653089A83");
			robotManager.connect();

			Thread m = new Thread(robotManager);
			m.start();

			// robotManager.start();

			RobotPC r1 = new RobotPC();
			r1.setCurrentX(0);
			r1.setCurrentY(0);
			r1.setCurrentWeight(0.0f);
			robotList.add(r1);
			
			PCGUI pcGUI = new PCGUI(jobTable, robotList);
			Thread display = new Thread(pcGUI);
			
			display.start();

			JobAssignment planner = new JobAssignment(itemTable);
			BlockingQueue<Byte> messages = new LinkedBlockingQueue<Byte>();
			robotManager.setMovementQueue("LilBish", messages);
			
			// The loop that gets each of the jobs from the priority queue and pops them off path finding to be run on
			while (true) {
				Job currentJob;
				try {
					currentJob = jobTable.popQueue();
				}catch(Exception e) {
					break;
				}
				ArrayList<Task> tasks = currentJob.getItemList();

				ArrayList<Step> steps = planner.getNextPlan(tasks, r1);


				ShortestPathFinder pathfinder = new ShortestPathFinder();
				
				
				//BlockingQueue<>
				// We may need a instruction type which is a queue of actions, so that when cancellations occur, whole instructions are cancelled, in stead pf just the current action
				
				
				System.out.println("JobID: " + currentJob.getJobID());
				
				for(Step s : steps) {
					System.out.println("Robot Current X: " + r1.getCurrentX() + "\nRobot Current Y: " + r1.getCurrentY() + "\nDestination X: " + s.getCoordinate().x + "\nDestination Y: " + s.getCoordinate().y);	
					
					System.out.println(pathfinder.pathfind(r1.getCurrentX(), r1.getCurrentY(), s.getCoordinate().x, s.getCoordinate().y));
					
					messages.addAll(pathfinder.pathfind(r1.getCurrentX(), r1.getCurrentY(), s.getCoordinate().x, s.getCoordinate().y));
					
					r1.setCurrentX(s.getCoordinate().x);
					r1.setCurrentY(s.getCoordinate().y);
					
					
				}
				
				/*for (Step s : steps) {
					System.out.println("");
					if (s.getCommand().length() == 2) {
						System.out.println("GET: " + s.getQuantity() + " of " + s.getCommand() + "- LOCATE TO: "
								+ s.getCoordinate());
					} else {
						System.out.println("route to drop off point");
					}
					// ArrayList<byte> messages = pathfinder.getPath(point, s.coordinate());
					spoofRoutePlanning(messages);
			
				}*/
				currentJob.setActive(false);
			}
			
			(new BufferedReader(new InputStreamReader(System.in))).readLine();
			
			System.out.println("All processed");

		} catch (IOException e) {
			e.printStackTrace();
			// This is bad
			System.exit(0);
		}
		
		
		
	}

	public static void spoofRoutePlanning(BlockingQueue<Byte> messages) throws IOException {
		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
		String input;
		System.out.println("Input a direction sequence (input '.' to finish):\n");
		while (!(input = user.readLine()).equals(".")) {
			if (input.equals("n")) {
				messages.offer(NetworkMessage.MOVE_NORTH);
			} else if (input.equals("e")) {
				messages.offer(NetworkMessage.MOVE_EAST);
			} else if (input.equals("s")) {
				messages.offer(NetworkMessage.MOVE_SOUTH);
			} else if (input.equals("w")) {
				messages.offer(NetworkMessage.MOVE_WEST);
			} else if (input.equals("p")) {
				messages.offer(NetworkMessage.AWAIT_PICKUP);
				break;
			}
		}

	}

}
