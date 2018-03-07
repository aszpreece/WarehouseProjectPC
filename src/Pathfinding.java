import lejos.robotics.navigation.Pose;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.NavigationMesh;
import lejos.robotics.pathfinding.NodePathFinder;

public class Pathfinding {
	
	
	Pose startPos;
	NodePathFinder pathfinder;
	AstarSearchAlgorithm aStar;
	NavigationMesh mesh;
	GridMapToMesh gridToMesh;
	
	public Pathfinding() {
		gridToMesh = new GridMapToMesh();
		mesh = gridToMesh.createCompleteMesh(0);
		startPos = new Pose();
		pathfinder = new NodePathFinder(aStar, mesh);
		
		System.out.println(mesh.getMesh());
	}
	
	public static void main(String[] args) {
		
	}
}