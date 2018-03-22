package bluetooth.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import bluetooth.Robot;
import com.whshared.network.NetworkMessage;

public class RobotSender extends Thread {

	private DataOutputStream output;
	private Robot robot;
	private volatile boolean stop = false;
	Queue<Byte> messageQueue = new ConcurrentLinkedQueue<Byte>();

	public RobotSender(Robot robot, DataOutputStream output) {
		this.robot = robot;
		this.output = output;
	}

	public void setMovement(List<Byte> queue) {
		messageQueue.clear();
		messageQueue.addAll(queue);
	}
	
	public boolean hasCommands() {
		return !messageQueue.isEmpty();
	}

	@Override
	public void run() {
		while (!stop && !Thread.currentThread().isInterrupted()) {
			if (robot.getCanMakeMove() && hasCommands()) {
				try {
					robot.setRequestingMove(false);
					robot.setMakeNextMove(false);
					Byte message = messageQueue.poll();
					if (message == null) {
						message = NetworkMessage.NO_MOVE;
					}
					output.writeByte(message);
					switch(message) {
	    			case NetworkMessage.MOVE_NORTH:
	    				robot.setCurrentY(robot.getCurrentY() + 1);
	    				robot.setParked(false);
	    				System.out.println("Sending " + robot.getName() + " north");
	    				break;
	    			case NetworkMessage.MOVE_EAST:
	    				robot.setCurrentX(robot.getCurrentX() + 1);
	    				robot.setParked(false);
	    				System.out.println("Sending " + robot.getName() + " east");
	    				break;
	    			case NetworkMessage.MOVE_WEST:
	    				robot.setCurrentX(robot.getCurrentX() - 1);
	    				robot.setParked(false);
	    				System.out.println("Sending " + robot.getName() + " west");
	    				break;
	    			case NetworkMessage.MOVE_SOUTH:
	    				robot.setCurrentY(robot.getCurrentY() - 1);
	    				robot.setParked(false);
	    				System.out.println("Sending " + robot.getName() + " south");
	    				break;
	    			case NetworkMessage.NO_MOVE :
	    			case NetworkMessage.AWAIT_DROPOFF :
	    			case NetworkMessage.AWAIT_PICKUP :
	    				break;
	    			}
					System.out.println("Coords of " + robot.getName() + ": " + robot.getX() + " " + robot.getY());
					if (!messageQueue.isEmpty() && (messageQueue.peek() == NetworkMessage.AWAIT_PICKUP || messageQueue.peek() == NetworkMessage.AWAIT_DROPOFF)) {
	    				System.out.println("Sending pickup/dropoff " + robot.getName());
						output.writeByte(messageQueue.poll());
						robot.setParked(true);
					}
					output.flush();
				} catch (IOException e) {
					if (!stop) {
						e.printStackTrace();
						robot.disconnect();
					}
					break;
				}
			}
		}

	}

	public void halt() {
		stop = true;
	}

	public void clearInstructions() {
		messageQueue.clear();;	
	}


}
