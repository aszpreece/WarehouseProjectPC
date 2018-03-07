package com.whpc.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import com.whpc.bluetooth.threads.RobotReciever;
import com.whpc.bluetooth.threads.RobotSender;
import com.whshared.network.NetworkMessage;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Robot  {

	private DataInputStream input;
	private DataOutputStream output;
	private final NXTInfo m_nxt;
	private String name;
	private boolean canMakeMove = false;
	private boolean isMoving = false;
	
	private RobotReciever reciever;
	private RobotSender sender;
	private boolean requestingMove = false;

	public Robot(NXTInfo nxt) {
		m_nxt = nxt;
		name = nxt.name;
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
	
	public void setRequestingMove(boolean v) {
		requestingMove = v;
	}
	
	public String getName() {
		return name;
	}
	
	public void setMoveQueue(Queue<Byte> queue) {
		sender.setMoveMentQueue(queue);
	}

//	@Override
//	public void run() {
//		byte msg = 0;
//		try {
//			while (!Thread.currentThread().isInterrupted()) {
//				msg = input.readByte();
//				if (msg == NetworkMessage.REQUEST_MOVE) {
//					isMoving = true;
//					canMakeMove = false;
//					System.out.println(m_nxt.name + " is requesting a move");
//				}
//				output.flush();
//			}
//		} catch (IOException e) {
//			//only print stack trace if the thread was not interrupted
//			if (!Thread.currentThread().isInterrupted())
//				e.printStackTrace();
//		}
//
//	}
	
//	public void stop() {
//		try {
//			Thread.currentThread().interrupt();
//			input.close();
//			output.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void setMakeNextMove() {
		canMakeMove = true;	
	}
	
	public boolean getCanMakeMove() {
		return canMakeMove;
	}

}