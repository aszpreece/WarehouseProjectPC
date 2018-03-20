package pathfinding;

import java.util.ArrayList;

import com.whshared.network.NetworkMessage;

public class MultiPathfinder {

	final static int GRAPH_WIDTH = 12;
    final static int GRAPH_HEIGHT =8;
    final static int GRAPH_STEPS = 30;
    static int currentStep = 0;
    static boolean canRefresh = false;
    
    //public static int [][][] multi_graph = new int [GRAPH_WIDTH][GRAPH_HEIGHT][GRAPH_STEPS];

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

    static MultiGraph multiGraph = new MultiGraph(graph);
    
    public MultiPathfinder() {
    	/*for (int i = 0; i < GRAPH_STEPS; i++) {
    		for (int j = 0; j < GRAPH_HEIGHT; j++) {
    			for (int k = 0; k < GRAPH_WIDTH; k++) {
    				multi_graph[j][k][i] = graph[j][k];
    			}
    		}
    	}*/
    }

    private static int currentStepPlus() {
    	if (currentStep % 30  == GRAPH_STEPS - 1) {
    		multiGraph.refresh(0);
    		currentStep++;
    	} else {
    		currentStep++;
    		multiGraph.refresh(currentStep % 30);
    	}
    	return currentStep;
    }
    
    
    public static ArrayList<Byte> Pathfinder(int startx, int starty, int endx, int endy) {
    	ArrayList<Byte> multiList = new ArrayList<Byte>();
    	ArrayList<Byte> finalList = new ArrayList<Byte>();
    	multiList = (new ShortestPathFinder(multiGraph.getGraph(currentStep))).pathfind(starty, startx, endy, endx);
    	int currenty = starty;
    	int currentx = startx;
    	//multiGraph.occupy(startx, starty, currentStep % 30);
    	int i = 0;
    	while (i < multiList.size()) {
    		System.out.println(currenty);
    		System.out.println(currentx);
    		if (multiGraph.getGraph(currentStep % 30)[currenty][currentx] == 8) {
    			multiList.add(i, NetworkMessage.NO_MOVE);
    			i++;
    			currentStepPlus();
    		} else if (multiGraph.getGraph(currentStep % 30)[currenty][currentx] == 9) {
    			multiList = (new ShortestPathFinder(multiGraph.getGraph(currentStep))).pathfind(currenty, currentx, endy, endx);
    		} else {
    			switch(multiList.get(i)) {
    			case NetworkMessage.MOVE_SOUTH:
    				multiGraph.free(currentx, currenty, currentStep % 30);
    				currenty++;
    			case NetworkMessage.MOVE_WEST:
    				multiGraph.free(currentx, currenty, currentStep % 30);
    				currentx++;
    			case NetworkMessage.MOVE_EAST:
    				multiGraph.free(currentx, currenty, currentStep % 30);
    				currentx--;
    			case NetworkMessage.MOVE_NORTH:
    				multiGraph.free(currentx, currenty, currentStep % 30);
    				currenty--;
    			}
    		}
    		
    		finalList.add(multiList.get(i));
    		i++;
    		currentStepPlus();
    	}
    	return finalList;
    }
}
