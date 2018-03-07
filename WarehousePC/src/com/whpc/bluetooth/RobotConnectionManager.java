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
 * @author tap747
 * May be merged or removed later.
 */
public class RobotConnectionManager implements Runnable{
	
	public static void main(String[] args) {
		RobotConnectionManager manager = new RobotConnectionManager();
		manager.addNXT("Poppy", "001653089A83");
	
		Thread m = new Thread(manager);
		m.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.waitForConnect();
		Queue<Byte> q = new LinkedBlockingQueue<Byte>();
		q.add(NetworkMessage.MOVE_SOUTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_SOUTH);
		q.add(NetworkMessage.MOVE_SOUTH);
		manager.setMovementQueue("Poppy", q);
	}


	List<NXTInfo> NXTS = new ArrayList<NXTInfo>();
	List<Robot> connections = new ArrayList<Robot>();

	public void addNXT(String name, String address) {
		NXTInfo nxt = new NXTInfo(NXTCommFactory.BLUETOOTH, name, address);
		NXTS.add(nxt);
		connections.add(new Robot(nxt));
	}
	
	public void setMovementQueue(String robotName, Queue<Byte> messages) {
		for (Robot r : connections) {
			if (r.getName().equals(robotName)) {
				r.setMoveQueue(messages);
			}
		}
	}
	
	/**
	 * flags that all the robots are ready to move and the time step can advance.
	 */
	public void setReady() {
		for (Robot c : connections) {
			c.setMakeNextMove();
		}	
	}
	
	/**
	 * checks if all the robots are ready to move.
	 */
	public boolean checkReady() {
		for (Robot c : connections) {
			if (c.requestingMove()) 
				//System.out.println("Not ready");
				return false;
		}	
		return true;
	}

	@Override
	public void run() {
		for (Robot connection : connections) {
			NXTComm nxtComm;
			try {
				nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				connection.connect(nxtComm);
			} catch (NXTCommException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(true) {
				if (this.checkReady()) {
					this.setReady();
				}
			}
		}
	}
	
	public void waitForConnect() {
		boolean allConnected = false;
		while (!allConnected) {
			allConnected = true;
			for (Robot c : connections) {
				allConnected &= c.isConnected();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
