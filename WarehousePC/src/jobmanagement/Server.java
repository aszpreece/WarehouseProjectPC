package jobmanagement;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.whshared.network.NetworkMessage;

import bluetooth.Robot;
import filehandling.ItemTable;
import filehandling.JobTable;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import types.Job;
import types.Step;
import types.Task;
import ui.PCGUI;

public class Server extends Thread {
	
	public static void main(String[] args) {

		try {
			ItemTable itemTable = new ItemTable();
			JobTable jobTable = new JobTable();
			ArrayList<Robot> robotList  = new ArrayList<Robot>();

			
			Server robotManager = new Server();
			
			Robot r1 = robotManager.addNXT("LilBish", "00165317B895");
			
			robotManager.connect();

			Server m = new Server();
			m.start();
			
			PCGUI pcGUI = new PCGUI(jobTable, robotList);
			Thread display = new Thread(pcGUI);
			
			display.start();

			JobAssignment assigner = new JobAssignment(itemTable);

			
			
			while (true) {
				Job currentJob;
				try {
					currentJob = jobTable.popQueue();
				}catch(Exception e) {
					break;
				}
				ArrayList<Task> tasks = currentJob.getItemList();

				ArrayList<Step> steps = assigner.getNextPlan(tasks, r1);

				BlockingQueue<Byte> messages = new LinkedBlockingQueue<Byte>();
				
				//robotManager.setTask("LilBish", ); figure this out
		
				System.out.println("JobID: " + currentJob.getJobID());
				
				for(Step s : steps) {
//					System.out.println("Robot Current X: " + r1.getCurrentX() + "\nRobot Current Y: " + r1.getCurrentY() + "\nDestination X: " + s.getCoordinate().x + "\nDestination Y: " + s.getCoordinate().y);
//					dijk.pathfind(r1.getCurrentX(), r1.getCurrentY(), s.getCoordinate().y, s.getCoordinate().x);
//
//					System.out.println(dijk.getPathToFollow());
//					
//					messages.addAll(dijk.getPathToFollow());
//					
//					r1.setCurrentX(s.getCoordinate().x);
//					r1.setCurrentY(s.getCoordinate().y);
//					
//					dijk.refreshMap();
				
				}
				
				currentJob.setActive(false);
			}
			
			System.out.println("All processed");

		} catch (IOException e) {
			e.printStackTrace();
			// This is bad
			System.exit(0);
		}
	}
	
	int TimeStep = 0;
	
	List<NXTInfo> NXTS = new ArrayList<NXTInfo>();
	List<Robot> connections = new ArrayList<Robot>();
	ArrayList<String> names = new ArrayList<String>();

	/**
	 * connects to nxts. Should be called before starting the thread.
	 */
	public void connect() {
		
		for (Robot connection : connections) {
			NXTComm nxtComm;
			try {
				nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				connection.connect(nxtComm);
			} catch (NXTCommException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				connections.remove(connection);
			}
		}
	}

	public int getTimeStep() {
		return TimeStep;
	}

	/**
	 * Adds an nxt to the manager.
	 * 
	 * @param name
	 *            Name of the nxt
	 * @param address
	 *            address of nxt
	 * @return A robot object representing the robot.
	 */
	public Robot addNXT(String name, String address) {
		NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, name, address);
		NXTS.add(nxt);
		Robot r = new Robot(nxt, this);
		connections.add(r);
		return (r);
	}

	/**
	 * sets a robot to do a particular task.
	 * 
	 * @param robotName
	 * @param messages
	 */
	public void setTask(String robotName, Task t) {
		for (Robot r : connections) {
			if (r.getName().equals(robotName)) {
				r.setTask(t);
			}
		}
	}

	/**
	 * flags that all the robots are ready to move and the time step can advance.
	 */
	public void setReady(boolean v) {
		for (Robot c : connections) {
			c.setMakeNextMove(v);
		}
	}

	/**
	 * checks if all the robots are ready to move.
	 */
	public boolean checkReady() {
		for (Robot c : connections) {
			if (!c.requestingMove())
				return false;
		}
		return true;
	}

	@Override
	public void run() {
		while (true) {
			if (this.checkReady()) {
				this.setReady(true);
				TimeStep++;
			}
			
			
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void removeRobot(Robot robot) {
		connections.remove(robot);
		System.out.println("Robot: " + robot.getName() + " has disconnected");	
	}

	public void halt() {
		while(connections.size() > 0)
			connections.get(0).disconnect();	
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
