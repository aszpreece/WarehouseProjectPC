package pathfinding;

import java.util.ArrayList;
import java.util.Collections;

public class PathfindOnGraph {
	
    final static int NODE_TRIED = 2;
    final static int PATH_NODE = 3;

    public static int[][] graph = { 
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
    Dijkstras djikstras;

    public static void main(String[] args) {
        PathfindOnGraph pathfinder = new PathfindOnGraph(graph);
        Dijkstras djikstras = new Dijkstras(graph); 

        djikstras.pathfind(6, 5, 8, 3);
  
        pathfinder.pathToFollow = djikstras.getPathToFollow();
        System.out.println(pathfinder.pathToFollow);
    }

    public PathfindOnGraph(int[][] grid) {
    	pathToFollow = new ArrayList<Byte>();
    }
}