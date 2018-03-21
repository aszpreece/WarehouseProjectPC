package pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import types.Node;

public class ReservationTable {
	
	private Map<Integer, ArrayList<Node>> reservations = new ConcurrentHashMap<Integer, ArrayList<Node>>(); 
	private Set<Node> parkedPositions = new HashSet<Node>();
	
	public boolean isReserved(Node pos, int timeStep) {
		return parkedPositions.contains(pos) || reservations.getOrDefault(timeStep, new ArrayList<Node>()).contains(pos);
	}
	
	public void reservePosition(Node pos, int timeStep) {
		if (reservations.get(timeStep) != null) {
			reservations.get(timeStep).add(pos);
		} else {
			ArrayList<Node> newList = new ArrayList<Node>();
			newList.add(pos);
			reservations.put(timeStep, newList); 
		}
	}
	
	public void parkPosition(Node pos) {
		parkedPositions.add(pos);
	}
	
	public void unparkPosition(Node pos) {
		parkedPositions.remove(pos);
	}

}
