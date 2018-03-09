import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.Path;

public class Pathfinding {

	Pose startPose;
	Waypoint endNode;
	NodePathFinder pathfinder;
	AstarSearchAlgorithm aStar;
	FourWayGridMesh mesh;
	GridMapToMesh gridToMesh;

	public Pathfinding() {
		gridToMesh = new GridMapToMesh();
		mesh = gridToMesh.createCompleteMesh();
		aStar = new AstarSearchAlgorithm();
		pathfinder = new NodePathFinder(aStar, mesh);
		startPose = new Pose(0.17f, 0.155f, 0);
		endNode = new Waypoint(0, 0);
		pathFind();
	}

//	private void setStartNode(float x, float y) {
//		startNode = new Node(x, y);
//	}

	private void setGoalWaypoint(float d, float y) {
		endNode.setLocation(d, y);
	}

	private Path pathFind() {
		setGoalWaypoint(2.1f, 3.36f);
		Path path = new Path();
		try {
			path = pathfinder.findRoute(startPose, endNode);
		} catch (DestinationUnreachableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(path.toString());
		return path;
	}

	public static void main(String[] args) {
		Pathfinding pathfind = new Pathfinding();
		pathfind.pathFind();
	}
}
