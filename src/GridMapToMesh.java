import lejos.robotics.pathfinding.FourWayGridMesh;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class GridMapToMesh {
	
	private FourWayGridMesh mesh;
	private GridMap map;

	public GridMapToMesh() {
		map = MapUtils.createRealWarehouse();
		mesh = new FourWayGridMesh(map, 0.3f, 0.17f);
	}
	
	public FourWayGridMesh createCompleteMesh() {
		return mesh;
	}
}