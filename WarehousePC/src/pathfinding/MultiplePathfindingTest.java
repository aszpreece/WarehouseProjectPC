package pathfinding;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultiplePathfindingTest {

	@Test
	public void testMultiples() {
		Multiples pathfinder = new Multiples();
		System.out.println(pathfinder.pathfinder(3, 0, 5, 3));
	}

}
