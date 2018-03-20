package pathfinding;

import java.util.ArrayList;

public class MultiGraph {
	final static int GRAPH_STEPS = 30;
	final static int GRAPH_WIDTH = 12;
    final static int GRAPH_HEIGHT =8;
    final static int POINT_OCCUPIED = 8;
    final static int POINT_PARKED = 9;
    
	ArrayList<int [][]> multiGraph = new ArrayList<int [][]>();

	public MultiGraph(int[][] graph) {
		for (int i = 0; i < GRAPH_STEPS; i++) {
			multiGraph.add(graph);
		}
	}
	
    public int[][] getGraph(int time) {
    	return multiGraph.get(time);
    }
	
	public void refresh(int time) {
		for (int i = 0; i < GRAPH_HEIGHT; i++) {
			for (int j = 0; j < GRAPH_WIDTH; j++) {
				if (multiGraph.get(time)[i][j] != POINT_PARKED) {
					multiGraph.get(time)[i][j] = 1;
				}
			}
		}
	}
	/*
	public void occupy(int x, int y, int time) {
		if (x == 0 && (y > 0 || y <= GRAPH_HEIGHT)) {
			for (int i = 1; i <= GRAPH_HEIGHT; i++) {
				multiGraph.get(time)[i][0] = POINT_OCCUPIED;
			}
		} else if (x == GRAPH_WIDTH && (y > 0 || y <= GRAPH_HEIGHT)) {
			for (int i = 1; i <= GRAPH_HEIGHT; i++) {
				multiGraph.get(time)[i][0] = POINT_OCCUPIED;
			}
		}
	}
*/
	public void posSetOccupied(int x, int y, int time) {
		multiGraph.get(time)[x][y] = POINT_OCCUPIED;
		//occupy(x, y, time);
	}
	
	public void posSetParked(int x, int y, int time) {
		multiGraph.get(time)[x][y] = POINT_PARKED;
	}
	
	public void free(int x, int y, int time) {
		//System.out.println(y);
		//System.out.println(x);
		multiGraph.get(time)[y][x] = 1;

	}
}
