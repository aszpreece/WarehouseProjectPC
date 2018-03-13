package com.whpc.pathfinding;

import java.util.ArrayList;

public class PathfindOnGraph {
	
	char[][] graph = {
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '0', '0', '0', '0', '0', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '0', '0', '0', '0', '0', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '0', '0', '0', '0', '0', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
            {'1', '0', '0', '0', '0', '0', '1', '1'},
            {'1', '1', '1', '1', '1', '1', '1', '1'},
        };
	
	Node startPosition;
	Node goalPosition;
	ArrayList<Node> path;
	
	PathfindOnGraph(){
		startPosition = new Node (0,0);
		goalPosition = new Node (0,0);
		path = new ArrayList<Node>();
	}
	
	public static void main(String[] args) {
		
	}
	
	public void setStartPosition(int x, int y) {
		startPosition.x = x;
		startPosition.y = y;
		graph[x-1][y-1] = 'S';
	}
	
	public void setGoalPosition(int x, int y) {
		startPosition.x = x;
		startPosition.y = y;
		graph[x-1][y-1] = 'X';
	}
	


}
