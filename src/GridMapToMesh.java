import lejos.geom.Point;
import lejos.robotics.pathfinding.NavigationMesh;
import lejos.robotics.pathfinding.Node;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class GridMapToMesh {

	private final int GRID_WIDTH = 12;
	private final int GRID_HEIGHT = 8;

	private NavigationMesh mesh;
	private GridMap map;

	public GridMapToMesh() {
		map = MapUtils.createRealWarehouse();
		createCompleteMesh(0);
	}

	public NavigationMesh createCompleteMesh(int ypos) {
		// iterate across a row of the grid
		for (int xpos = 0; xpos < GRID_WIDTH; xpos++) {
			if (checkPointValidity(xpos, ypos)) {// check the point is valid
				Point coord = map.getCoordinatesOfGridPosition(xpos, ypos);// get the coordinates of the point
				Node node = new Node(coord.x, coord.y);// create the node for the point

				// check how many neighbours the point will - check points around are valid
				//if the neighbours are valid, connect them
				int noOfNeighbours = 0;// the number of neighbours
				if (checkPointValidity(xpos - 1, ypos)) {
					Point leftNeighbour = map.getCoordinatesOfGridPosition(xpos - 1, ypos);
					Node leftNode = new Node(leftNeighbour.x, leftNeighbour.y);
					mesh.connect(node, leftNode);
					noOfNeighbours++;
				}
				if (checkPointValidity(xpos + 1, ypos)) {
					Point rightNeighbour = map.getCoordinatesOfGridPosition(xpos + 1, ypos);
					Node rightNode = new Node(rightNeighbour.x, rightNeighbour.y);
					mesh.connect(node, rightNode);
					noOfNeighbours++;
				}
				if (checkPointValidity(xpos, ypos - 1)) {
					Point downNeighbour = map.getCoordinatesOfGridPosition(xpos, ypos - 1);
					Node downNode = new Node(downNeighbour.x, downNeighbour.y);
					mesh.connect(node, downNode);
					noOfNeighbours++;
				}
				if (checkPointValidity(xpos, ypos + 1)) {
					Point upNeighbour = map.getCoordinatesOfGridPosition(xpos, ypos + 1);
					Node upNode = new Node(upNeighbour.x, upNeighbour.y);
					mesh.connect(node, upNode);
					noOfNeighbours++;
				}
				mesh.addNode(node, noOfNeighbours);
			}
		}
		//when it reaches the end of the row, call on next y value
		ypos++;
		if (ypos > GRID_HEIGHT  == false) {
			createCompleteMesh(ypos);
		} 
		return mesh;
	}

	private boolean checkPointValidity(int xpos, int ypos) {
		if (map.isValidGridPosition(xpos, ypos)) {// check that it is a valid grid position
			if (!map.isObstructed(xpos, ypos)) {// check grid point isn't obstructed
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}