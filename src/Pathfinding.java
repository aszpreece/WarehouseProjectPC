import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.Path;

public class Pathfinding {

	Node startNode;
	Node endNode;
	NodePathFinder pathfinder;
	AstarSearchAlgorithm aStar;
	FourWayGridMesh mesh;
	GridMapToMesh gridToMesh;

	public Pathfinding() {
		gridToMesh = new GridMapToMesh();
		mesh = gridToMesh.createCompleteMesh();
		pathfinder = new NodePathFinder(aStar, mesh);
		setStartNode(0, 0);
		pathFind();
	}

	private void setStartNode(float x, float y) {
		startNode = new Node(x, y);
	}

	private void setGoalNode(float d, float y) {
		endNode = new Node(d, y);
	}

	private Path pathFind() {
		setGoalNode(2.44f, 0);
		return aStar.findPath(startNode, endNode);
	}

	public static void main(String[] args) {
		Pathfinding pathfind = new Pathfinding();
		pathfind.pathFind();
	}
}
