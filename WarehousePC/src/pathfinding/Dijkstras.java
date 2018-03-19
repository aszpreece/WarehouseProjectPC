package pathfinding;

import java.util.ArrayList;
import java.util.Arrays;

import com.whshared.network.NetworkMessage;

import lejos.geom.Point;
import types.Node;

public class Dijkstras {

	final static int NODE_TRIED = 2;// what to set the map pos. to if a node has been tried and is not on the path
	final static int PATH_NODE = 3;// what to set the map pos. to if a node has been tried and is on the path

	int[][] grid;// the grid to be checked against
	int[][] map;// the map that will have the path on it

	int MAX_Y;// the max height of the grid
	int MAX_X;// the max width of the grid

	Node startingPosition;
	Node goalPosition;
	Node currentNode;

	ArrayList<Byte> pathToFollow;// will contain the path the robot will follow

	public Dijkstras(int[][] grid) {
		startingPosition = new Node(0, 0);
		goalPosition = new Node(0, 0);
		currentNode = new Node(0, 0);
		pathToFollow = new ArrayList<Byte>();

		this.grid = grid;
		this.MAX_Y = grid.length;
		this.MAX_X = grid[0].length;

		this.map = new int[MAX_Y][MAX_X];
		
	}

	public boolean pathfind(int startx, int starty, int goalx, int goaly) {
		startingPosition.set(startx, starty);
		goalPosition.set(goalx, goaly);

		Boolean pathFound = findAPath(startingPosition.x, startingPosition.y);
		findShortestPathToFollow(startx, starty);
		return pathFound;
	}

