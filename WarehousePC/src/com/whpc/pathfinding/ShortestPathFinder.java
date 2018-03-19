package com.whpc.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;

import com.whshared.network.NetworkMessage;

public class ShortestPathFinder {
	
	final static int[][] graph = { 
    		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

	final static int NODE_TRIED = 2;// what to set the map pos. to if a node has been tried and is not on the path
	final static int PATH_NODE = 3;// what to set the map pos. to if a node has been tried and is on the path

	int[][] map;// the map that will have the path on it

	int MAX_Y;// the max height of the grid
	int MAX_X;// the max width of the grid

	Node startingPosition;
	Node goalPosition;
	Node currentNode;

	ArrayList<Byte> pathToFollow;// will contain the path the robot will follow

	public ShortestPathFinder() {
		startingPosition = new Node(0, 0);
		goalPosition = new Node(0, 0);
		currentNode = new Node(0, 0);
		pathToFollow = new ArrayList<Byte>();

		this.MAX_Y = graph.length;
		this.MAX_X = graph[0].length;
		
		map = new int[MAX_Y][MAX_X];
	}

	public ArrayList<Byte> pathfind(int starty, int startx, int goaly, int goalx) {
		
		resetMap();
		pathToFollow.clear();
		
		map[startx][starty] = PATH_NODE;
		startingPosition.set(startx, starty);
		goalPosition.set(goalx, goaly);
		pathFind(startingPosition.x, startingPosition.y);
		return pathToFollow;
	}

	// helper function to check whether the current node is the end node
	private boolean isEnd(int height, int width) {
		return height == goalPosition.x && width == goalPosition.y;
	}

	// visualises the map with the path to follow on
	public String toString() {
		String s = "";
		for (int[] row : map) {
			s += Arrays.toString(row) + "\n";
		}
		return s;
	}

	public void pathFind(int startX, int startY) {
		currentNode.set(startX, startY);
		
		if (isEnd(startX, startY)) {// initial check to see whether the start is equal to the goal
			return;
		}

		if (goalPosition.y > currentNode.y) {// if we need to go west
			if (map[currentNode.x][currentNode.y + 1] != 0) {// if we can go west
				map[currentNode.x][currentNode.y + 1] = PATH_NODE;
				pathToFollow.add(NetworkMessage.MOVE_WEST);
				if (!isEnd(currentNode.x, currentNode.y + 1)) {// check node isn't the end
					pathFind(currentNode.x, currentNode.y + 1);
				}
			} else {// obstacle in the way
				goAroundObstacle(currentNode.x, currentNode.y, true);
				pathFind(currentNode.x, currentNode.y);
			}
		} else if (goalPosition.y < currentNode.y) {// if we need to go east
			if (map[currentNode.x][currentNode.y - 1] != 0) {// if we can go east
				map[currentNode.x][currentNode.y - 1] = PATH_NODE;
				pathToFollow.add(NetworkMessage.MOVE_EAST);
				if (!isEnd(currentNode.x, currentNode.y - 1)) {// check node isn't the end
					pathFind(currentNode.x, currentNode.y - 1);
				}
			} else {// obstacle in the way
				goAroundObstacle(currentNode.x, currentNode.y, true);
				pathFind(currentNode.x, currentNode.y);
			}
		} else if (goalPosition.x > currentNode.x) {// we need to go north
			if (map[currentNode.x + 1][currentNode.y] != 0) {// if we can go north
				map[currentNode.x + 1][currentNode.y] = PATH_NODE;
				pathToFollow.add(NetworkMessage.MOVE_NORTH);
				if (!isEnd(currentNode.x + 1, currentNode.y)) {// check node isn't the end
					pathFind(currentNode.x + 1, currentNode.y);
				}
			} else {// obstacle in the way
				goAroundObstacle(currentNode.x, currentNode.y, false);
				pathFind(currentNode.x, currentNode.y);
			}
		} else {// we need to go south
			if (map[currentNode.x - 1][currentNode.y] != 0) {// if we can go north
				map[currentNode.x - 1][currentNode.y] = PATH_NODE;
				pathToFollow.add(NetworkMessage.MOVE_SOUTH);
				if (!isEnd(currentNode.x - 1, currentNode.y)) {// check node isn't the end
					pathFind(currentNode.x - 1, currentNode.y);
				}
			} else {// obstacle in the way
				goAroundObstacle(currentNode.x, currentNode.y, false);
				pathFind(currentNode.x, currentNode.y);
			}
		}
		return;
	}

	public void goAroundObstacle(int startX, int startY, Boolean toSide) {
		currentNode.set(startX, startY);
		
		if (toSide) {// we need to go west or east but can't
			if (goalPosition.x > currentNode.x ) {// check if we want to go north
				map[currentNode.x + 1][currentNode.y] = PATH_NODE;// if so, go north
				pathToFollow.add(NetworkMessage.MOVE_NORTH);
				currentNode.set(currentNode.x + 1, currentNode.y);
			} else if(goalPosition.x < currentNode.x){// we want to go south
				map[currentNode.x - 1][currentNode.y] = PATH_NODE;// go south
				pathToFollow.add(NetworkMessage.MOVE_SOUTH);
				currentNode.set(currentNode.x - 1, currentNode.y);
			}else if(currentNode.x >= 4){
				map[currentNode.x + 1][currentNode.y] = PATH_NODE;// if so, go north
				pathToFollow.add(NetworkMessage.MOVE_NORTH);
				currentNode.set(currentNode.x + 1, currentNode.y);
			}else {
				map[currentNode.x - 1][currentNode.y] = PATH_NODE;// go south
				pathToFollow.add(NetworkMessage.MOVE_SOUTH);
				currentNode.set(currentNode.x - 1, currentNode.y);
			}
		} else {// we need to go north or south but can't
			if (currentNode.x < goalPosition.x) {// if we want to go north
				if (currentNode.y != 0 && map[currentNode.x][currentNode.y - 1] == PATH_NODE) {// check if we came the east
					map[currentNode.x][currentNode.y - 1] = 1;
					map[currentNode.x + 1][currentNode.y - 1] = PATH_NODE;
					pathToFollow.remove(pathToFollow.size() - 1);
					pathToFollow.add(NetworkMessage.MOVE_NORTH);
					currentNode.set(currentNode.x + 1, currentNode.y - 1);
				} else if (currentNode.y != 1 && map[currentNode.x][currentNode.y + 1] == PATH_NODE) {// check if we came from the west
					map[currentNode.x][currentNode.y + 1] = 1;
					map[currentNode.x + 1][currentNode.y + 1] = PATH_NODE;
					pathToFollow.remove(pathToFollow.size() - 1);
					pathToFollow.add(NetworkMessage.MOVE_NORTH);
					currentNode.set(currentNode.x + 1, currentNode.y + 1);
				} else {// we didn't come from either so just go to the west and north
					map[currentNode.x][currentNode.y + 1] = PATH_NODE;
					map[currentNode.x + 1][currentNode.y + 1] = PATH_NODE;
					pathToFollow.add(NetworkMessage.MOVE_WEST);
					pathToFollow.add(NetworkMessage.MOVE_NORTH);
					currentNode.set(currentNode.x + 1, currentNode.y + 1);
				}
			} else {// we want to go south
				if (currentNode.y != 11 && map[currentNode.x][currentNode.y - 1] == PATH_NODE) {// check if we came the east
					map[currentNode.x][currentNode.y - 1] = 1;
					map[currentNode.x - 1][currentNode.y - 1] = PATH_NODE;
					pathToFollow.remove(pathToFollow.size() - 1);
					pathToFollow.add(NetworkMessage.MOVE_SOUTH);
					currentNode.set(currentNode.x - 1, currentNode.y - 1);
				} else if (currentNode.y != 1 && map[currentNode.x][currentNode.y + 1] == PATH_NODE) {// check if we came from the west
					map[currentNode.x][currentNode.y + 1] = 1;
					map[currentNode.x - 1][currentNode.y + 1] = PATH_NODE;
					pathToFollow.remove(pathToFollow.size() - 1);
					pathToFollow.add(NetworkMessage.MOVE_SOUTH);
					currentNode.set(currentNode.x - 1, currentNode.y + 1);
				} else {// we didn't come from either so just go to the west and south
					map[currentNode.x][currentNode.y + 1] = PATH_NODE;
					map[currentNode.x - 1][currentNode.y + 1] = PATH_NODE;
					pathToFollow.add(NetworkMessage.MOVE_WEST);
					pathToFollow.add(NetworkMessage.MOVE_SOUTH);
					currentNode.set(currentNode.x - 1, currentNode.y + 1);
				}
			}
		}
	}

	// returns the path that the robot will follow
	ArrayList<Byte> getPathToFollow() {
		return pathToFollow;
	}
	
	// resets the map
	void resetMap() {
		for (int x = 0; x < MAX_Y; x++) {
			for (int y = 0; y < MAX_X; y++) {
				map[x][y] = graph[x][y];
			}
		}
	}
}