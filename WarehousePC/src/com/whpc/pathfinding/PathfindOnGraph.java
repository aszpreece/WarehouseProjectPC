package com.whpc.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

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
    PathfindOnGraph pathfinder;
    Djikstras djikstras;

    public static void main(String[] args) {
        PathfindOnGraph pathfinder = new PathfindOnGraph(graph);
        Djikstras djikstras = new Djikstras(graph);

        djikstras.pathfind(0, 1, 5, 5);
  
        pathfinder.pathToFollow = djikstras.getPathToFollow();
        System.out.println(pathfinder.pathToFollow);
    }

    public PathfindOnGraph(int[][] grid) {
    	pathToFollow = new ArrayList<Byte>();
    }
}