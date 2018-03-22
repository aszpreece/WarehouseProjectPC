package jobmanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.whshared.network.NetworkMessage;

import bluetooth.Robot;
import filehandling.ItemTable;
import filehandling.JobTable;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import pathfinding.AStar;
import pathfinding.CAStar;
import pathfinding.ReservationTable;
import types.Job;
import types.Node;
import types.Step;
import types.Task;
import ui.PCGUI;

public class Server extends Thread {
	
	
	public static void main(String[] args) {
		Server m = new Server();
		m.start();

	}
	
	volatile ReservationTable rTable = new ReservationTable();
	
	boolean pause = false;
	
	int TimeStep = 0;
	
	List<Robot> connections = new ArrayList<Robot>();

	/**
	 * connects to nxts. Should be called before starting the thread.
	 */
	public void connect() {
		List<Robot> failed = new ArrayList<Robot>();
		for (Robot connection : connections) {
			NXTComm nxtComm;
			try {
				nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				connection.connect(nxtComm);
			} catch (NXTCommException e) {
				System.out.println("Failed to connect to robot " + connection.getName());
				failed.add(connection);
			}
		}
		connections.removeAll(failed);
		
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
		Robot r = new Robot(new NXTInfo(NXTCommFactory.BLUETOOTH, name, address), rTable ,this);
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
			if (!c.requestingMove() || !c.isParked())
				return false;
		}
		return true;
	}

	@Override
	public void run() {
	   int[][] graph = { 
	    		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
	            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
	            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
	            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
	            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
	            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	    };
	    
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
	
		//rTable.parkPosition(new Node(1, 0));
		rTable.reservePosition(new Node(1, 0), 1);
		System.out.println(rTable.isReserved(new Node(1, 0), 1));
		System.out.println(rTable.isReserved(new Node(2, 0), 1));
		ArrayList<Robot> robotList  = new ArrayList<Robot>();
		Robot bish;
		robotList.add((bish = addNXT("LilBish", "00165317B895")));
		bish.setCurrentX(0);
		bish.setCurrentY(1);
		Robot poppy;
		robotList.add((poppy = addNXT("Poppy", "001653089A83")));
		poppy.setCurrentX(0);
		poppy.setCurrentY(7);
//		
//		Robot lego;
//		robotList.add((lego = addNXT("LEGOlas (DAB)", "0016530898D0")));
//		lego.setCurrentX(0);
//		lego.setCurrentY(4);
		
		PCGUI pcGUI = new PCGUI(jobTable, this);
		Thread display = new Thread(pcGUI);
		display.start();

		JobAssignment assigner = new JobAssignment(itemTable);
		//ShortestPathFinder pathfinder = new ShortestPathFinder(null);
		connect();
		
		CAStar pathfinder = new CAStar(rTable);
		
		//once all the set up is complete we bign the main server loop. This constantly makes sure that each robot has a job assigned to it.
		Map<Robot, Job> jobMap = new HashMap<Robot, Job>();
		Map<Robot, Queue<Step>> stepMap = new HashMap<Robot, Queue<Step>>();
//		Node poppyGoal = new Node(0, 0);
//		Node bishGoal = new Node(0, 7);
//		poppy.setInstructions(pathfinder.pathfind(new Node(poppy.getX(), poppy.getY()), poppyGoal, getTimeStep()));
//		System.out.println("found poppys path");
//		bish.setInstructions(pathfinder.pathfind(new Node(bish.getX(), bish.getY()), bishGoal, getTimeStep()));
//		
		while (true) {

			for (Robot r : connections) {
				if (!jobMap.containsKey(r) || !jobMap.get(r).getActive()) {	
					System.out.println("Giving a new job to " + r.getName());
					Job newJob = jobTable.popQueue();
					jobMap.put(r, newJob);
					System.out.println(newJob.getItemList());
					Queue<Step> robotSteps = assigner.getNextPlan(newJob.getItemList(), r);
					System.out.println("ROBOT STEPS:");
					System.out.println(robotSteps);
					stepMap.put(r, robotSteps);
					r.clearInstructions();
				}
				if(!r.hasInstructions()) {
					Step robotStep = stepMap.get(r).poll();
					if (robotStep != null) {
						List<Byte> instructions = new LinkedList<Byte>();
						r.setInstructions(pathfinder.pathfind(new Node(r.getX(), r.getY()), robotStep.getCoordinate(), getTimeStep()));						if(robotStep.getCommand().equals("DROP")) {
							instructions.add(NetworkMessage.AWAIT_DROPOFF);			
						} else {
							instructions.add(NetworkMessage.AWAIT_PICKUP);
						}
						System.out.println("Instructions: " + instructions);
						r.setInstructions(instructions);
					} else {
						jobMap.get(r).setActive(false);
					}
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
	

}
