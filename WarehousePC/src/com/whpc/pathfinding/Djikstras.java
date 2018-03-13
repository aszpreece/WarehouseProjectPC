package com.whpc.pathfinding;

public class Djikstras {
	
	final static int NODE_TRIED = 2;
    final static int PATH_NODE = 3;

	private int[][] grid;
	private int[][] map;

	private int MAX_Y;
	private int MAX_X;
	
	public Djikstras(int[][] grid) {
		this.grid = grid;
        this.MAX_Y = grid.length;
        this.MAX_X = grid[0].length;

        this.map = new int[MAX_Y][MAX_X];
	}
	
	 private boolean findAPathNE(int height, int width) {
	        if (!isValid(height,width)) {
	            return false;
	        }

	        if (isEnd(height, width)) {
	            map[height][width] = PATH_NODE;
	            return true;
	        } else {
	            map[height][width] = NODE_TRIED;
	        }

	        // North
	        if (findAPathNE(height - 1, width)) {
	            map[height-1][width] = PATH_NODE;
	            return true;
	        }
	        // East
	        if (findAPathNE(height, width + 1)) {
	            map[height][width + 1] = PATH_NODE;
	            return true;
	        }
	        // South
	        if (findAPathNE(height + 1, width)) {
	            map[height + 1][width] = PATH_NODE;
	            return true;
	        }
	        // West
	        if (findAPathNE(height, width - 1)) {
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
}
