package com.whpc.main;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.whpc.bluetooth.RobotManager;
import com.whpc.pathfinding.LinePathfinding;
import com.whshared.network.NetworkMessage;

import lejos.robotics.pathfinding.Path;

public class MainTest {

	public static void main(String[] args) {
		LinePathfinding pathfind = new LinePathfinding();
		
		RobotManager manager = new RobotManager();		
		//manager.addNXT("Poppy", "001653089A83");
		manager.addNXT("LilBish", "00165317B895");
		manager.connect();

		Thread m = new Thread(manager);
		m.start();

		Queue<Byte> q = new LinkedBlockingQueue<Byte>();
		Queue<Byte> l = new LinkedBlockingQueue<Byte>();

		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_SOUTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);
		q.add(NetworkMessage.MOVE_NORTH);

		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_SOUTH);
		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_NORTH);
		l.add(NetworkMessage.MOVE_NORTH);

		//manager.setMovementQueue("NXTL", q);
		


		Path path = pathfind.pathFind();
		Queue<Byte> directions = pathfind.pathToDirections(path);
		
		System.out.println(directions.toString());
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.setMovementQueue("LilBish", directions);
	}
}
