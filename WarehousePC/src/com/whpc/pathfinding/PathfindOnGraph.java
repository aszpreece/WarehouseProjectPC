package com.whpc.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.whshared.network.NetworkMessage;

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
        Djikstras djikstras = new Djikstras(graph);
        
        boolean solved = djikstras.pathfind(0, 7, 7, 7);
        
        System.out.println("Solved: " + solved);
        System.out.println(djikstras.toString());
  
        pathfinder.pathToFollow = djikstras.getPathToFollow();
        
        Collections.reverse(pathfinder.pathToFollow);
        System.out.println(pathfinder.pathToFollow);
    }

    public PathfindOnGraph(int[][] grid) {
    	pathToFollow = new ArrayList<Byte>();
    }
}