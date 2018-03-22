package pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import com.whshared.network.NetworkMessage;

import types.Node;

public class CAStar {

	int width, height;

	private ReservationTable reservationTable;

	/**
	 * representation of the warehouse map: 1's are valid positions and 0's are walls, top left is (0,0)
	 */
	final static int[][] graph = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
								   { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			                       { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
			                       { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

	public CAStar(ReservationTable table) {
		reservationTable = table;
		width = graph[0].length - 1;
		height = graph.length - 1;
	}

	/**
	 * @param start the starting grid position of the robot
	 * @param end its goal
	 * @param startTimeStep the current time step
	 * @return a list of bytes corresponding to NetworkMessage to send to the real robot
	 */
	public List<Byte> pathfind(Node start, Node end, int startTimeStep) {
		
		//if the pathfinder gets called with the same starting position, it just returns a list with no instructions to move
		if (end.equals(start)) {
			//reserves the next two timesteps for the robots current position
			reservationTable.reservePosition(start, startTimeStep);
			reservationTable.reservePosition(start, startTimeStep + 1);
			return new ArrayList<Byte>();
		}
		//the nodes which have not been expanded, ordered by F therefore the node pathstep with the lowest F will be expanded first
		PriorityQueue<PathStep> openList = new PriorityQueue<PathStep>();
		//the nodes that have already been expanded
		Set<PathStep> closedList = new HashSet<PathStep>();
		
		//the starting node is added to the open list
		openList.add(new PathStep(Optional.empty(), start, heuristic(start, end)));
		
		//list to store adjacent nodes
		ArrayList<Node> potential = new ArrayList<Node>();
		
		while (!openList.isEmpty()) {
			
			PathStep currentStep = openList.poll();
			//System.out.println("Iteration " + currentStep.getCoordinate().toString());
			closedList.add(currentStep);
	
			//add add all adjacent nodes to the potential nodes to expand
			potential.add(new Node(currentStep.getCoordinate().getX(), currentStep.getCoordinate().getY() + 1));
			potential.add(new Node(currentStep.getCoordinate().getX(), currentStep.getCoordinate().getY() - 1));
			potential.add(new Node(currentStep.getCoordinate().getX() + 1, currentStep.getCoordinate().getY()));
			potential.add(new Node(currentStep.getCoordinate().getX() - 1, currentStep.getCoordinate().getY()));
			
			for (Node node : potential) {
				PathStep s = new PathStep(Optional.of(currentStep), node, heuristic(currentStep.getCoordinate(), end));
				
				//checks if the current node is a valid grid position and whether a robot has already reserved this spot at the current time step
				if (isFree(node, startTimeStep + s.getG())) {	
					
					//if the current node isn't in either node list that means it is newly discovered therefore add it to the open list
					if (!closedList.contains(s) && !openList.contains(s)) {
						if (end.equals(s.getCoordinate())) {
							
							//the end goal has been reached: return the reconstructed path to get to it
							return reconstruct(s, startTimeStep);
						}
						//System.out.println("Added to open list " + s.getCoordinate().getX() + " " + s.getCoordinate().getY());
						openList.add(s);
					}
				} else {
					//System.out.println(node.toString() + " is not a space!");
				}
			}
			potential.clear();
		}
		//if the openlist has been exhausted and the method hasn't returned due to the goal node being expanded, there is no path available
		return null;
	}

	private List<Byte> reconstruct(PathStep current, int currentTimeStep) {
		List<Byte> directions = new ArrayList<Byte>();
		PathStep parent = null;
		reservationTable.reservePosition(current.getCoordinate(), currentTimeStep + current.getG() + 1);
		
		//loop until their is no parent to the node (which means it has arrived at the start node
		while (current.getParent().isPresent()) {
			parent = current.getParent().get();
			
			//convert coordinate changes into cardinal movements
			if (current.getCoordinate().x > parent.getCoordinate().x) {
				directions.add(NetworkMessage.MOVE_EAST);
			} else if(current.getCoordinate().x < parent.getCoordinate().x) {
				directions.add(NetworkMessage.MOVE_WEST);	
			} else if (current.getCoordinate().y > parent.getCoordinate().y) {
				directions.add(NetworkMessage.MOVE_NORTH);
			} else if(current.getCoordinate().y < parent.getCoordinate().y) {
				directions.add(NetworkMessage.MOVE_SOUTH);	
			}
			
			//add the reserve the coordinate at the cur
			reservationTable.reservePosition(current.getCoordinate(), currentTimeStep + current.getG());
			reservationTable.reservePosition(parent.getCoordinate(), currentTimeStep + current.getG());
			
			//advance to previous node
			current = parent;
		}
		//since the path is reconstructed in reverse it needs to be reversed to get it back into the correct chronological order
		Collections.reverse(directions);
		return directions;
	}

	private boolean isFree(Node n, int step) {
		//for a node to be possibly free it must within the grid.
		if (n.x <= width && n.x >= 0 && n.y <= height && n.y >= 0) {
			//for a node to be actually free, it must not be a wall and the position must not be reserved at the current time step
			return graph[n.y][n.x] == 1 && !reservationTable.isReserved(n, step);
		}
		return false;
	}

	//the heuristic of cooperative AStar is AStar without cooperation
	private int heuristic(Node start, Node end) {
		AStar p = new AStar();
		return p.pathfind(start, end).size();
	}
	
	public void clearReservations() {
		//clear table.
		//reservations.clear();
	}

}
