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

	public List<Byte> pathfind(Node start, Node end, int startTimeStep) {
		
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
				PathStep s = new PathStep(Optional.of(currentStep), node, heuristic(currentStep.getCoordinate(), end));
				if (isFree(node, startTimeStep + s.getG())) {	
					//System.out.println(node.toString() + " is a space!");
					if (!closedList.contains(s) && !openList.contains(s)) {
						if (end.equals(s.getCoordinate())) {
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
		return null;
	}

	private List<Byte> reconstruct(PathStep current, int currentTimeStep) {
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
			reservationTable.reservePosition(current.getCoordinate(), current.getG());
			reservationTable.reservePosition(parent.getCoordinate(), current.getG());
			current = parent;
		}
		Collections.reverse(directions);
		return directions;
	}

	private boolean isFree(Node n, int step) {
		if (n.x <= width && n.x >= 0 && n.y <= height && n.y >= 0) {
			return graph[n.y][n.x] == 1 && !reservationTable.isReserved(n, step);
		}
		return false;
	}

	private int heuristic(Node start, Node end) {
		AStar p = new AStar();
		return p.pathfind(start, end).size();
	}
	
	public void clearReservations() {
		//clear table.
		//reservations.clear();
	}

}
