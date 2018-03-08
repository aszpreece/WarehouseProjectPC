import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import rp.robotics.mapping.MapUtils;

public class LinePathfinding {

	LineMap map;
	Pose startPos;
	Waypoint goalPos;
	ShortestPathFinder pathfinder;
	
	public LinePathfinding() {
		startPos = new Pose();
		goalPos = new Waypoint(startPos);
		map = MapUtils.createRealWarehouse();
		pathfinder = new ShortestPathFinder(map);
		
	}
	
	public void setStartPos(float x, float y) {
		startPos.setLocation(x, y);
	}
	
	public void setGoalPos(float x, float y) {
		goalPos.setLocation(x, y);
	}
	
	public void pathFind(/*float a, float b, float x, float y*/) {
		/* setStartPos(a, b);
		 * setGoalPos(x, y);
		 */
		try {
			Path path = pathfinder.findRoute(startPos, goalPos, map);
			System.out.println(path);
		} catch (DestinationUnreachableException e) {
			System.out.println("Unreachable Destination");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		LinePathfinding pathfind = new LinePathfinding();
		pathfind.pathFind();
	}
}
