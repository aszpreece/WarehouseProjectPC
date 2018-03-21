
import java.util.Optional;
import java.util.Random;

import javax.swing.JFrame;

import lejos.util.Delay;
import rp.robotics.LocalisedRangeScanner;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import models.PerfectActionModel;
import models.PerfectSensorModel;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.KillMeNow;
import rp.robotics.visualisation.MapVisualisationComponent;
import rp.systems.StoppableRunnable;

/**
 * @author timch
 * 
 *         Written from Nick Hawes ExampleMarkovLocalisation; code
 *         Contributions: modifications to "move method", wrote "localise" which
 *         tries to explore the map while respecting the obstructions in the
 *         map, wrote "whereAmI" which returns the location of the robot (if it
 *         is pass the threshold), wrote "printDistributionAs2DArray" which
 *         prints the grid distribution to help with precise debugging
 * 
 */

public class MarkovLocalisation {

	// The map used as the basis of behaviour
	private static GridMap m_map;

	// Probability distribution over the position of a robot on the given
	// grid map. Note this assumes that the robot has a known heading.
	private GridPositionDistribution m_distribution;

	// The visualisation showing position uncertainty
	private GridPositionDistributionVisualisation m_mapVis;

	// The pilot object used to move the robot around on the grid.
	private final GridPilot m_pilot;

	// The range scanning sensor
	private LocalisedRangeScanner m_ranger;

	/**
	 * the threshold to confirm the localisation is sure enough to be at a given
	 * location
	 * 
	 * IMPORTANT: setting it too low will give an incorrect localisation value while
	 * setting it too high will make it so it never realises its localised
	 * 
	 * Standard values for a max range simulated sensor is 0.7 which will allow it
	 * to localise accurately while with limited range such as 1 metre the
	 * simulation will rarely be more sure than 0.7 (only in corners)
	 */
	private float THRESHOLD = 0.6f;

	/**
	 * distance in metres to the closest wall, used to stop the robot from trying to
	 * travel into a wall.
	 */
	private final float CLOSEST_WALL = 0.20f;

	/**
	 * @param _robot
	 *            the simulated robot
	 * @param _gridMap
	 *            the map to localise on
	 * @param _start
	 *            starting position
	 * @param _ranger
	 *            the range sensor
	 */
	public MarkovLocalisation(MovableRobot _robot, GridMap _gridMap, GridPose _start, LocalisedRangeScanner _ranger) {
		m_map = _gridMap;
		m_pilot = new GridPilot(_robot.getPilot(), _gridMap, _start);
		m_ranger = _ranger;
		m_distribution = new GridPositionDistribution(m_map);
	}

