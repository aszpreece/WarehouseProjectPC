package com.whpc.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.whshared.network.NetworkMessage;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/**
 * @author tap747 May be merged or removed later.
 */
public class RobotManager implements Runnable {

	public static void main(String[] args) {
		RobotManager manager = new RobotManager();
		//manager.addNXT("Poppy", "001653089A83");
		manager.addNXT("LilBish", "00165317B895");
		manager.connect();

		Thread m = new Thread(manager);
		m.start();

		Queue<Byte> q = new LinkedBlockingQueue<Byte>();
		Queue<Byte> l = new LinkedBlockingQueue<Byte>();

		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_SOUTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);

		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_SOUTH);
		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_NORTH);

		//manager.setMovementQueue("NXTL", q);
		manager.setMovementQueue("LilBish", l);
	}


	List<NXTInfo> NXTS = new ArrayList<NXTInfo>();
	List<Robot> connections = new ArrayList<Robot>();
	
	/**
	 * connects to nxts. Should be called before starting the thread.
	 */
	private void connect() {
		for (Robot connection : connections) {
			NXTComm nxtComm;
			try {
				nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				connection.connect(nxtComm);
			} catch (NXTCommException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/** 
	 * Adds an nxt to the manager.
	 * @param name Name of the nxt
	 * @param address address of nxt
	 * @return A robot object representing the robot.
	 */
	public Robot addNXT(String name, String address) {
		NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, name, address);
		NXTS.add(nxt);
		Robot r = new Robot(nxt, this) ;
		connections.add(r);
		return(r);
	}

	/**
	 * sets a robot to do a particular task.
	 * @param robotName
	 * @param messages
	 */
	public void setMovementQueue(String robotName, Queue<Byte> messages) {
		for (Robot r : connections) {
			if (r.getName().equals(robotName)) {
				r.setMoveQueue(messages);
			}
		}
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
			if (!c.requestingMove())
				return false;
		}
		return true;
	}

	@Override
	public void run() {
		while (true) {
			if (this.checkReady()) {
				System.out.println("All ready for instructions!");
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
}
