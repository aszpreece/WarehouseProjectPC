package com.whpc.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;

import com.whshared.network.NetworkMessage;

public class Djikstras {

	final static int NODE_TRIED = 2;// what to set the map pos. to if a node has been tried and is not on the path
	final static int PATH_NODE = 3;// what to set the map pos. to if a node has been tried and is on the path

	int[][] grid;// the grid to be checked against
	int[][] map;// the map that will have the path on it

	int MAX_Y;// the max height of the grid
	int MAX_X;// the max width of the grid

	Node startingPosition;
	Node goalPosition;

	ArrayList<Byte> pathToFollow;// will contain the path the robot will follow

	public Djikstras(int[][] grid) {
		startingPosition = new Node(0, 0);
		goalPosition = new Node(0, 0);
		pathToFollow = new ArrayList<Byte>();

		this.grid = grid;
		this.MAX_Y = grid.length;
		this.MAX_X = grid[0].length;

		this.map = new int[MAX_Y][MAX_X];
	}

	public boolean pathfind(int startx, int starty, int goalx, int goaly) {
		startingPosition.set(startx, starty);
		goalPosition.set(goalx, goaly);
		
		if (starty < goaly) {
			if (startx < goalx) {
				return findAPathNE(startingPosition.x, startingPosition.y);
			} else {
				return findAPathNW(startingPosition.x, startingPosition.y);
			}
		} else if (starty < goaly){
			if (startx < goalx) {
				return findAPathSE(startingPosition.x, startingPosition.y);
			} else {
				return findAPathSW(startingPosition.x, startingPosition.y);
			}
		}else {
			return false;
		}
	}

	private boolean findAPathNE(int height, int width) {// if the goal pos. is NorthEast of the start pos.
		if (!isValid(height, width)) {// check if the start position is valid
			return false;
		}

		if (isEnd(height, width)) {// check if you have reached the end
			map[height][width] = PATH_NODE;// set the goal node to be on the path
			pathToFollow.add(NetworkMessage.AWAIT_DROPOFF);// add the message to the list to await dropoff
			return true;// end the pathfinding
		} else {
			map[height][width] = NODE_TRIED;// this node isnt on the path
		}

		// North checks
		if (findAPathNE(height - 1, width)) {
			map[height - 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_NORTH);
			return true;
		}
		// East checks
		if (findAPathNE(height, width + 1)) {
			map[height][width + 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_EAST);
			return true;
		}
		// South checks
		if (findAPathNE(height + 1, width)) {
			map[height + 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_SOUTH);
			return true;
		}
		// West checks
		if (findAPathNE(height, width - 1)) {
			map[height][width - 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_WEST);
			return true;
		}
		return false;
	}

	private boolean findAPathNW(int height, int width) {// if the goal pos. is NorthWest of the start pos.
		if (!isValid(height, width)) {// check if the start position is valid
			return false;
		}

		if (isEnd(height, width)) {// check if you have reached the end
			map[height][width] = PATH_NODE;// set the goal node to be on the path
			pathToFollow.add(NetworkMessage.AWAIT_DROPOFF);// add the message to the list to await dropoff
			return true;// end the pathfinding
		} else {
			map[height][width] = NODE_TRIED;// this node isnt on the path
		}

		// North checks
		if (findAPathNW(height - 1, width)) {
			map[height - 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_NORTH);
			return true;
		}
		// West checks
		if (findAPathNW(height, width - 1)) {
			map[height][width - 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_WEST);
			return true;
		}
		// South checks
		if (findAPathNW(height + 1, width)) {
			map[height + 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_SOUTH);
			return true;
		}
		// East checks
		if (findAPathNW(height, width + 1)) {
			map[height][width + 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_EAST);
			return true;
		}
		return false;
	}

	private boolean findAPathSE(int height, int width) {// if the goal pos. is SouthEast of the start pos.
		if (!isValid(height, width)) {// check if the start position is valid
			return false;
		}

		if (isEnd(height, width)) {// check if you have reached the end
			map[height][width] = PATH_NODE;// set the goal node to be on the path
			pathToFollow.add(NetworkMessage.AWAIT_DROPOFF);// add the message to the list to await dropoff
			return true;// end the pathfinding
		} else {
			map[height][width] = NODE_TRIED;// this node isnt on the path
		}

		// South checks
		if (findAPathSE(height + 1, width)) {
			map[height + 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_SOUTH);
			return true;
		}
		// East checks
		if (findAPathSE(height, width + 1)) {
			map[height][width + 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_EAST);
			return true;
		}
		// North checks
		if (findAPathSE(height - 1, width)) {
			map[height - 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_NORTH);
			return true;
		}
		// West checks
		if (findAPathSE(height, width - 1)) {
			map[height][width - 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_WEST);
			return true;
		}
		return false;
	}

	private boolean findAPathSW(int height, int width) {// if the goal pos. is SouthWest of the start pos.
		if (!isValid(height, width)) {// check if the start position is valid
			return false;
		}

		if (isEnd(height, width)) {// check if you have reached the end
			map[height][width] = PATH_NODE;// set the goal node to be on the path
			pathToFollow.add(NetworkMessage.AWAIT_DROPOFF);// add the message to the list to await dropoff
			return true;// end the pathfinding
		} else {
			map[height][width] = NODE_TRIED;// this node isnt on the path
		}

		// South checks
		if (findAPathSW(height + 1, width)) {
			map[height + 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_SOUTH);
			return true;
		}
		// West checks
		if (findAPathSW(height, width - 1)) {
			map[height][width - 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_WEST);
			return true;
		}
		// North checks
		if (findAPathSW(height - 1, width)) {
			map[height - 1][width] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_NORTH);
			return true;
		}
		// East checks
		if (findAPathSW(height, width + 1)) {
			map[height][width + 1] = PATH_NODE;
			pathToFollow.add(NetworkMessage.MOVE_EAST);
			return true;
		}
		return false;
	}

	// helper function to check whether the current node is the end node
	private boolean isEnd(int height, int width) {
		return height == goalPosition.x && width == goalPosition.y;
	}

	// helper function to check whether the current node is valid
	private boolean isValid(int height, int width) {
		if (inRange(height, width) && canTravel(height, width) && !isTried(height, width)) {
			return true;
		}
		return false;
	}

	// helper function to see whether the node can be travelled to
	private boolean canTravel(int height, int width) {
		return grid[height][width] == 1;
	}

	// helper function to check if the node has already been checked
	private boolean isTried(int height, int width) {
		return map[height][width] == NODE_TRIED;
	}

	// helper function to check whether the node is in the graph
	private boolean inRange(int height, int width) {
		return inHeight(height) && inWidth(width);
	}

	// helper method to check whether the node is within the height constraint
	private boolean inHeight(int height) {
		return height >= 0 && height < MAX_Y;
	}

	// helper method to check whether the node is within the width constraint
	private boolean inWidth(int width) {
		return width >= 0 && width < MAX_X;
	}

	// visualises the map with the path to follow on
	public String toString() {
		String s = "";
		for (int[] row : map) {
			s += Arrays.toString(row) + "\n";
		}
		return s;
	}

	// returns the path that the robot ill have to follow
	ArrayList<Byte> getPathToFollow() {
		return pathToFollow;
	}
}
