
package models;

import rp.robotics.localisation.ActionModel;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.navigation.Heading;

/**
 * Modified version of Nick Hawes Perfect action model, added code to do
 * movements in all four directions using one method and applied Bayes
 * conditional probability to a perfect action model
 *
 * @author Nick Hawes
 *
 */
public class PerfectActionModel implements ActionModel {

	/**
	 * the minimum probability to stop distributions in the map from becoming zero
	 */
	private final float MIN_PROBABILITY = 0.01f;

	@Override
	public GridPositionDistribution updateAfterMove(GridPositionDistribution _from, Heading _heading) {

		// Create the new distribution that will result from applying the action
		// model
		GridPositionDistribution to = new GridPositionDistribution(_from);

		// Move the probability in the correct direction for the action
		if (_heading == Heading.PLUS_X) {
			move(_from, to, 1, 0);
		} else if (_heading == Heading.PLUS_Y) {
			move(_from, to, 0, 1);
		} else if (_heading == Heading.MINUS_X) {
			move(_from, to, -1, 0);
		} else if (_heading == Heading.MINUS_Y) {
			move(_from, to, 0, -1);
		}

		return to;
	}

	/**
	 * @param _from
	 *            the grid distribution before making the move
	 * @param _to
	 *            the grid distribution after making the move
	 * @param dx
	 *            the change in x coordinate (will always be 1 or -1)
	 * @param dy
	 *            the change in y coordinate (will always be 1 or -1)
	 */
	private void move(GridPositionDistribution _from, GridPositionDistribution _to, int dx, int dy) {

		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {

					// position before move
					int fromX = x;
					int fromY = y;
					float fromProb = 0;

					// position after move, add change in position
					int toX = x + dx;
					int toY = y + dy;

					if (dx == 0) { // if the change in x is 0 then it was a move in the y direction
						if (_to.isValidGridPosition(toX, toY) && !_to.isObstructed(toX, toY)) {
							// check all points before the from positions, (either in the plus x or minus x
							// directions),
							// stop when a wall is reached since the current position cannot be reached by
							// passing through
							// a wall
							for (int i = fromY; _to.isValidGridPosition(fromX, i) && !_to.isObstructed(fromX, i)
									&& i <= _to.getGridHeight() && i >= 0; i += dy) {
								// sum the probability at the from location
								fromProb += _from.getProbability(fromX, i);
							}
							// have a minimum probability so no locations become improbable
							_to.setProbability(toX, toY, Math.max(fromProb, MIN_PROBABILITY));
						}

					} else { // if the change in y is 0 then it was a move in the x direction
						if (_to.isValidGridPosition(toX, fromY) && !_to.isObstructed(toX, toY)) {
							// check all points before the from positions, (either in the plus y or minus y
							// directions),
							// stop when a wall is reached since the current position cannot be reached by
							// passing through
							// a wall
							for (int i = fromX; _to.isValidGridPosition(i, fromY) && !_to.isObstructed(i, fromY)
									&& i <= _to.getGridWidth() && i >= 0; i += dx) {
								// sum the probability at the from location
								fromProb += _from.getProbability(i, fromY);
							}
							// have a minimum probability so no locations become improbable
							_to.setProbability(toX, toY, Math.max(fromProb, MIN_PROBABILITY));
						}

					}
					// set probability for position after move

				}
			}
		}
		// normalise to stop total probability from being bigger than 1
		_to.normalise();
	}

}
