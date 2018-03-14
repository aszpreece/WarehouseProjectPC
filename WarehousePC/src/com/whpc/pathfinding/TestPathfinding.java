package com.whpc.pathfinding;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestPathfinding {

	@Test
	public void testNode() {
		try {
			Node node = new Node(2, 4);
		      
			assertEquals(2, node.getX());
			assertEquals(4, node.getY());
			
			node.set(6, 9);
			assertEquals(6, node.getX());
			assertEquals(9, node.getY());
			
		} catch (Exception e) {
		      fail(e.getMessage());
		}
	}
	
	@Test
	public void testDjikstras() {
		int[][] graph = { 
    		{1, 1},
    		{0, 1}
		};
		
		try {
			Djikstras djik = new Djikstras(graph);
			
			int startx = 1, starty = 1, goalx = 0, goaly = 0;
			
			boolean result = djik.pathfind(startx, starty, goalx, goaly);
			boolean expected = true;
			assertEquals(expected, result);
			
			String expectedToString = ""; //needs the expected tostring
			String resultToString = djik.toString();
			assertEquals(expectedToString, resultToString);
			
			Byte expectedPathToFollow = null;//needs the expected path
			ArrayList<Byte> resultGetPathToFollow = djik.getPathToFollow();
			assertEquals(expectedPathToFollow, resultGetPathToFollow);
		} catch (Exception e) {
		      fail(e.getMessage());
		}
	}
	
	@Test
	public void testPathfindOnGraph() {
		int[][] graph = { 
    		{1, 1, 1},
    		{1, 0, 1},
    		{1, 1, 1}
		};
		try {
			PathfindOnGraph paths = new PathfindOnGraph(graph);
		} catch (Exception e) {
		      fail(e.getMessage());
		}
	}
	
	@Test
	public void testPathfindOnGraphMain() {
		try {
			PathfindOnGraph.main(new String[] {});
		} catch (Exception e) {
		      fail(e.getMessage());
		}
	}
}
