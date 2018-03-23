package unused;

import java.util.ArrayList;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.mapping.MapUtils;



/**
 * from Phillip Eagles https://github.com/corfeur12/warehouse_MapUtils_fix
 * fixed simulated map that allows robot to turn without crashing into walls
 *
 */
public class MapUtilsFix extends MapUtils {
	
	public static GridMap createRealWarehouse2018() {

		float height = 2.48f;
		float width = 3.68f;

		float xInset = 0.14f, yInset = 0.15f;
		int gridWidth = 12, gridHeight = 8;
		float cellSize = 0.30f;

		ArrayList<Line> lines = new ArrayList<Line>();

		// these are the walls for the world outline
		lines.add(new Line(0f, 0f, width, 0f));
		lines.add(new Line(width, 0f, width, height));
		lines.add(new Line(width, height, 0f, height));
		lines.add(new Line(0f, height, 0f, 0f));
		
		float yMin = 0.3f;
		float yMax = 1.78f;
		
		float x1Min = 0.30f;
		float x1Max = 0.59f;
		
		float x2Min = 1.2f;
		float x2Max = 1.49f;
		
		float x3Min = 2.10f;
		float x3Max = 2.39f;
		
		float x4Min = 3.01f;
		float x4Max = 3.30f;

		lines.add(new Line(x1Min, yMin, x1Min, yMax));
		lines.add(new Line(x1Max, yMin, x1Max, yMax));
		lines.add(new Line(x1Min, yMin, x1Max, yMin));
		lines.add(new Line(x1Min, yMax, x1Max, yMax));

		lines.add(new Line(x2Min, yMin, x2Min, yMax));
		lines.add(new Line(x2Max, yMin, x2Max, yMax));
		lines.add(new Line(x2Min, yMin, x2Max, yMin));
		lines.add(new Line(x2Min, yMax, x2Max, yMax));

		lines.add(new Line(x3Min, yMin, x3Min, yMax));
		lines.add(new Line(x3Max, yMin, x3Max, yMax));
		lines.add(new Line(x3Min, yMin, x3Max, yMin));
		lines.add(new Line(x3Min, yMax, x3Max, yMax));

		lines.add(new Line(x4Min, yMin, x4Min, yMax));
		lines.add(new Line(x4Max, yMin, x4Max, yMax));
		lines.add(new Line(x4Min, yMin, x4Max, yMin));
		lines.add(new Line(x4Min, yMax, x4Max, yMax));

		Line[] lineArray = new Line[lines.size()];

		lines.toArray(lineArray);

		return new GridMap(gridWidth, gridHeight, xInset, yInset, cellSize,
				new LineMap(lineArray, new Rectangle(0, 0, width, height)));
	}

}
