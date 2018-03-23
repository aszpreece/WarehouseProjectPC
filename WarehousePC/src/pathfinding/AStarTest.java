package pathfinding;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import types.Node;

public class AStarTest {

	//easy route. If it crashes then there is a problem
	@Test
	public void easy() {
		AStar pathf = new AStar();
		Node start = new Node(0,0);
		Node end = new Node(0, 4);
		List<Byte> path = pathf.pathfind(start, end);
		for(Byte b : path) {
			System.out.println(b);
		}

	}
	
	//harder route. If it crashes there is  a problem.
	@Test
	public void med() {
		AStar pathf = new AStar();
		Node start = new Node(0,0);
		Node end = new Node(4, 0);
		List<Byte> path = pathf.pathfind(start, end);
		for(Byte b : path) {
			System.out.println(b);
		}

	}

	//harder route that makes it turn a corner.
	@Test
	public void hard() {
		AStar pathf = new AStar();
		Node start = new Node(0,0);
		Node end = new Node(2, 1);
		List<Byte> path = pathf.pathfind(start, end);
		for(Byte b : path) {
			System.out.println(b);
		}

	}
	
	//makes it path to the opposite side of the map.
	@Test
	public void darkSouls() {
		AStar pathf = new AStar();
		Node start = new Node(0,0);
		Node end = new Node(9, 5);
		List<Byte> path = pathf.pathfind(start, end);
		for(Byte b : path) {
			System.out.println(b);
		}

	}


}
