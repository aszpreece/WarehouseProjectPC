package bluetooth.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import bluetooth.Robot;
import com.whshared.network.NetworkMessage;

public class RobotSender implements Runnable {

	private DataOutputStream output;
	private Robot robot;
	BlockingQueue<Byte> messageQueue = new LinkedBlockingQueue<Byte>();

	public RobotSender(Robot robot, DataOutputStream output) {
		this.robot = robot;
		this.output = output;
	}

	public void setMoveMentQueue(BlockingQueue<Byte> m) {
		messageQueue = m;
	}

	@Override
	public void run() {
		while (true) {
			//System.out.println(robot.getCanMakeMove());
			if (robot.getCanMakeMove()) {
				/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				try {
					//System.out.println("Sending to " + robot.getName());
					robot.setRequestingMove(false);
					robot.setMakeNextMove(false);
					Byte message = messageQueue.take();
					if (message == null) {
						message = NetworkMessage.NO_MOVE;
						//System.out.println("Out of instructions");
					}
					output.writeByte(message);
					if (message == NetworkMessage.MOVE_EAST || message == NetworkMessage.MOVE_WEST
							|| message == NetworkMessage.MOVE_NORTH || message == NetworkMessage.MOVE_SOUTH) {
						robot.setMoving(true);
					}
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
		}

	}

}
