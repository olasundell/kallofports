package kop.map.routecalculator;

import com.bbn.openmap.proj.Projection;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/20/11
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldCreationWorker implements Callable<NewWorld.LatitudeArr> {
	private NewWorld points;
	private float lat;
	private int i;
	private Projection projection;
	private List<Shape> shapeList;

	public WorldCreationWorker(NewWorld points, float lat, int i, Projection proj, List<Shape> shapes) {
		this.points = points;
		this.lat = lat;
		this.i = i;
		this.projection = proj;
		this.shapeList = shapes;
	}

	private boolean isWater(java.awt.Point p) {
		for (Shape s: shapeList) {
//			if (s!=null && s.intersects(i, j , i , j)) {
			if (s!=null && s.contains(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public NewWorld.LatitudeArr call() throws Exception {
		float lon;

		for (int j=0;j<points.lats[i].longitudes.length;j++) {
			lon = points.calcLon(j);
			if (isWater(projection.forward(lat, lon))) {
				points.lats[i].longitudes[j] = new Point(i,j,lat,lon);
			}
		}

		return points.lats[i];
	}
}
