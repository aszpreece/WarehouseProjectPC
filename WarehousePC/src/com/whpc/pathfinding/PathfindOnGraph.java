package com.whpc.pathfinding;

import java.util.ArrayList;

public class PathfindOnGraph {

    private ArrayList<Byte> pathToFollow;

    public static void main(String[] args) {
        PathfindOnGraph pathfinder = new PathfindOnGraph();
        ShortestPathFinder shortest = new ShortestPathFinder();

        shortest.pathfind(1, 6, 1, 0);
              
        System.out.println(shortest.toString());
        pathfinder.pathToFollow = shortest.getPathToFollow();
        System.out.println(pathfinder.pathToFollow);
    }

    public PathfindOnGraph() {
    	pathToFollow = new ArrayList<Byte>();
    }
}