import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import java.util.ArrayDeque;
import java.util.Queue;

import com.whshared.network.*;

public class LinePathfinding {

	LineMap map;
	Pose startPos;
	Waypoint goalPos;
	ShortestPathFinder pathfinder;

	public LinePathfinding() {
		startPos = new Pose();
		goalPos = new Waypoint(startPos);
		map = createLineMap.create();
		pathfinder = new ShortestPathFinder(map);

	}

	// sets the start location of the job
	public void setStartPos(float x, float y) {
		startPos.setLocation(x, y);
	}

	// sets the goal location of the job
	public void setGoalPos(float x, float y) {
		goalPos.setLocation(x, y);
	}

	/*
	 * method for returning the path the robot needs to take to get to the goal
	 * 
	 * @return Path the path the robot needs to take - path of 2d waypoints
	 */
	private Path pathFind(/* float a, float b, float x, float y */) {
		/*
		 * setStartPos(a, b); setGoalPos(x, y);
		 */

		//0,0 is 0.17, 0.155
		
		setStartPos(0, 0.3f);
		setGoalPos(0.6f,0.3f);
		
		Path path = new Path();

		try {
			path = pathfinder.findRoute(startPos, goalPos, map);
			System.out.println(path);
		} catch (DestinationUnreachableException e) {
			System.out.println("Unreachable Destination");
			e.printStackTrace();
		}
		return path;
	}

	/*
	 * method to turn the path into a set of directions for route execution
	 * 
	 * @return Queue the queue of directions the robot needs to take
	 */
	private Queue<Byte> pathToDirections(Path path) {
		Queue<Byte> directions = new ArrayDeque<Byte>();
		for (int i = 0; i < path.size(); i++) {//go through all the steps of the path
			if (i == 0) {//if at the first step
				if (startPos.getY() == path.get(i).y) {//if the y remains the same
					directions.add(directionFinder(startPos.getX(), path.get(i).x, 'x'));
				}else {//x remains the same
					directions.add(directionFinder(startPos.getY(), path.get(i).y, 'y'));
				}
			}else {//not the first step
				if (path.get(i).y == path.get(i-1).y) {//if the y remains the same
					directions.add(directionFinder(path.get(i).x, path.get(i-1).x, 'x'));
				}else {//x remains the same
					directions.add(directionFinder(path.get(i).y, path.get(i-1).y, 'y'));
				}
			}
		}
		return directions;
	}

	/*
	 * helper function to find the direction the robot needs to move in next
	 * 
	 * @return Byte the next direction
	 */
	private Byte directionFinder(float current, float newPos, char axis) {
		if (axis == 'x') {// changing in the x direction
			if (newPos > current) {
				return NetworkMessage.MOVE_EAST;
			} else {
				return NetworkMessage.MOVE_WEST;
			}
		} else {// changing in the y direction
			if (newPos > current) {
				return NetworkMessage.MOVE_NORTH;
			} else {
				return NetworkMessage.MOVE_SOUTH;
			}
		}
	}

	public static void main(String[] args) {
		LinePathfinding pathfind = new LinePathfinding();
		Path path = pathfind.pathFind();
		Queue<Byte> directions = pathfind.pathToDirections(path);
		
		System.out.println(directions.toString());
	}
}
