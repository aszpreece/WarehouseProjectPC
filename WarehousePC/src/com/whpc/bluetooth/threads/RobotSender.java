package com.whpc.bluetooth.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.whpc.bluetooth.Robot;
import com.whshared.network.NetworkMessage;

public class RobotSender implements Runnable {

	private DataOutputStream output;
	private Robot robot;
	Queue<Byte> messageQueue = new LinkedBlockingQueue<Byte>();

	public RobotSender(Robot robot, DataOutputStream output) {
		this.robot = robot;
		this.output = output;
	}

	public void setMoveMentQueue(Queue<Byte> m) {
		messageQueue = m;
	}

	@Override
	public void run() {
		while (true) {
			System.out.println(robot.requestingMove());
			if (robot.requestingMove()) {
				try {
					System.out.println("Sending");
					robot.setRequestingMove(false);
					Byte message = NetworkMessage.MOVE_NORTH;
					output.writeByte(message);
					if (message == NetworkMessage.MOVE_EAST || message == NetworkMessage.MOVE_WEST
							|| message == NetworkMessage.MOVE_NORTH || message == NetworkMessage.MOVE_SOUTH) {
						robot.setMoving(true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
