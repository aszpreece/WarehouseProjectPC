package pathfinding;

import java.util.ArrayList;

public class Multiples {
	final static int GRAPH_STEPS = 30;
	final static int GRAPH_WIDTH = 12;
    final static int GRAPH_HEIGHT = 8;
    final static int POINT_OCCUPIED = 8;
    final static int POINT_PARKED = 9;
    
    int currentStep = 0;
    
    public int[][] graph = { 
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
		        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		};
	
    MultiGraph multiGraph = new MultiGraph(graph);
	
	public ArrayList<Byte> findPath(int startx, int starty, int endx, int endy) {
		
		// The ArrayList of moves
		ArrayList<Byte> multiList = new ArrayList<Byte>();
		
		// the current location of the robot
		int currentx = startx;
    	int currenty = starty;
    	
    	// the shortest path cost
    	int iterator = 0;
		// If the algorithm is called from a point where a robot was parked, it releases the point in the grid
		multiGraph.getGraph(currentStep)[startx][starty] = POINT_OCCUPIED;
		
		while (true) {
			// making the first step toward the goal
			iterator--;
			
			// if the robot is at the end position, return the list
			if (currentx == endx && currenty == endy) {
				return multiList;
			}
			
			if (currentx == 0) {
				if (currenty < GRAPH_HEIGHT && currenty > 0) {
					multiGraph.getGraph(currentStep + 1)[currentx][currenty + 1] = iterator;
					multiGraph.getGraph(currentStep + 1)[currentx][currenty - 1] = iterator;
				} else if (currenty == GRAPH_HEIGHT) {
					multiGraph.getGraph(currentStep + 1)[currentx][currenty - 1] = iterator;
				} else if (currenty == 0) {
					multiGraph.getGraph(currentStep + 1)[currentx][currenty + 1] = iterator;
				}
				if (multiGraph.getGraph(currentStep)[currentx][currenty + 1] == 1) {
					multiGraph.getGraph(currentStep)[currentx][currenty + 1] = iterator;
				}
			} 
		}
		
		
	}
}
