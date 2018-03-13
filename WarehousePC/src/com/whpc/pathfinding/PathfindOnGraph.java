package com.whpc.pathfinding;

import java.util.Arrays;

public class PathfindOnGraph {
	
    final static int NODE_TRIED = 2;
    final static int PATH_NODE = 3;

    private static int[][] graph = { 
    		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    private Node startingPosition;
    private Node goalPosition;
    
    private int[][] grid;
    private int[][] map;
    
    private int MAX_Y;
    private int MAX_X;


    public static void main(String[] args) {
        PathfindOnGraph pathfinder = new PathfindOnGraph(graph);
        boolean solved = pathfinder.solve();
        System.out.println("Solved: " + solved);
        System.out.println(pathfinder.toString());
    }

    public PathfindOnGraph(int[][] grid) {
    	startingPosition = new Node(0, 0);
    	goalPosition = new Node(0, 0);
        this.grid = grid;
        this.MAX_Y = grid.length;
        this.MAX_X = grid[0].length;

        this.map = new int[MAX_Y][MAX_X];
        
        //set testing values
        startingPosition.set(0, 7);
        goalPosition = new Node(7, 11);
    }

    public boolean solve() {
        return findAPath(0,7);
    }

    private boolean findAPath(int height, int width) {
        if (!isValid(height,width)) {
            return false;
        }

        if ( isEnd(height, width) ) {
            map[height][width] = PATH_NODE;
            return true;
        } else {
            map[height][width] = NODE_TRIED;
        }

        // North
        if (findAPath(height - 1, width)) {
            map[height-1][width] = PATH_NODE;
            return true;
        }
        // East
        if (findAPath(height, width + 1)) {
            map[height][width + 1] = PATH_NODE;
            return true;
        }
        // South
        if (findAPath(height + 1, width)) {
            map[height + 1][width] = PATH_NODE;
            return true;
        }
        // West
        if (findAPath(height, width - 1)) {
            map[height][width - 1] = PATH_NODE;
            return true;
        }

        return false;
    }

    private boolean isEnd(int height, int width) {
        return height == goalPosition.x && width == goalPosition.y;
    }

    private boolean isValid(int height, int width) {
        if (inRange(height, width) && canTravel(height, width) && !isTried(height, width)) {
            return true;
        }
        return false;
    }

    private boolean canTravel(int height, int width) {
        return grid[height][width] == 1;
    }

    private boolean isTried(int height, int width) {
        return map[height][width] == NODE_TRIED;
    }

    private boolean inRange(int height, int width) {
        return inHeight(height) && inWidth(width);
    }

    private boolean inHeight(int height) {
        return height >= 0 && height < MAX_Y;
    }

    private boolean inWidth(int width) {
        return width >= 0 && width < MAX_X;
    }

    public String toString() {
        String s = "";
        for (int[] row : map) {
            s += Arrays.toString(row) + "\n";
        }
        return s;
    }
	
	public void setStartPosition(int x, int y) {
		startingPosition.x = x;
		startingPosition.y = y;
		
	}
	
	public void setGoalPosition(int x, int y) {
			goalPosition.x = x;
			goalPosition.y = y;
	}
}