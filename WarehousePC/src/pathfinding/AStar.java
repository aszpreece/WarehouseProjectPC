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

public class AStar {

	int width, height;

	/**
	 * representation of the warehouse map: 1's are valid positions and 0's are walls, top left is (0,0)
	 */
	private final Map<Node, String> nodes = new HashMap<Node, String>();

	final static int[][] graph = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
								   { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, 
			                       { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
			                       { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
			                       { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

	public AStar() {
		width = graph[0].length - 1;
		height = graph.length - 1;
	}

	/**
	 * @param start the starting grid position of the robot
	 * @param end its goal
	 * @param startTimeStep the current time step
	 * @return a list of bytes corresponding to NetworkMessage to send to the real robot
	 */
	public List<Byte> pathfind(Node start, Node end) {
		
		//if the pathfinder gets called with the same starting position, it just returns a list with no instructions to move
		if (end.equals(start)) {
			return new ArrayList<Byte>();
		}
		
		//the nodes which have not been expanded, ordered by F therefore the node pathstep with the lowest F will be expanded first
		PriorityQueue<PathStep> openList = new PriorityQueue<PathStep>();
		//the nodes which have already been expanded which no longer need to be put into the open list if they are rediscovered
		Set<PathStep> closedList = new HashSet<PathStep>();

		//add the starting node to the starting list
		openList.add(new PathStep(Optional.empty(), start, heuristic(start, end)));
		//adjacent nodes
		ArrayList<Node> potential = new ArrayList<Node>();
		
		while (!openList.isEmpty()) {
			
			PathStep currentStep = openList.poll();
			closedList.add(currentStep);
	
			//add add all adjacent nodes to the potential nodes to expand
			potential.add(new Node(currentStep.getCoordinate().getX(), currentStep.getCoordinate().getY() + 1));
			potential.add(new Node(currentStep.getCoordinate().getX(), currentStep.getCoordinate().getY() - 1));
			potential.add(new Node(currentStep.getCoordinate().getX() + 1, currentStep.getCoordinate().getY()));
			potential.add(new Node(currentStep.getCoordinate().getX() - 1, currentStep.getCoordinate().getY()));
			
			for (Node node : potential) {
				if (isSpace(node)) {
					PathStep s = new PathStep(Optional.of(currentStep), node, heuristic(currentStep.getCoordinate(), end));
					
					//if the current node isn't in either node list that means it is newly discovered therefore add it to the open list
					if (!closedList.contains(s) && !openList.contains(s)) {
						
						//the end goal has been reached: return the reconstructed path to get to it
						if (end.equals(s.getCoordinate())) {
							return reconstruct(s);
						}
						openList.add(s);
					}
				}
			}
			potential.clear();
		}
		//if the openlist has been exhausted and the method hasn't returned due to the goal node being expanded, there is no path available
		return new ArrayList<Byte>();
	}

	private List<Byte> reconstruct(PathStep current) {
		List<Byte> directions = new ArrayList<Byte>();
		PathStep parent = null;
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
			
			//advance to previous node
			current = parent;
		}
		//since the path is reconstructed in reverse it needs to be reversed to get it back into the correct chronological order
		Collections.reverse(directions);
		return directions;
	}

	private boolean isSpace(Node n) {
		//for a node to be possibly free it must within the grid.
		if (n.x <= width && n.x >= 0 && n.y <= height && n.y >= 0) {
			//for a node to actually be free it must not be a wall
			return graph[n.y][n.x] == 1;
		}
		return false;
	}

	//manhattan distance for heuristic
	private int heuristic(Node start, Node end) {
		return Math.abs((end.x - start.x) + (end.y - start.y));
	}

}
