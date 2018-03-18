package com.whpc.pathfinding;

import java.util.ArrayList;

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

    private ArrayList<Byte> pathToFollow;

    public static void main(String[] args) {
        PathfindOnGraph pathfinder = new PathfindOnGraph(graph);
        ShortestPathFinder shortest = new ShortestPathFinder(graph);

        shortest.pathfind(0, 11, 7, 0);
  
        System.out.println(shortest.toString());
        pathfinder.pathToFollow = shortest.getPathToFollow();
        System.out.println(pathfinder.pathToFollow);
    }

    public PathfindOnGraph(int[][] grid) {
    	pathToFollow = new ArrayList<Byte>();
    }
}