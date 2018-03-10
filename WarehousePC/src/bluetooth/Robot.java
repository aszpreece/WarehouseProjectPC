package bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;

import bluetooth.threads.*;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

public class Robot {

	private DataInputStream input;
	private DataOutputStream output;
	private final NXTInfo m_nxt;
	private String name;
	private volatile boolean canMakeMove = false;
	private volatile boolean isMoving = false;
	
	private RobotReciever reciever;
	private RobotSender sender;
	private volatile boolean requestingMove = false;
	private RobotManager manager;

	public Robot(NXTInfo nxt, RobotManager m) {
		m_nxt = nxt;
		name = nxt.name;
		// Why is the manager imported?
		manager = m;
	}

	public boolean connect(NXTComm comm) throws NXTCommException {
		if (comm.open(m_nxt)) {
			input = new DataInputStream(comm.getInputStream());
			output = new DataOutputStream(comm.getOutputStream());
			reciever = new RobotReciever(this, input);
			sender = new RobotSender(this, output);
			new Thread(reciever).start();
			new Thread(sender).start();
			System.out.println("sr threads created");
		}
		return isConnected();
	}

	public boolean isConnected() {
		return output != null;
	}
	
	/**
	 * @return If the robot is ready to move. I.e, it is stationary at a junction or drop off point.
	 */
	public boolean isMoving() {
		return isMoving;
	}
	
	public void setMoving(boolean v) {
		isMoving = v;
	}
	
	public boolean requestingMove() {
		return requestingMove;
	}
	
	public synchronized void setRequestingMove(boolean v) {
		requestingMove = v;
	}
	
	public String getName() {
		return name;
	}
	
	public void setMoveQueue(Queue<Byte> queue) {
		sender.setMoveMentQueue(queue);
	}


	public void setMakeNextMove(boolean v) {
		canMakeMove = v;	
	}
	
	public boolean getCanMakeMove() {
		return canMakeMove;
	}

}