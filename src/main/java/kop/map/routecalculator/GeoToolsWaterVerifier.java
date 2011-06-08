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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ola Sundell
 */
public class GeoToolsWaterVerifier implements WaterVerifier {

	private ArrayList<Geometry> geometryList;
	private GeometryFactory geometryFactory;

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
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return null;
	}

	@Override
	public boolean isWater(double lat, double lon) {
		// TODO this is too magical. It needs to be extracted to a util method and tested properly.
		com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(lon-179.0, 89.0-lat));
		for (Geometry geometry: geometryList) {
			if (geometry.contains(point)) {
				return true;
			}
		}
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
