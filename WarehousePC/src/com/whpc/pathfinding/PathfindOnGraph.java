package com.whpc.pathfinding;

import java.util.ArrayList;

public class PathfindOnGraph {
	
    final static int NODE_TRIED = 2;
    final static int PATH_NODE = 3;
   
    public static int[][] graph = { 
    		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    private ArrayList<Byte> pathToFollow;
    
    public static ArrayList<Byte> Pathfinder() {
    	ArrayList<Byte> multiList = new ArrayList<Byte>();
    	
    }
    
    public static void main(String[] args) {
        PathfindOnGraph pathfinder = new PathfindOnGraph(graph);
        ShortestPathFinder shortest = new ShortestPathFinder(graph);
        MultiPathfinder multiple = new MultiPathfinder(graph);
        
        shortest.pathfind(2, 5, 0, 3);
        
  
        System.out.println(shortest.toString());
        pathfinder.pathToFollow = shortest.getPathToFollow();
        System.out.println(pathfinder.pathToFollow);
    }

    public PathfindOnGraph(int[][] grid) {
    	pathToFollow = new ArrayList<Byte>();
    }
}