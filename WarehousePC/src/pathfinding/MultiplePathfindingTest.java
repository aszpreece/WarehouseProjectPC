package pathfinding;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultiplePathfindingTest {

	@Test
	public void testMultiples() {
		Multiples pathfinder = new Multiples();
		pathfinder.pathfinder(0, 5, 9, 7);
		pathfinder.pathfinder(0, 5, 9, 6);
		pathfinder.pathfinder(0, 6, 10, 7);
		pathfinder.pathfinder(9, 7, 1, 0);
		System.out.println(pathfinder.pathfinder(0, 0, 2, 0));
	}

}