	private boolean findAPath(int height, int width) {// if the goal pos. is WESTNORTH of the start pos.
		if (!isValid(height, width)) {// check if the start position is valid
			return false;
		}

		if (isEnd(height, width)) {// check if you have reached the end
			map[height][width] = PATH_NODE;// set the goal node to be on the path
			return true;// end the pathfinding
		} else {
			map[height][width] = NODE_TRIED;// this node isnt on the path
		}

		// WEST checks
		if (findAPath(height - 1, width)) {
			map[height - 1][width] = PATH_NODE;
			return true;
		}
		// NORTH checks
		if (findAPath(height, width + 1)) {
			map[height][width + 1] = PATH_NODE;
			return true;
		}
		// EAST checks
		if (findAPath(height + 1, width)) {
			map[height + 1][width] = PATH_NODE;
			return true;
		}
		// SOUTH checks
		if (findAPath(height, width - 1)) {
			map[height][width - 1] = PATH_NODE;
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

	// finds the path that the robot will have to follow
	void findShortestPathToFollow(int x, int y) {
		currentNode.set(x, y);
		
		if (isEnd(x, y)) {// initial check to see whether the start is equal to the goal
			return;
		}

		if (goalPosition.y > currentNode.y) {// check if the goal is to the NORTH
			if (map[currentNode.x][currentNode.y + 1] == 3) {// check if the left is on the path
				pathToFollow.add(NetworkMessage.MOVE_NORTH);
				map[currentNode.x][currentNode.y] = 2;
				if (!isEnd(x, y)) {
					findShortestPathToFollow(currentNode.x, currentNode.y + 1);
				}
			} else if (goalPosition.x > currentNode.x) {// check if the goal is WEST
				if (map[currentNode.x + 1][currentNode.y] == 3) {
					pathToFollow.add(NetworkMessage.MOVE_WEST);
					map[currentNode.x][currentNode.y] = 2;
					if (!isEnd(x, y)) {
						findShortestPathToFollow(currentNode.x + 1, currentNode.y);
					}
				}
			} else {// must move EAST
				pathToFollow.add(NetworkMessage.MOVE_EAST);
				map[currentNode.x][currentNode.y] = 2;
				if (!isEnd(x, y)) {
					findShortestPathToFollow(currentNode.x - 1, currentNode.y);
				}
			}
		} else if (goalPosition.y < currentNode.y) {// check if the goal is to the SOUTH
			if (map[currentNode.x][currentNode.y - 1] == 3) {// check if the right is on the path
				pathToFollow.add(NetworkMessage.MOVE_SOUTH);
				map[currentNode.x][currentNode.y] = 2;
				if (!isEnd(x, y)) {
					findShortestPathToFollow(currentNode.x, currentNode.y - 1);
				}
			} else if (goalPosition.x > currentNode.x) {// check if the goal is WEST
				if (map[currentNode.x + 1][currentNode.y] == 3) {
					pathToFollow.add(NetworkMessage.MOVE_WEST);
					map[currentNode.x][currentNode.y] = 2;
					if (!isEnd(x, y)) {
						findShortestPathToFollow(currentNode.x + 1, currentNode.y);
					}
				}
			} else {// must move EAST
				pathToFollow.add(NetworkMessage.MOVE_EAST);
				map[currentNode.x][currentNode.y] = 2;
				if (!isEnd(x, y)) {
					findShortestPathToFollow(currentNode.x - 1, currentNode.y);
				}
			}

		} else if (goalPosition.x > currentNode.x) {// check if the goal is WEST
			if (map[currentNode.x + 1][currentNode.y] == 3) {// check if WEST is on the path
				pathToFollow.add(NetworkMessage.MOVE_WEST);
				map[currentNode.x][currentNode.y] = 2;
				if (!isEnd(x, y)) {
					findShortestPathToFollow(currentNode.x + 1, currentNode.y);
				}
			} else {// must move to either side to keep going
				if (map[currentNode.x][currentNode.y + 1] == 3) {// check if the left is on the path
					pathToFollow.add(NetworkMessage.MOVE_NORTH);
					map[currentNode.x][currentNode.y] = 2;
					if (!isEnd(x, y)) {
						findShortestPathToFollow(currentNode.x, currentNode.y + 1);
					}
				} else {// must move SOUTH
					pathToFollow.add(NetworkMessage.MOVE_SOUTH);
					map[currentNode.x][currentNode.y] = 2;
					if (!isEnd(x, y)) {
						findShortestPathToFollow(currentNode.x, currentNode.y - 1);
					}
				}
			}
		} else {// goal must be EAST
			if (map[currentNode.x - 1][currentNode.y] == 3) {// check if EAST is available
				pathToFollow.add(NetworkMessage.MOVE_EAST);
				map[currentNode.x][currentNode.y] = 2;
				if (!isEnd(x, y)) {
					findShortestPathToFollow(currentNode.x - 1, currentNode.y);
				}
			} else {// must move to either side to keep going
				if (map[currentNode.x][currentNode.y + 1] == 3) {// check if the left is on the path
					pathToFollow.add(NetworkMessage.MOVE_NORTH);
					map[currentNode.x][currentNode.y] = 2;
					if (!isEnd(x, y)) {
						findShortestPathToFollow(currentNode.x, currentNode.y + 1);
					}
				} else {// must move SOUTH
					pathToFollow.add(NetworkMessage.MOVE_SOUTH);
					map[currentNode.x][currentNode.y] = 2;
					if (!isEnd(x, y)) {
						findShortestPathToFollow(currentNode.x, currentNode.y - 1);
					}
				}
			}
		}
	}

	public void refreshMap() {
		map = new int[MAX_Y][MAX_X];
	}
	
	// returns the path that the robot will follow
	public ArrayList<Byte> getPathToFollow() {
		/*System.out.println("\nGRID\n");
		System.out.println("X Axis");
		System.out.println("     0  1  2  3  4  5  6  7  8  9  10 11");
		System.out.print("     -----------------------------------");
		for(int y = 0; y < MAX_Y; y++) {
			System.out.println();
			System.out.print("Y: " + y + "|");
			for(int x = 0; x < MAX_X; x++) {
				System.out.print(grid[y][x] + "  ");
			}
		}
		System.out.println();
		System.out.println("\nMAP\n");
		System.out.println("X Axis");
		System.out.println("     0  1  2  3  4  5  6  7  8  9  10 11");
		System.out.print("     -----------------------------------");
		for(int y = 0; y < MAX_Y; y++) {
			System.out.println();
			System.out.print("Y: " + y + "|");
			for(int x = 0; x < MAX_X; x++) {
				System.out.print(map[y][x] + "  ");
			}
		}
		*/
		return pathToFollow;
	}
}