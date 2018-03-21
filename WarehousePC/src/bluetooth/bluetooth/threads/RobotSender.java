package bluetooth.threads;

import java.io.DataOutputStream;
import java.io.IOException;
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

	public void setMovementQueue(Queue<Byte> queue) {
		messageQueue.clear();
		messageQueue.addAll(queue);
	}
	
	public boolean hasCommands() {
		return !messageQueue.isEmpty();
	}

	@Override
	public void run() {
		while (!stop && !Thread.currentThread().isInterrupted()) {

			if (robot.getCanMakeMove()) {

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
	    			case NetworkMessage.MOVE_EAST:
	    				robot.setCurrentX(robot.getCurrentX() + 1);
	    			case NetworkMessage.MOVE_WEST:
	    				robot.setCurrentX(robot.getCurrentX() - 1);
	    			case NetworkMessage.MOVE_SOUTH:
	    				robot.setCurrentY(robot.getCurrentY() - 1);
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
