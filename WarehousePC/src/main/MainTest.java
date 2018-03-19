package main;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import bluetooth.RobotManager;
import com.whshared.network.NetworkMessage;

import lejos.robotics.pathfinding.Path;

public class MainTest {

	public static void main(String[] args) {
		//LinePathfinding pathfind = new LinePathfinding();
		
		RobotManager manager = new RobotManager();		
		//manager.addNXT("Poppy", "001653089A83");
		manager.addNXT("LilBish", "00165317B895");
		manager.connect();

		Thread m = new Thread(manager);
		m.start();

		BlockingQueue<Byte> q = new LinkedBlockingQueue<Byte>();
		BlockingQueue<Byte> l = new LinkedBlockingQueue<Byte>();

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

		manager.setMovementQueue("LilBish", q);
		
		System.out.println("Connected and stuff");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.halt();
		
		/*Path path = pathfind.pathFind();
		Queue<Byte> directions = pathfind.pathToDirections(path);
		
		System.out.println(directions.toString());
	
		manager.setMovementQueue("LilBish", directions);
		*/
	}
}
