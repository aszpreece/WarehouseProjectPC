//package pathfinding;
//
//import java.awt.PrintGraphics;
//import java.awt.print.Printable;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import com.whshared.network.NetworkMessage;
//
//public class Multiples {
//	final static int GRAPH_STEPS = 30;
//	final static int GRAPH_WIDTH = 12;
//    final static int GRAPH_HEIGHT = 8;
//    final static int POINT_OCCUPIED = 8;
//    final static int POINT_PARKED = 9;
//    
//    int currentStep = 0;
//    
//    public int[][] graph = { 
//				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//		        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
//		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
//		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
//		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
//		        {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1},
//		        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//		};
//	
//    MultiGraph multiGraph = new MultiGraph(graph);
//
//
//    private int currentStepPlus() {
//    	if (currentStep % 30  == GRAPH_STEPS - 1) {
//    		currentStep++;
//    	} else {
//    		currentStep++;
//    	}
//    	return currentStep;
//    }
//    
//    private ArrayList<Node> returnPath (int x, int y, int step) {
//    	ArrayList<Node> list = new ArrayList<Node>();
//    	int lastPoint = multiGraph.getGraph(step)[x][y];
//    	list.add(new Node(x, y));
//    	lastPoint++;
//    	int counter = 0;
//    	while (lastPoint < 0) {
//    		boolean added = false;
//    		if (!added) {
//	    		if (x > 0) {
//	    			if (multiGraph.getGraph(step)[x - 1][y] == lastPoint) {
//	    				x--;
//	    				list.add(new Node(x, y));
//	    				added = true;
//	    			}
//	    		}
//    		}
//    		if (!added) {
//	    		if (x < GRAPH_HEIGHT - 1) {
//	    			if (multiGraph.getGraph(step)[x + 1][y] == lastPoint) {
//	    				x++;
//	    				list.add(new Node(x, y));
//	    				added = true;
//	    			}
//	    		}
//    		}
//    		if (!added) {
//	    		if (y < GRAPH_WIDTH - 1) {
//	    			if (multiGraph.getGraph(step)[x][y + 1] == lastPoint) {
//	    				y++;
//	    				list.add(new Node(x, y));
//	    				added = true;
//	    			}
//	    		}
//    		}
//    		if (!added) {
//	    		if (y > 0) {
//	    			if (multiGraph.getGraph(step)[x][y - 1] == lastPoint) {
//	    				y--;
//	    				list.add(new Node(x, y));
//	    				added = true;
//	    			}
//	    		}
//    		}
//    		
//    		multiGraph.getGraph(step)[list.get(counter).getX()][list.get(counter).getY()] = POINT_OCCUPIED;
//    		multiGraph.printGraph(step);
//    		System.out.println();
//    		
//    		lastPoint++;
//    		step--;
//    		
//    		counter++;
//    	}
//    	
//    	Collections.reverse(list);
//    	for (int i = 0; i < list.size(); i++) {
//    	//System.out.print(list.get(i).getX());
//    	//System.out.println(list.get(i).getY());
//    	}    
//    	return list;
//    }
//    
//    private ArrayList<Node> findPath (int endx, int endy, int iterator) {
//    	while (true) {
//    		//multiGraph.printGraph(currentStep);
//    		//System.out.println();
//    		if (multiGraph.getGraph(currentStep)[endx][endy] != 1) {
//    			//System.out.println(multiGraph.getGraph(currentStep)[endx][endy]);
//    			return returnPath(endx, endy, currentStep);
//    		}
//	    	for (int i = 0; i < GRAPH_HEIGHT; i++) {
//			    for (int j = 0; j < GRAPH_WIDTH; j++) {
//			    	if (multiGraph.getGraph(currentStep)[i][j] == iterator) {
//			    		if (j < GRAPH_WIDTH - 1) {
//			    			if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
//			    				multiGraph.getGraph(currentStep)[i][j + 1] = iterator - 1;
//			    			}
//			    		}
//			    		if (j > 0) {
//			    			if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
//			    				multiGraph.getGraph(currentStep)[i][j - 1] = iterator - 1;
//			    			}
//			    		}
//			    		if (i < GRAPH_HEIGHT - 1) {
//			    			if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
//			    				multiGraph.getGraph(currentStep)[i + 1][j] = iterator - 1;
//			    			}
//			    		}
//			    		if (i > 0) {
//			    			if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
//			    				multiGraph.getGraph(currentStep)[i - 1][j] = iterator - 1;
//			    			}
//			    		}
//			    	}
//			    }
//	    	}
//	        	 
//	    	currentStepPlus();
//	    	iterator--;
//    	}
//    }
//    
//    private ArrayList<Byte> nodeToByte (ArrayList<Node> nodes) {
//    	ArrayList<Byte> multiList = new ArrayList<Byte>();
//    	int currentx = nodes.get(0).getX();
//    	int currenty = nodes.get(0).getY();
//    	for (int i = 1; i < nodes.size(); i++) {					
//			if (nodes.get(i).getX() > currentx) {
//				currentx = nodes.get(i).getX();
//				multiList.add(NetworkMessage.MOVE_SOUTH);
//			} else if (nodes.get(i).getX() < currentx) {
//				currentx = nodes.get(i).getX();
//				multiList.add(NetworkMessage.MOVE_NORTH);
//			} else if (nodes.get(i).getY() > currenty) {
//				currenty = nodes.get(i).getY();
//				multiList.add(NetworkMessage.MOVE_EAST);
//			} else if (nodes.get(i).getY() > currenty) {
//				currenty = nodes.get(i).getY();
//				multiList.add(NetworkMessage.MOVE_WEST);
//			}
//		}
//    	return multiList;
//    }
//    
//	public ArrayList<Byte> pathfinder(int startx, int starty, int endx, int endy) {
//		
//		ArrayList<Node> nodesList = new ArrayList<Node>();
//	
//    	starty = GRAPH_HEIGHT - 1 -starty;
//    	endy = GRAPH_HEIGHT - 1 -endy;
//
//    	int iterator = -1;
//    	
//    	multiGraph.getGraph(currentStep)[starty][startx] = iterator;
//		
//		nodesList = findPath(endy, endx, iterator);
//		for(int i = 0; i < GRAPH_STEPS; i++) {
//			multiGraph.getGraph(i)[endy][endx] = POINT_PARKED;
//		}
//
//		multiGraph.refresh();
//		/*for(int i = 0; i < 30; i++) {
//			multiGraph.printGraph(i);
//			System.out.println();
//		}*/
//		return nodeToByte(nodesList);
//	}
//}
