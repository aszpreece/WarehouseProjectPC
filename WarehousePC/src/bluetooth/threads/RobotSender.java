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
					if (message == NetworkMessage.MOVE_EAST || message == NetworkMessage.MOVE_WEST
							|| message == NetworkMessage.MOVE_NORTH || message == NetworkMessage.MOVE_SOUTH) {
						robot.setMoving(true);
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


}
