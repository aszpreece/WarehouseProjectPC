package bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import bluetooth.threads.RobotReciever;
import bluetooth.threads.RobotSender;
import jobmanagement.Server;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;
import types.Task;

/**
 * Robot skeleton for other people to work with
 * @author b1999
 *
 */
public class Robot {
	
	public static final int NORTH = 0;
	public static final int EAST = 90;
	public static final int SOUTH = 180;
	public static final int WEST = 270;
	
	private DataInputStream input;
	private DataOutputStream output;
	private final NXTInfo m_nxt;
	private String name;
	private volatile boolean canMakeMove = false;
	
	private RobotReciever reciever;
	private RobotSender sender;
	private volatile boolean requestingMove = false;
	private volatile boolean connected = false;
	private Server manager;
	private int x = 0, y = 0;
	private Task task;
	
	private Integer currentX;
	private Integer currentY;
	
	private Float currentWeight;
	private Float maxWeight = 50f; // Maybe finalis
	
	/**
	 * Current direction is degrees.
	 */
	private Integer currentDirection;
	
	private String robotName;
	
	/**
	 * Set to a value NORTH (0), EAST (90), SOUTH (180), WEST (270)
	 * @param direction The direction to set
	 */
	public void setDirectionCurrent(Integer direction) {
		currentDirection = direction;
	}

	public Integer getCurrentX() {
		return currentX;
	}

	public void setCurrentX(Integer currentX) {
		this.currentX = currentX;
	}

	public Integer getCurrentY() {
		return currentY;
	}

	public void setCurrentY(Integer currentY) {
		this.currentY = currentY;
	}

	public Float getCurrentWeight() {
		return currentWeight;
	}

	public void setCurrentWeight(Float currentWeight) {
		this.currentWeight = currentWeight;
	}

	public Float getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Float maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Robot(NXTInfo nxt, Server jobManagerServer) {
		m_nxt = nxt;
		name = nxt.name;
		// Why is the manager imported?
		manager = jobManagerServer;
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
	
	public boolean requestingMove() {
		return requestingMove;
	}
	
	public synchronized void setRequestingMove(boolean v) {
		requestingMove = v;
	}
	
	public String getName() {
		return name;
	}
	
	public void setMakeNextMove(boolean v) {
		canMakeMove = v;	
	}
	
	public boolean getCanMakeMove() {
		return canMakeMove;
	}

	public void cancelJob() {
		// TODO Auto-generated method stub
	}

	public boolean hasInstructions() {
		return sender.hasCommands();
	}

}
