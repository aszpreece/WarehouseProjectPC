package com.whpc.pathfinding;

import java.util.ArrayList;

public class PathfindOnGraph {
	
	final int NODE_TRIED = -1;
	final int PATH_NODE = 2;
	
	final int [][] graph = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
        };
	
	final int MAX_X = graph[0].length;
	final int MAX_Y = graph.length;
	
	Node startPosition;
	Node goalPosition;
	ArrayList<String> path;
	int [][] grid;
	int [][] map;
	
	PathfindOnGraph(){
		startPosition = new Node (0,0);
		goalPosition = new Node (0,0);
		path = new ArrayList<String>();
		grid = graph;
		map = new int[MAX_X][MAX_Y];
	}
	
	public static void main(String[] args) {
		PathfindOnGraph pathfinder = new PathfindOnGraph();
		pathfinder.setGoalPosition(5, 5);
		System.out.println(pathfinder.findAPath(pathfinder.startPosition.x, pathfinder.startPosition.y));
	}
	
	public boolean findAPath(int i, int j) {
        if (!isValid(i,j)) {
            return false;
        }

        if (isEnd(i, j) ) {
            map[i][j] = PATH_NODE;
            return true;
        } else {
            map[i][j] = NODE_TRIED;
        }

        // North
        if (findAPath(i - 1, j)) {
            map[i-1][j] = PATH_NODE;
            return true;
        }
        // East
        if (findAPath(i, j + 1)) {
            map[i][j + 1] = PATH_NODE;
            return true;
        }
        // South
        if (findAPath(i + 1, j)) {
            map[i + 1][j] = PATH_NODE;
            return true;
        }
        // West
        if (findAPath(i, j - 1)) {
            map[i][j - 1] = PATH_NODE;
            return true;
        }
        return false;
    }

    private boolean isEnd(int i, int j) {
        return i == MAX_Y - 1 && j == MAX_X - 1;
    }

    private boolean isValid(int i, int j) {
        if (inRange(i, j) && isOpen(i, j) && !isTried(i, j)) {
            return true;
        }
        return false;
    }

    private boolean isOpen(int i, int j) {
        return grid[i][j] == 1;
    }

    private boolean isTried(int i, int j) {
        return map[i][j] == NODE_TRIED;
    }

    private boolean inRange(int i, int j) {
        return inHeight(i) && inWidth(j);
    }

    private boolean inHeight(int i) {
        return i >= 0 && i < MAX_Y;
    }

    private boolean inWidth(int j) {
        return j >= 0 && j < MAX_X;
    }
	
	public void setStartPosition(int x, int y) {
		startPosition.x = x;
		startPosition.y = y;
		
	}
	
	public void setGoalPosition(int x, int y) {
		if (graph[x][y] != 0) {
			startPosition.x = x;
			startPosition.y = y;
		}
	}
}