package kop.map.routecalculator;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/21/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
	private static NewWorld world = null;

	public static NewWorld getBlankWorld() {
		return getBlankWorld(1, 20, 0);
	}

	public static NewWorld getBlankWorld(double scale, int northOffset, int southOffset) {
		NewWorld blankWorld = new NewWorld((int) Math.round(180*scale), (int) Math.round(360*scale));
		blankWorld.setNorthOffset(northOffset);
		blankWorld.setSouthOffset(southOffset);
		blankWorld.setScale((float) scale);

		for (int i=0;i< blankWorld.lats.length;i++) {
			for (int j=0;j< blankWorld.lats[i].longitudes.length;j++) {
				blankWorld.lats[i].longitudes[j] = new Point(i,j, blankWorld.calcLat(i), blankWorld.calcLon(j));
			}
		}

		return  blankWorld;
	}

	public static NewWorld getSmallWorld() {
		return getSmallWorld(1,30,15);
	}

	public static NewWorld getSmallWorld(float scale, int northOffset, int latSize) {
		if (world==null || world.getScale() == scale) {
			RouteCalculator calculator = new RouteCalculator();
			NewWorld newWorld = new NewWorld(Math.round(latSize*scale),Math.round(360*scale));
			newWorld.setScale(scale);
			newWorld.setNorthOffset(Math.round(northOffset*scale));
			newWorld.setSouthOffset(Math.round((180-northOffset-latSize)*scale));
			world = calculator.calculateWorld(newWorld);
		}
		return world;
	}
}
