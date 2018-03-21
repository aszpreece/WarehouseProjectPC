package pathfinding;

import java.util.Optional;

import types.Node;

public class PathStep implements Comparable<PathStep>{
	public PathStep(Optional<PathStep> parent, Node coordinate, int h) {
		this.parent = parent;
		this.coordinate = coordinate;
		this.g = parent.isPresent() ? parent.get().getG() + 1 : 0;
	}

	private Optional<PathStep> parent = Optional.empty();
	private Node coordinate;
	private int g;
	private int h;
	
	public int getFScore() {
		return h + g;
	}

	@Override
	public int compareTo(PathStep p) {
		return Integer.compare(getFScore(), p.getFScore());
	}
	
	public Node getCoordinate() {
		return coordinate;
	}
	
	public int getG() {
		return g;
	}
	
	public void setG(int v) {
		g = v;
	}
	
	public Optional<PathStep> getParent() {
		return parent;
	}
	
	 @Override
	 public int hashCode() {
	     return (coordinate.getX() >> 8) + coordinate.getY();
	 }
	
	@Override
	public boolean equals(Object o) {
		PathStep other = (PathStep)o;
		//System.out.println("Comparing " + coordinate.getX() + " " + coordinate.getY() + " and " + other.getCoordinate().getX() + " " + other.getCoordinate().getY());
		return coordinate.getX() == other.getCoordinate().getX() && coordinate.getY() == other.getCoordinate().getY();
	}

}
