import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.NodePathFinder;

public class Pathfinding {

	Pose startPos;
	Node startNode;
	Waypoint goalPoint;
	Node endNode;
	NodePathFinder pathfinder;
	AstarSearchAlgorithm aStar;
	FourWayGridMesh mesh;
	GridMapToMesh gridToMesh;

	public Pathfinding() {
		gridToMesh = new GridMapToMesh();
		mesh = gridToMesh.createCompleteMesh();
		startPos = new Pose();
		goalPoint = new Waypoint(0, 0);
		pathfinder = new NodePathFinder(aStar, mesh);
		setStartPoint(0, 0);
		pathFind();
	}

	private void setStartPoint(float x, float y) {
		startPos.setLocation(x, y);
		startNode = new Node(x, y);
	}

	private void setGoalPoint(float d, float y) {
		goalPoint.setLocation(d, y);
		endNode = new Node(d, y);
	}

	private void pathFind() {
		setGoalPoint(2.44f, 0);
		aStar.findPath(startNode, endNode);
	}

	public static void main(String[] args) {
		Pathfinding pathfind = new Pathfinding();
	}
}