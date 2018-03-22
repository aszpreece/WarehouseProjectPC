package jobmanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import com.whshared.network.NetworkMessage;

import bluetooth.Robot;
import filehandling.ItemTable;
import filehandling.JobTable;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import pathfinding.CAStar;
import pathfinding.ReservationTable;
import types.Job;
import types.Node;
import types.Step;
import ui.PCGUI;

public class Server extends Thread {

	public static void main(String[] args) {
		Server m = new Server();
		m.start();

	}
	
	volatile float score = 0;
	volatile ReservationTable rTable = new ReservationTable();
	volatile boolean pause = false;

	int timeStep = 0;

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
		return timeStep;
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
		Robot r = new Robot(new NXTInfo(NXTCommFactory.BLUETOOTH, name, address), rTable, this);
		connections.add(r);
		return (r);
	}

	/**
	 * flags that all the robots are ready to move and the time step can
	 * advance.
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
			//System.out.println("Robot " + c.getName() + " is req " + c.requestingMove() + " and is parked " + c.isParked());
			if (!c.requestingMove() || c.isParked())
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

		ArrayList<Robot> robotList = new ArrayList<Robot>();
		Robot bish;
		robotList.add((bish = addNXT("LilBish", "00165317B895")));
		bish.setCurrentX(0);
		bish.setCurrentY(1);
		bish.setDestination(bish.getX(), bish.getY());
		Robot poppy;
		robotList.add((poppy = addNXT("Poppy", "001653089A83")));
		poppy.setCurrentX(0);
		poppy.setCurrentY(2);
		
		Robot lego;
		robotList.add((lego = addNXT("LEGOlas (DAB)", "0016530898D0")));
		lego.setCurrentX(0);
		lego.setCurrentY(4);
		
		 connect();
		 
//		 PCGUI pcGUI = new PCGUI(jobTable, this);
//		 Thread display = new Thread(pcGUI);
//		 display.start();
		 
		ArrayList<Node> dropOffs = new ArrayList<Node>();
		dropOffs.add(new Node(0, 0));
		dropOffs.add(new Node(11, 7));
		 
		JobAssignment assigner = new JobAssignment(itemTable, dropOffs);


		CAStar pathfinder = new CAStar(rTable);

		// once all the set up is complete we begin the main server loop. This
		// constantly makes sure that each robot has a job assigned to it.
		Map<Robot, Job> jobMap = new HashMap<Robot, Job>();
		Map<Robot, Queue<Step>> stepMap = new HashMap<Robot, Queue<Step>>();
//		Node AStart = new Node(0, 7);
//		Node BStart = new Node(0, 0);
//
//		poppy.setInstructions(pathfinder.pathfind(new Node(poppy.getX(),
//		poppy.getY()), BStart, getTimeStep()));
//		System.out.println("found poppys path");
//		bish.setInstructions(pathfinder.pathfind(new Node(bish.getX(),
//		bish.getY()), AStart, getTimeStep()));
		
		while (true) {
	
			for (Robot r : connections) {
				if (!jobMap.containsKey(r) || !jobMap.get(r).getActive()) {
					System.out.println("Giving a new job to " + r.getName());
					Job newJob = jobTable.popQueue();
					newJob.setActive(true);
					jobMap.put(r, newJob);
					Queue<Step> robotSteps = assigner.getNextPlan(newJob.getItemList(), r);
					stepMap.put(r, robotSteps);
					r.clearInstructions();
				}

				//if the robot is out of instructions, i.e it has completed a given route...
				if (!r.hasInstructions() && r.isReady()) {

					Step robotStep;
					robotStep = stepMap.get(r).poll();
	
					if (robotStep != null) {
						if (r.getCurrentStep() != null) {
							r.getCurrentStep().setStepComplete();
							System.out.println(r.getName() + " completed a step");
						}
						r.setCurrentStep(robotStep);
						List<Byte> instructions = pathfinder.pathfind(new Node(r.getX(), r.getY()), robotStep.getCoordinate(),
								getTimeStep());

						robotStep.getCommand();
						System.out.println(robotStep.getCoordinate().toString());
						System.out.println(robotStep.getCommand());
					    if (robotStep.getCommand().equals("DROP")) {

							instructions.add(NetworkMessage.AWAIT_DROPOFF);
						} else {
							instructions.add(NetworkMessage.AWAIT_PICKUP);
						}
						System.out.println(r.getName() + " instructions: " + instructions);
						r.setInstructions(instructions);
					} else {
						System.out.println("Job completed");
						score += jobMap.get(r).getTotalReward();
						jobMap.get(r).setActive(false);
					}
				}
     		}
			if (this.checkReady() && !pause) {
				this.setReady(true);
		/*		try {
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				timeStep++;
//				System.out.println(timeStep);
//				rTable.print(timeStep, 3);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public float getScore() {
		return score;
	}
	
	public void setPaused(boolean p) {
		pause = p;
	}

	public void removeRobot(Robot robot) {
		connections.remove(robot);
		System.out.println("Robot: " + robot.getName() + " has disconnected");
	}

	public void halt() {
		while (connections.size() > 0)
			connections.get(0).disconnect();
	}

	public List<Robot> getConnectedRobots() {
		return connections;
	}

}
