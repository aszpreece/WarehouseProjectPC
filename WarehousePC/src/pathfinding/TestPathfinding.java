package pathfinding;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.whshared.network.NetworkMessage;
import types.Node;

public class TestPathfinding {

//	@Test
//	public void testNode() {
//		try {
//			Node node = new Node(2, 4);
//		      
//			assertEquals(2, node.getX());
//			assertEquals(4, node.getY());
//			
//			node.set(6, 9);
//			assertEquals(6, node.getX());
//			assertEquals(9, node.getY());
//			
//		} catch (Exception e) {
//		      fail(e.getMessage());
//		}
//	}
//	
//	@Test
//	public void testPathfindingEastandWest() {
//			ShortestPathFinder shortest = new ShortestPathFinder();
//			ArrayList<Byte> path = new ArrayList<Byte>();
//			ArrayList<Byte> expectedPath = new ArrayList<Byte>();
//		
//			//basic east test
//			path = shortest.pathfind(5, 0, 0, 0);
//			for (int i = 0; i < 5; i++) {
//				expectedPath.add(NetworkMessage.MOVE_EAST);
//			}
//			assertEquals(expectedPath, path);
//			
//			//east around obstacle
//			path = shortest.pathfind(5, 1, 0, 1);
//			expectedPath.add(0, NetworkMessage.MOVE_SOUTH);
//			expectedPath.add(NetworkMessage.MOVE_NORTH);
//			assertEquals(expectedPath, path);
//			
//			expectedPath.clear();
//			
//			//basic west test
//			path = shortest.pathfind(0, 0, 5, 0);
//			for (int i = 0; i < 5; i++) {
//				expectedPath.add(NetworkMessage.MOVE_WEST);
//			}
//			assertEquals(expectedPath, path);
//			
//			//west around obstacle
//			path = shortest.pathfind(0, 1, 5, 1);
//			expectedPath.add(0, NetworkMessage.MOVE_SOUTH);
//			expectedPath.add(NetworkMessage.MOVE_NORTH);
//			assertEquals(expectedPath, path);
//	}
//	
//	@Test
//	public void testPathfindingNorthandSouth() {
//			ShortestPathFinder shortest = new ShortestPathFinder();
//			ArrayList<Byte> path = new ArrayList<Byte>();
//			ArrayList<Byte> expectedPath = new ArrayList<Byte>();
//			
//			//basic north test
//			path = shortest.pathfind(0, 0, 0, 6);
//			for (int i = 0; i < 6; i++) {
//				expectedPath.add(NetworkMessage.MOVE_NORTH);
//			}
//			assertEquals(expectedPath, path);		
//			
//			//north around obstacle
//			path = shortest.pathfind(1, 0, 1, 6);
//			expectedPath.add(0, NetworkMessage.MOVE_WEST);
//			expectedPath.add(NetworkMessage.MOVE_EAST);
//			assertEquals(expectedPath, path);
//			
//			expectedPath.clear();
//			
//			//basic south test
//			path = shortest.pathfind(0, 6, 0, 0);
//			for (int i = 0; i < 6; i++) {
//				expectedPath.add(NetworkMessage.MOVE_SOUTH);
//			}
//			assertEquals(expectedPath, path);
//			
//			//south around obstacle
//			path = shortest.pathfind(1, 6, 1, 0);
//			expectedPath.add(0, NetworkMessage.MOVE_WEST);
//			expectedPath.add(NetworkMessage.MOVE_EAST);
//			assertEquals(expectedPath, path);
//	}
//	
	@Test
	public void checkAsWeGo() {
		ShortestPathFinder shortest = new ShortestPathFinder();
		ArrayList<Byte> path = new ArrayList<Byte>();
		ArrayList<Byte> expectedPath = new ArrayList<Byte>();
		
		path = shortest.pathfind(6, 5, 8, 3);
		expectedPath.add(NetworkMessage.MOVE_NORTH); 
		expectedPath.add(NetworkMessage.MOVE_WEST);
		expectedPath.add(NetworkMessage.MOVE_WEST);
		expectedPath.add(NetworkMessage.MOVE_SOUTH);
		expectedPath.add(NetworkMessage.MOVE_SOUTH);
		expectedPath.add(NetworkMessage.MOVE_SOUTH);
		System.out.println(shortest.toString());
		assertEquals(expectedPath, path);
	}
}
