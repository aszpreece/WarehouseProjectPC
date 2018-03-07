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
		//map = //get linemap code from maputils in a new class
		pathfinder = new ShortestPathFinder(map);
		
	}
	
	private void setStartPos(float x, float y) {
		startPos.setLocation(x, y);
	}
	
	private void setGoalPos(float x, float y) {
		goalPos.setLocation(x, y);
	}
	
	private void pathFind() {
		try {
			Path path = pathfinder.findRoute(startPos, goalPos, map);
		} catch (DestinationUnreachableException e) {
			System.out.println("Unreachable Destination");
			e.printStackTrace();
		}
	}
}
