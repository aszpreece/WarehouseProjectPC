package bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

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
	private volatile boolean connected = false;
	private RobotManager manager;
	private int x = 0, y = 0;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

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
			reciever.start();
			sender.start();
			connected = true;
		}
		return connected;
	}
	
	public void disconnect() {
		try {
			sender.halt();
			sender.interrupt();
			sender.join();
			reciever.halt();
			reciever.interrupt();
			reciever.join();
			input.close();
			output.close();
			connected = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		manager.removeRobot(this);
	}

	public boolean isConnected() {
		return connected;
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
	
	public void setMoveQueue(BlockingQueue<Byte> queue) {
		sender.setMoveMentQueue(queue);
	}

	public void setMakeNextMove(boolean v) {
		canMakeMove = v;	
	}
	
	public boolean getCanMakeMove() {
		return canMakeMove;
	}

}