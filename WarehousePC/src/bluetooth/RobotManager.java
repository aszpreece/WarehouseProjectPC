package bluetooth;

import java.util.ArrayList;
import java.util.List;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import types.Task;

/**
 * @author tap747 May be merged or removed later.
 */
public class RobotManager extends Thread {

	int TimeStep = 0;
	
	List<NXTInfo> NXTS = new ArrayList<NXTInfo>();
	List<Robot> connections = new ArrayList<Robot>();
	ArrayList<String> names = new ArrayList<String>();

	/**
	 * connects to nxts. Should be called before starting the thread.
	 */
	public void connect() {
		
		while (connections.size() > 0) {
			Robot connection = connections.get(0);
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
		TimeStep++;
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
	
	public void cancelJob(String job) {
		
	}

	@Override
	public void run() {
		while (true) {
			if (this.checkReady()) {
				//System.out.println("All ready for instructions!");
				this.setReady(true);
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
