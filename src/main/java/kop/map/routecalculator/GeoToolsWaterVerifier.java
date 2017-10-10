package kop.map.routecalculator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ola Sundell
 */
public class GeoToolsWaterVerifier implements WaterVerifier {

	private List<Geometry> geometryList;
	private GeometryFactory geometryFactory;
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void setupInstance(String shapeFileName) {
		File file = new File(shapeFileName);

		// TODO what if the file doesn't exist?

		ShapefileDataStore store = null;
		SimpleFeatureCollection collection = null;
		try {
			store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(file);
			SimpleFeatureSource featureSource = store.getFeatureSource();
			collection = featureSource.getFeatures();
		} catch (IOException e) {
			logger.error("Could not retrieve collection", e);
			return;
		}
		geometryFactory = new GeometryFactory();

		geometryList = new ArrayList<Geometry>();

		SimpleFeatureIterator iter = collection.features();

		while (iter.hasNext()) {
			SimpleFeature feature = iter.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			geometryList.add(geometry);
		}
		iter.close();
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Could not clone object.",e);
		}

		return null;
	}

	@Override
	public boolean isWater(double lat, double lon) {
		double realLon = lon;
		if (lon == -180.0) {
			realLon = -179.9;
		} else if (lon == 180.0) {
			realLon = 179.9;
		}
		Coordinate coordinate = new Coordinate(realLon,lat);
		com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(coordinate);
		for (Geometry geometry: geometryList) {
			if (geometry.contains(point)) {
				return true;
			}
		}
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
