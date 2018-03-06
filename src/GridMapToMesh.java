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

	private void createCompleteMesh(int ypos) {
		// iterate across a row of the grid
		for (int xpos = 0; xpos < GRID_WIDTH; xpos++) {
			if (checkPointValidity(xpos, ypos)) {// check the point is valid
				Point coord = map.getCoordinatesOfGridPosition(xpos, ypos);// get the coordinates of the point
				Node node = new Node(coord.x, coord.y);// create the node for the point

				// check how many neighbours the point will - check points around are valid
				int noOfNeighbours = 0;// the number of neighbours
				if (checkPointValidity(xpos - 1, ypos)) {
					noOfNeighbours++;
				}
				if (checkPointValidity(xpos + 1, ypos)) {
					noOfNeighbours++;
				}
				if (checkPointValidity(xpos, ypos - 1)) {
					noOfNeighbours++;
				}
				if (checkPointValidity(xpos, ypos + 1)) {
					noOfNeighbours++;
				}

				mesh.addNode(node, noOfNeighbours);
			}
		}

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

// iterate through gridmap checking if the node is valid, and if the nodes
// around it are valid
// if it is, add to the mesh with the number of neighbours
