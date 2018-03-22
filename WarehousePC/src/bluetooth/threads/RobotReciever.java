package bluetooth.threads;

import java.io.DataInputStream;
import java.io.IOException;

import bluetooth.Robot;
import com.whshared.network.NetworkMessage;

public class RobotReciever extends Thread {

	private DataInputStream input;
	private Robot robot;
	private volatile boolean stop = false;
	
	public RobotReciever(Robot robot, DataInputStream input) {
		this.input = input;
		this.robot = robot;
	}
	
	@Override
	public void run() {
		byte message;
		while(!Thread.currentThread().isInterrupted()) {
			try {
				message = input.readByte();
				System.out.println("Message recieved: " + message);
				if (message == NetworkMessage.REQUEST_MOVE && !robot.isParked()) {
					robot.setParked(false);
					robot.setRequestingMove(true);
				} else if (message == NetworkMessage.AWAIT_DROPOFF || message == NetworkMessage.AWAIT_PICKUP) {
					System.out.println("Robot dropoff has been comp");
					robot.setParked(false);
				} else if (message == NetworkMessage.CANCEL_JOB) {
					robot.cancelJob();
				}
			} catch (IOException e) {
				if (!stop) {
					e.printStackTrace();
					robot.disconnect();
				}
				break;
			}
		}
		
	}

	public void halt() {
		try {
			stop = true;
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
