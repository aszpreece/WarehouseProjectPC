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

	public List<Byte> pathfind(Node start, Node end) {
		
		if (end.equals(start)) {
			return new ArrayList<Byte>();
		}
		
		PriorityQueue<PathStep> openList = new PriorityQueue<PathStep>();
		Set<PathStep> closedList = new HashSet<PathStep>();

		openList.add(new PathStep(Optional.empty(), start, heuristic(start, end)));
		ArrayList<Node> potential = new ArrayList<Node>();
		
		while (!openList.isEmpty()) {
			
			PathStep currentStep = openList.poll();
			//System.out.println("Iteration " + currentStep.getCoordinate().toString());
			closedList.add(currentStep);
	
			potential.add(new Node(currentStep.getCoordinate().getX(), currentStep.getCoordinate().getY() + 1));
			potential.add(new Node(currentStep.getCoordinate().getX(), currentStep.getCoordinate().getY() - 1));
			potential.add(new Node(currentStep.getCoordinate().getX() + 1, currentStep.getCoordinate().getY()));
			potential.add(new Node(currentStep.getCoordinate().getX() - 1, currentStep.getCoordinate().getY()));
			
			for (Node node : potential) {
				if (isSpace(node)) {
					PathStep s = new PathStep(Optional.of(currentStep), node, heuristic(currentStep.getCoordinate(), end));
					//System.out.println(node.toString() + " is a space!");
					if (!closedList.contains(s) && !openList.contains(s)) {
						if (end.equals(s.getCoordinate())) {
							return reconstruct(s);
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
		return null;
	}

	private List<Byte> reconstruct(PathStep current) {
		List<Byte> directions = new ArrayList<Byte>();
		PathStep parent = null;
		while (current.getParent().isPresent()) {
			parent = current.getParent().get();
			if (current.getCoordinate().x > parent.getCoordinate().x) {
				directions.add(NetworkMessage.MOVE_EAST);
			} else if(current.getCoordinate().x < parent.getCoordinate().x) {
				directions.add(NetworkMessage.MOVE_WEST);	
			} else if (current.getCoordinate().y > parent.getCoordinate().y) {
				directions.add(NetworkMessage.MOVE_NORTH);
			} else if(current.getCoordinate().y < parent.getCoordinate().y) {
				directions.add(NetworkMessage.MOVE_SOUTH);	
			} 
			current = parent;
		}
		Collections.reverse(directions);
		return directions;
	}

	private boolean isSpace(Node n) {
		if (n.x <= width && n.x >= 0 && n.y <= height && n.y >= 0) {
			return graph[n.y][n.x] == 1;
		}
		return false;
	}

	private int heuristic(Node start, Node end) {
		return Math.abs((end.x - start.x) + (end.y - start.y));
	}

}
