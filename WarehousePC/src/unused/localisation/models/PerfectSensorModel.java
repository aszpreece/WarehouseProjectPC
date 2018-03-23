/**
 *
 */
package unused.localisation.models;

import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.Heading;

/**
 * Empty sensor model for testing, Added
 *
 * @author Nick Hawes
 *
 */
public class PerfectSensorModel {

	/**
	 * the noise of the sensor (if not zero can be used to model the real robot in a
	 * more realistic way)
	 */
	private final float NOISE;

	/**
	 * Map of the world, used to find the distance from an obstacle at a point given
	 * a heading
	 */
	private GridMap map;

	/**
	 * the maximum range of the sensor (if not the actual max of the simulated
	 * sensor it can be used to model the real robot's sensors max range)
	 */
	private final float MAX_RANGE;

	public PerfectSensorModel(GridMap map, float MaxRange, float noise) {
		this.map = map;
		this.MAX_RANGE = MaxRange;
		this.NOISE = noise;
	}

	public GridPositionDistribution updateAfterSensing(GridPositionDistribution _from, Heading _heading,
			float reading) {

		// create a copy of the distribution
		GridPositionDistribution to = new GridPositionDistribution(_from);

		// loop through the map and update points accordingly
		for (int y = 0; y < to.getGridHeight(); y++) {

			for (int x = 0; x < to.getGridWidth(); x++) {

				// update if it is not an obstructed point
				if (!to.isObstructed(x, y)) {

					// check if the current point is within the noise threshold
					if (withinNoiseRange(x, y, _heading, reading)) {
						to.setProbability(x, y, _from.getProbability(x, y) * 1f);
					}
					// otherwise update it and reduce the probability to very low but not zero. the
					// only situation where this else if will be false is if max range is set below
					// the actual simulated max range in which case it will ignore the erroneous
					// values to comply with the custom max range
					else if (reading <= MAX_RANGE) {
						to.setProbability(x, y, _from.getProbability(x, y) * 0.01f);
					}
				}
			}
		}
		// normalise so the the sum of probabilities add up to one
		to.normalise();
		return to;
	}

	/**
	 * @param x
	 *            current x position
	 * @param y
	 *            current y poistion
	 * @param _heading
	 *            current direction robot is heading
	 * @param actual
	 *            the actual reading from the robot
	 * @return
	 */
	public boolean withinNoiseRange(int x, int y, Heading _heading, float actual) {
		float expected = map.rangeToObstacleFromGridPosition(x, y, Heading.toDegrees(_heading));
		return (expected + NOISE > actual && expected - NOISE < actual);
	}

}