	/**
	 * Optionally run the visualisation of the robot and localisation process. This
	 * is not necessary to run the localisation and could be removed once on the
	 * real robot.
	 * 
	 * @param _sim
	 */
	public void visualise(MapBasedSimulation _sim) {

		JFrame frame = new JFrame("Map Viewer");
		frame.addWindowListener(new KillMeNow());

		// visualise the distribution on top of a line map
		m_mapVis = new GridPositionDistributionVisualisation(m_distribution, m_map);
		MapVisualisationComponent.populateVisualisation(m_mapVis, _sim);

		frame.add(m_mapVis);
		frame.pack();
		frame.setSize(m_mapVis.getMinimumSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	/***
	 * Move the robot and update the distribution with the action and sensor models
	 * 
	 * @param _actionModel
	 *            model representing movements the robot has made
	 * @param _sensorModel
	 *            model representing the sensor readings at specific points
	 */
	private void move(ActionModel _actionModel, PerfectSensorModel _sensorModel) {

		// How long to sleep between updates, just for clarity on the
		// visualisation!
		long delay = 500;
		Heading heading = m_pilot.getGridPose().getHeading();

		// Move robot forward
		m_pilot.moveForward();

		// Update estimate of position using the action model
		m_distribution = _actionModel.updateAfterMove(m_distribution, heading);

		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			// m_mapVis.setDistribution(m_distribution);
		}

		// A delay so we can see what's going on
		Delay.msDelay(delay);

		// THIS LINE OF CODE ADDS NOISE TO THE SIMULATED SENSOR
		// float noise = (((2*r.nextFloat())-1)*NOISE);

		float noise = 0;
		m_distribution = _sensorModel.updateAfterSensing(m_distribution, heading, m_ranger.getRange() + noise);
		// If visualising, update the shown distribution
		if (m_mapVis != null) {
			m_distribution.normalise();
			m_mapVis.setDistribution(m_distribution);
		}

		// after the move and sensor distribution updates check if any point on the map
		// has surpassed the threshold.
		Optional selfLocation = whereAmI();
		Delay.msDelay(delay);
		if (selfLocation == Optional.empty()) {
			System.out.println("i'm not sure where i am");
		} else {
			System.out.println("i am at " + selfLocation);
		}
	}

	/**
	 * move the robot in a way so that it turns to check left and right every couple
	 * of moves to see if there is a location it can turn into, then uses these
	 * moves to update the action and sensor models
	 */
	public void localise() {
		// two models to represent the robot's movement and sensor
		ActionModel actionModel = new PerfectActionModel();
		PerfectSensorModel sensorModel = new PerfectSensorModel(m_map, 2.4f, 0.06f);

		// Check if location is known (should be optional empty at this point)
		Optional selfLocation = whereAmI();

		// amount of forward moves to make before checking if you can make a turn
		final int checkTurn = 6;
		// current forward moves
		int forward = 0;

		// carry on running while robot cannot determine its location
		while (selfLocation == Optional.empty()) {
			// turn if at a wall
			if (m_ranger.getRange() < CLOSEST_WALL + 0.05) {
				m_pilot.rotatePositive();
				forward = 0;
			}
			// robot should check left and right to see if it can turn, if it can turn to
			// explore more of the map
			else if (forward >= checkTurn) {
				m_pilot.rotateNegative();
				System.out.println(m_ranger.getRange());
				// check if range after rotation is a wall
				if (m_ranger.getRange() > CLOSEST_WALL + 0.05) {
					move(actionModel, sensorModel);
					forward = 0;
				}
				// if it is a wall rotate to the other side and check if the other size is a
				// wall
				else {
					m_pilot.rotatePositive();
					m_pilot.rotatePositive();
					if (m_ranger.getRange() > CLOSEST_WALL + 0.05) {
						System.out.println("turn");
						move(actionModel, sensorModel);
						forward = 0;
					}
					// if left and right are both walls then go back to going forwards
					else {
						m_pilot.rotateNegative();
					}
				}
			}
			// move forwards and increment the amount of times it has moved forward.
			else {
				move(actionModel, sensorModel);
				forward++;
			}
			selfLocation = whereAmI();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Work on this map
		// GridMap map = TestMaps.warehouseMap();
		// GridMap map = MapUtils.createRealWarehouse();

		// PHILLIP EAGLES FIXED MAP: https://github.com/corfeur12/warehouse_MapUtils_fix
		GridMap map = MapUtilsFix.createRealWarehouse2018();

		// Create the simulation using the given map. This simulation can run
		// without a GUI.
		MapBasedSimulation sim = new MapBasedSimulation(map);

		// the starting position of the robot for the simulation. This is not
		// known in the action model or position distribution
		// brute force generate a valid random starting location
		Random r = new Random();
		int startGridX = r.nextInt(12);
		int startGridY = r.nextInt(8);

		while (map.isObstructed(startGridX, startGridY)) {
			startGridX = r.nextInt(12);
			startGridY = r.nextInt(8);
		}

		// set the starting location
		GridPose gridStart = new GridPose(startGridX, startGridY, Heading.PLUS_X);

		// Create a robot with a range scanner but no bumper
		MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
				map.toPose(gridStart));
		LocalisedRangeScanner ranger = sim.getRanger(wrapper);
		System.out.println(ranger.getRange());

		// run the localisation
		MarkovLocalisation ml = new MarkovLocalisation(wrapper.getRobot(), map, gridStart, ranger);
		ml.visualise(sim);
		ml.localise();

	}

	/**
	 * print out the distribution
	 */
	private void printDistributionAs2dArray() {
		float[][] distribution = new float[m_map.getXSize()][m_map.getYSize()];
		for (int i = 0; i < m_map.getXSize(); i++) {
			String output = "";
			for (int j = 0; j < m_map.getYSize(); j++) {
				distribution[i][j] = m_distribution.getProbability(i, j);
				// https://stackoverflow.com/questions/25981349/java-double-round-off-to-2-decimal-always
				// top answer
				output += String.format("%.2f", distribution[i][j]) + " ";
			}
			System.out.println(output);
		}

	}

	/**
	 * @return either the node it thinks its at or an empty optional to indicate it
	 *         is not sure enough
	 */
	private Optional<Node> whereAmI() {
		Optional<Node> estimatedPoint = Optional.empty();
		// loop through the whole map
		for (int y = 0; y < m_distribution.getGridHeight(); y++) {

			for (int x = 0; x < m_distribution.getGridWidth(); x++) {
				// if a point is passed the threshold then report it as the estimated location
				if (m_distribution.getProbability(x, y) > THRESHOLD) {
					estimatedPoint = Optional.of(new Node(x, y));
				}
			}
		}
		return estimatedPoint;
	}

}