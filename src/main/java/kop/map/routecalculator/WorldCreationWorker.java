package kop.map.routecalculator;

import com.bbn.openmap.proj.Projection;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Thread run by RouteCalculator to calculate graticules, it checks a NewWorld.LatitudeArr for isWater.
 */
public class WorldCreationWorker implements Callable<NewWorld.LatitudeArr> {
	private NewWorld points;
	private float lat;
	private int i;
	private WaterVerifier waterVerifier;

	public WorldCreationWorker(NewWorld points, float lat, int i, WaterVerifier waterVerifier) {
		this.points = points;
		this.lat = lat;
		this.i = i;
		this.waterVerifier = waterVerifier;
	}

	@Override
	public NewWorld.LatitudeArr call() throws Exception {
		float lon;

		for (int j=0;j<points.lats[i].longitudes.length;j++) {
			lon = points.calcLon(j);
//			if (waterVerifier.isWater(projection.forward(lat, lon))) {
			if (waterVerifier.isWater(lat, lon)) {
				points.lats[i].longitudes[j] = new Point(i,j,lat,lon);
			}
		}

		return points.lats[i];
	}
}
