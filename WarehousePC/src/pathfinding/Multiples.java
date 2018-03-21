package pathfinding;

import java.awt.PrintGraphics;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Collections;

import com.whshared.network.NetworkMessage;

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
	
    private int currentStepPlus() {
    	if (currentStep % 30  == GRAPH_STEPS - 1) {
    		//multiGraph.refresh(1);
    		currentStep++;
    	} else {
    		currentStep++;
    		//multiGraph.refresh(currentStep % 30 + 1);
    	}
    	return currentStep;
    }
    
    private ArrayList<Node> returnPath (int x, int y, int step) {
    	ArrayList<Node> list = new ArrayList<Node>();
    	int lastPoint = multiGraph.getGraph(step)[x][y];
    	list.add(new Node(x, y));
    	lastPoint++;
    	while (lastPoint < 0) {
    		boolean added = false;
    		if (!added) {
	    		if (x > 0) {
	    			if (multiGraph.getGraph(step)[x - 1][y] == lastPoint) {
	    				x--;
	    				list.add(new Node(x, y));
	    				added = true;
	    			}
	    		}
    		}
    		if (!added) {
	    		if (x < GRAPH_HEIGHT - 1) {
	    			if (multiGraph.getGraph(currentStep)[x + 1][y] == lastPoint) {
	    				x++;
	    				list.add(new Node(x, y));
	    				added = true;
	    			}
	    		}
    		}
    		if (!added) {
	    		if (y < GRAPH_WIDTH - 1) {
	    			if (multiGraph.getGraph(currentStep)[x][y + 1] == lastPoint) {
	    				y++;
	    				list.add(new Node(x, y));
	    				added = true;
	    			}
	    		}
    		}
    		if (!added) {
	    		if (y > 0) {
	    			if (multiGraph.getGraph(currentStep)[x][y - 1] == lastPoint) {
	    				y--;
	    				list.add(new Node(x, y));
	    				added = true;
	    			}
	    		}
    		}
    		lastPoint++;
    		step--;
    	}
    	/*
    	for (intPARKED i = -1; i > lastPoint; i--) {
    		if (multiGraph.getGraph(step)[x - 1][y] == lastPoint - 1) {
    			list.add(0, new Node(x - 1, y));
    			x--;
    			step--;
    		} else if (multiGraph.getGraph(step)[x + 1][y] == lastPoint - 1) {
    			list.add(0, new Node(x + 1, y));
    			x++;
    			step--;
    		} else if (multiGraph.getGraph(step)[x][y - 1] == lastPoint - 1) {
    			list.add(0, new Node(x, y - 1));
    			y--;
    			step--;
    		} else if (multiGraph.getGraph(step)[x][y + 1] == lastPoint - 1) {
    			list.add(0, new Node(x, y + 1));
    			y++;
    			step--;
    		}
    	}*/
    	//Collections.reverse(list);
    	for (int i = 0; i < 7; i++) {
    	System.out.print(list.get(i).getX());
    	System.out.println(list.get(i).getY());
    	}
    	return list;
    }
    
    private ArrayList<Node> findPath (int endx, int endy, int iterator) {
    	while (true) {
    							multiGraph.printGraph(currentStep);
    							System.out.println();
    		if (multiGraph.getGraph(currentStep)[endx][endy] != 1) {
    			return returnPath(endx, endy, currentStep);
    		}
	    	for (int i = 0; i < GRAPH_HEIGHT; i++) {
			    for (int j = 0; j < GRAPH_WIDTH; j++) {
			    	if (multiGraph.getGraph(currentStep)[i][j] == iterator) {
			    		if (j < GRAPH_WIDTH - 1) {
			    			if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
			    				multiGraph.getGraph(currentStep)[i][j + 1] = iterator - 1;
			    			}
			    		}
			    		if (j > 0) {
			    			if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
			    				multiGraph.getGraph(currentStep)[i][j - 1] = iterator - 1;
			    			}
			    		}
			    		if (i < GRAPH_HEIGHT - 1) {
			    			if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
			    				multiGraph.getGraph(currentStep)[i + 1][j] = iterator - 1;
			    			}
			    		}
			    		if (i > 0) {
			    			if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
			    				multiGraph.getGraph(currentStep)[i - 1][j] = iterator - 1;
			    			}
			    		}
			    	}
				    /*if (multiGraph.getGraph(currentStep)[i][j] == iterator) {
						if (i == 0) {
							if (j < GRAPH_WIDTH - 1 && j > 0) {
								if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j + 1] = iterator;
								}
								if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j - 1] = iterator;
								}
							} else if (j == GRAPH_WIDTH - 1) {
								if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j - 1] = iterator;
								}
							} else if (j == 0) {
								if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j + 1] = iterator;
								}
							}
							else if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
								multiGraph.getGraph(currentStep)[i][j + 1] = iterator;
							}
						} else if (i == GRAPH_HEIGHT - 1) {
							if (j < GRAPH_WIDTH - 1 && j > 0) {
								if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j - 1] = iterator;
								}
								if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j + 1] = iterator;
								}
							} else if (j == GRAPH_WIDTH - 1) {
								if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j - 1] = iterator;
								}
							} else if (j == 0) {
								if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
									multiGraph.getGraph(currentStep + 1)[i][j + 1] = iterator;
								}
							} else if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
								multiGraph.getGraph(currentStep)[i][j - 1] = iterator;
							}//
						} else if (j == 0) {
							if (i < GRAPH_HEIGHT - 1 && i > 0) {
								if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
									multiGraph.getGraph(currentStep)[i + 1][j] = iterator;
								}
								if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
									multiGraph.getGraph(currentStep + 1)[i - 1][j] = iterator;
								}
							} else if (i == GRAPH_HEIGHT - 1) {
								if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
									multiGraph.getGraph(currentStep + 1)[i - 1][j] = iterator;
								}
							} else if (i == 0) {
								if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
									multiGraph.getGraph(currentStep)[i + 1][j] = iterator;
								}
							} else if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
								multiGraph.getGraph(currentStep)[i + 1][j] = iterator;
							}
						} else if (j == GRAPH_WIDTH - 1) {
							if (i < GRAPH_HEIGHT && i > 0) {
								if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
									multiGraph.getGraph(currentStep)[i - 1][j] = iterator;
								}
								if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
									multiGraph.getGraph(currentStep + 1)[i + 1][j] = iterator;
								}
							} else if (i == GRAPH_HEIGHT - 1) {
								if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
									multiGraph.getGraph(currentStep)[i - 1][j] = iterator;
								}
							} else if (i == 0) {
								if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
									multiGraph.getGraph(currentStep + 1)[i + 1][j] = iterator;
								}
							}else if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
								multiGraph.getGraph(currentStep)[i - 1][j] = iterator;
							}
						} else {
							if (multiGraph.getGraph(currentStep)[i][j + 1] == 1) {
								multiGraph.getGraph(currentStep)[i][j + 1] = iterator;
							}
							if (multiGraph.getGraph(currentStep)[i][j - 1] == 1) {
								multiGraph.getGraph(currentStep)[i][j - 1] = iterator;
							}
							if (multiGraph.getGraph(currentStep)[i + 1][j] == 1) {
								multiGraph.getGraph(currentStep)[i + 1][j] = iterator;
							}
							if (multiGraph.getGraph(currentStep)[i - 1][j] == 1) {
								multiGraph.getGraph(currentStep)[i - 1][j] = iterator;
							}
						}
			    	}*/
			    }
	    	}
	    	//currentStep++;
	    	currentStepPlus();
	    	iterator--;
	    	System.out.println(iterator + "AAAAAh");
    	}
    }
    
    private ArrayList<Byte> nodeToByte (ArrayList<Node> nodes) {
    	ArrayList<Byte> multiList = new ArrayList<Byte>();
    	int currentx = nodes.get(0).getX();
    	int currenty = nodes.get(0).getY();
    	for (int i = 1; i < nodes.size(); i++) {					
			if (nodes.get(i).getX() > currentx) {
				currentx = nodes.get(i).getX();
				multiList.add(NetworkMessage.MOVE_EAST);
			} else if (nodes.get(i).getX() < currentx) {
				currentx = nodes.get(i).getX();
				multiList.add(NetworkMessage.MOVE_WEST);
			} else if (nodes.get(i).getY() > currenty) {
				currenty = nodes.get(i).getY();
				multiList.add(NetworkMessage.MOVE_NORTH);
			} else if (nodes.get(i).getY() > currenty) {
				currenty = nodes.get(i).getY();
				multiList.add(NetworkMessage.MOVE_SOUTH);
			}
		}
    	return multiList;
    }
    
	public ArrayList<Byte> pathfinder(int startx, int starty, int endx, int endy) {
		
		// The ArrayList of moves
		//ArrayList<Byte> multiList = new ArrayList<Byte>();
		
		// The ArrayList of nodes
		ArrayList<Node> nodesList = new ArrayList<Node>();
	
		// the current location of the robot
    	starty = GRAPH_HEIGHT - 1 -starty;
    	endy = GRAPH_HEIGHT - 1 -endy;
    	// the shortest path cost
    	int iterator = -1;
    	
    	multiGraph.getGraph(currentStep)[starty][startx] = iterator;
		// If the algorithm is called from a point where a robot was parked, it releases the point in the grid
		//multiGraph.getGraph(currentStep)[startx][starty] = POINT_OCCUPIED;
		
		/*
		while (true) {
			// making the first step toward the goal
			iterator--;
			
			// if the robot is at the end position, return the list
			if (currentx == endx && currenty == endy) {
				return multiList;
			}
			
		}
		*/
		//multiGraph.printGraph(0);
		//System.out.println();
		nodesList = findPath(endx, endy, iterator);
		for(int i = 0; i < GRAPH_STEPS; i++) {
			multiGraph.getGraph(i)[endy][endx] = POINT_PARKED;
		}
		
		/*multiGraph.refresh();
		for(int i = 0; i < 30; i++) {
			multiGraph.printGraph(i);
			System.out.println();
		}*/
		return nodeToByte(nodesList);
	}
}
