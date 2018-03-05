import lejos.robotics.pathfinding.NavigationMesh;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class GridMapToMesh {

	private NavigationMesh mesh;
	private GridMap map;
	
	public GridMapToMesh() {
		map = MapUtils.createRealWarehouse();
	}
	
}


//iterate through gridmap checking if the node is valid, and if the nodes around it are valid
//if it is, add to the mesh with the number of neighbours
