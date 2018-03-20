package jobmanagement;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
import pathfinding.MultiPathfinder;
import pathfinding.ShortestPathFinder;
import types.Job;
import types.Step;
import types.Task;
import ui.PCGUI;

public class Server extends Thread {
	
	public static void main(String[] args) {
		Server m = new Server();
		m.start();

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
		ItemTable itemTable;
		JobTable jobTable;
		try {
			itemTable = new ItemTable();
			jobTable = new JobTable();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Server failed to start");
			return;
		}
	
		ArrayList<Robot> robotList  = new ArrayList<Robot>();
		robotList.add(addNXT("LilBish", "00165317B895"));
		
		
		PCGUI pcGUI = new PCGUI(jobTable, robotList);
		Thread display = new Thread(pcGUI);
		display.start();

		JobAssignment assigner = new JobAssignment(itemTable);
		//ShortestPathFinder pathfinder = new ShortestPathFinder(null);
		connect();
		
		MultiPathfinder pathfinder = new MultiPathfinder();
		ArrayList<Byte> directions = new ArrayList<Byte>();
		
		//once all the set up is complete we bign the main server loop. This constantly makes sure that each robot has a job assigned to it.
		Map<Robot, Job> jobMap = new HashMap<Robot, Job>();
		Map<Robot, Queue<Step>> stepMap = new HashMap<Robot, Queue<Step>>();
		while (true) {

			for (Robot r : robotList) {
				if (!jobMap.containsKey(r) || !jobMap.get(r).getActive()) {	
					Job newJob = jobTable.popQueue();
					jobMap.put(r, newJob);
					Queue<Step> robotSteps = assigner.getNextPlan(newJob.getItemList(), r);
					stepMap.put(r, robotSteps);
					r.clearInstructions();
				}
				if(!r.hasInstructions()) {
					Step robotStep = stepMap.get(r).poll();
					directions = pathfinder.Pathfinder(r.getX(), r.getY(), robotStep.getCoordinate().getX(), robotStep.getCoordinate().getY());
				}
				
				
				if (!r.hasInstructions()) {
					//give robot next set of moves.
				}
			}
		
			
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
