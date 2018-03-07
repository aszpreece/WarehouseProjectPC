package com.whpc.bluetooth.threads;

import java.io.DataInputStream;
import java.io.IOException;

import com.whpc.bluetooth.Robot;
import com.whshared.network.NetworkMessage;

public class RobotReciever implements Runnable {

	private DataInputStream input;
	private Robot robot;
	
	public RobotReciever(Robot robot, DataInputStream input) {
		this.input = input;
		this.robot = robot;
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				if (input.readByte() == NetworkMessage.REQUEST_MOVE) {
					System.out.println(robot.getName() + " is requesting a move.");
					robot.setRequestingMove(true);
					System.out.println(robot.requestingMove());
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
