package main;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.whshared.network.NetworkMessage;

import bluetooth.RobotManager;
import bluetooth.threads.Message;

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

		BlockingQueue<Message> q = new LinkedBlockingQueue<Message>();
		BlockingQueue<Message> l = new LinkedBlockingQueue<Message>();

		q.add(new Message(NetworkMessage.MOVE_NORTH));
		q.add(new Message(NetworkMessage.MOVE_NORTH));
		q.add(new Message(NetworkMessage.MOVE_SOUTH));
		q.add(new Message(NetworkMessage.MOVE_NORTH));
		q.add(new Message(NetworkMessage.MOVE_NORTH));
		q.add(new Message(NetworkMessage.MOVE_NORTH));

		l.add(new Message(NetworkMessage.MOVE_NORTH));
		l.add(new Message(NetworkMessage.MOVE_NORTH));
		l.add(new Message(NetworkMessage.MOVE_SOUTH));
		l.add(new Message(NetworkMessage.MOVE_NORTH));
		l.add(new Message(NetworkMessage.MOVE_NORTH));
		l.add(new Message(NetworkMessage.MOVE_NORTH));

		manager.setMovementQueue("LilBish", q);
		


		/*Path path = pathfind.pathFind();
		Queue<Byte> directions = pathfind.pathToDirections(path);
		
		System.out.println(directions.toString());
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.setMovementQueue("LilBish", directions);
		*/
	}
}
