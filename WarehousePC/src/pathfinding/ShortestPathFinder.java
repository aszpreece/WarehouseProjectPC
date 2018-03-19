package pathfinding;

import java.util.ArrayList;
import java.util.Arrays;

import com.whshared.network.NetworkMessage;

import rp.util.Collections;
import types.Node;

public class ShortestPathFinder {

	final static int[][] graph = { 
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

	final static int NODE_TRIED = 2;// what to set the map pos. to if a node has been tried and is not on the path
	final static int PATH_NODE = 3;// what to set the map pos. to if a node has been tried and is on the path

	int[][] map;// the map that will have the path on it

	int MAX_Y;// the max height of the grid
	int MAX_X;// the max width of the grid

	Node startingPosition;
	Node goalPosition;
	Node currentNode;

	ArrayList<Byte> pathToFollow;// will contain the path the robot will follow
	ArrayList<Byte> firstPath;
	ArrayList<Byte> secondPath;

	public ShortestPathFinder() {
		startingPosition = new Node(0, 0);
		goalPosition = new Node(0, 0);
		currentNode = new Node(0, 0);
		pathToFollow = new ArrayList<Byte>();
		firstPath = new ArrayList<Byte>();
		secondPath = new ArrayList<Byte>();

		this.MAX_Y = graph.length;
		this.MAX_X = graph[0].length;

		map = new int[MAX_Y][MAX_X];
	}

	public ArrayList<Byte> pathfind(int starty, int startx, int goaly, int goalx) {

		firstPath.clear();
		secondPath.clear();

		// get path from start to goal
		reset();
		map[startx][starty] = PATH_NODE;
		startingPosition.set(startx, starty);
		goalPosition.set(goalx, goaly);
		pathFind(startingPosition.x, startingPosition.y);
		firstPath.addAll(pathToFollow);

		System.out.println("Found first path");
		System.out.println(firstPath.toString());
		System.out.println("\n");
		
		if (pathToFollow
				.size() > (Math.abs(goalPosition.x - currentNode.x) + Math.abs(goalPosition.y - currentNode.y))) {
			// get path from goal to start
			reset();
			map[goalx][goaly] = PATH_NODE;
			startingPosition.set(goalx, goaly);
			goalPosition.set(startx, starty);
			pathFind(startingPosition.x, startingPosition.y);
			secondPath = new ArrayList<Byte>();
			secondPath.addAll(pathToFollow);
			
			System.out.println("Found second path");
			System.out.println(secondPath.toString());
			System.out.println("\n");

			if (secondPath.size() < firstPath.size()) {
				Collections.reverse(secondPath);
				
				System.out.println("Reversing path");
				System.out.println("Size " + secondPath.size());
				
				for (int i = 0; i < secondPath.size(); i++) {// get a correct path					
					if (secondPath.get(i) == NetworkMessage.MOVE_NORTH) {
											
						System.out.println("North --> South");
						
						secondPath.remove(i);
						secondPath.add(i, NetworkMessage.MOVE_SOUTH);
					} else if (secondPath.get(i) == NetworkMessage.MOVE_EAST) {
						
						System.out.println("East --> West");
						
						secondPath.remove(i);
						secondPath.add(i, NetworkMessage.MOVE_WEST);
					} else if (secondPath.get(i) == NetworkMessage.MOVE_SOUTH) {
						
						System.out.println("South--> North");
						
						secondPath.remove(i);
						secondPath.add(i, NetworkMessage.MOVE_NORTH);
					} else if (secondPath.get(i) == NetworkMessage.MOVE_WEST) {
						
						System.out.println("West --> East");
						
						secondPath.remove(i);
						secondPath.add(i, NetworkMessage.MOVE_EAST);
					}
				}
				
				System.out.println("Second path is shorter");
				
				pathToFollow.clear();
				pathToFollow.addAll(secondPath);
			} else {
				
				System.out.println("First path is shorter");
				
				pathToFollow.clear();
				pathToFollow.addAll(firstPath);
			}
		}
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
			if (goalPosition.x > currentNode.x) {// check if we want to go north
				map[currentNode.x + 1][currentNode.y] = PATH_NODE;// if so, go north
				pathToFollow.add(NetworkMessage.MOVE_NORTH);
				currentNode.set(currentNode.x + 1, currentNode.y);
			} else if (goalPosition.x < currentNode.x) {// we want to go south
				map[currentNode.x - 1][currentNode.y] = PATH_NODE;// go south
				pathToFollow.add(NetworkMessage.MOVE_SOUTH);
				currentNode.set(currentNode.x - 1, currentNode.y);
			} else {// on the correct north / south
				if (currentNode.y > goalPosition.y) {// if we want to go east
					if (currentNode.x >= 4) {// check if we want to go north
						while (map[currentNode.x][currentNode.y - 1] == 0) {// until we pass the barrier
							map[currentNode.x + 1][currentNode.y] = PATH_NODE;// go north
							pathToFollow.add(NetworkMessage.MOVE_NORTH);
							currentNode.set(currentNode.x + 1, currentNode.y);
						}
					} else {// we to go south
						while (map[currentNode.x][currentNode.y - 1] == 0) {// until we pass the barrier
							map[currentNode.x - 1][currentNode.y] = PATH_NODE;// go north
							pathToFollow.add(NetworkMessage.MOVE_SOUTH);
							currentNode.set(currentNode.x - 1, currentNode.y);
						}
					}
				} else {// we want to go west
					if (currentNode.x >= 4) {// check if we want to go north
						while (map[currentNode.x][currentNode.y + 1] == 0) {// until we pass the barrier
							map[currentNode.x + 1][currentNode.y] = PATH_NODE;// go north
							pathToFollow.add(NetworkMessage.MOVE_NORTH);
							currentNode.set(currentNode.x + 1, currentNode.y);
						}
					} else {// we to go south
						while (map[currentNode.x][currentNode.y + 1] == 0) {// until we pass the barrier
							map[currentNode.x - 1][currentNode.y] = PATH_NODE;// go north
							pathToFollow.add(NetworkMessage.MOVE_SOUTH);
							currentNode.set(currentNode.x - 1, currentNode.y);
						}
					}
				}
			}
		} else {// we need to go north or south but can't
			if (currentNode.x < goalPosition.x) {// if we want to go north
				if (currentNode.y != 0 && map[currentNode.x][currentNode.y - 1] == PATH_NODE) {// check if we came the
																								// east
					map[currentNode.x][currentNode.y - 1] = 1;
					map[currentNode.x + 1][currentNode.y - 1] = PATH_NODE;
					pathToFollow.remove(pathToFollow.size() - 1);
					pathToFollow.add(NetworkMessage.MOVE_NORTH);
					currentNode.set(currentNode.x + 1, currentNode.y - 1);
				} else if (currentNode.y != 1 && map[currentNode.x][currentNode.y + 1] == PATH_NODE) {// check if we
																										// came from the
																										// west
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
				if (currentNode.y != 11 && map[currentNode.x][currentNode.y - 1] == PATH_NODE) {// check if we came the
																								// east
					map[currentNode.x][currentNode.y - 1] = 1;
					map[currentNode.x - 1][currentNode.y - 1] = PATH_NODE;
					pathToFollow.remove(pathToFollow.size() - 1);
					pathToFollow.add(NetworkMessage.MOVE_SOUTH);
					currentNode.set(currentNode.x - 1, currentNode.y - 1);
				} else if (currentNode.y != 1 && map[currentNode.x][currentNode.y + 1] == PATH_NODE) {// check if we
																										// came from the
																										// west
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

	// resets the map and the path
	void reset() {
		pathToFollow.clear();
		for (int x = 0; x < MAX_Y; x++) {
			for (int y = 0; y < MAX_X; y++) {
				map[x][y] = graph[x][y];
			}
		}
	}
}