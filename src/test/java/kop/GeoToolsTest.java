package kop;

import com.bbn.openmap.geo.Geo;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import kop.map.routecalculator.NewWorld;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileFeatureStore;
import org.geotools.data.shapefile.ShapefileUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureIterator;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.build.feature.FeatureGraphGenerator;
import org.geotools.graph.build.line.LineStringGraphGenerator;
import org.geotools.graph.build.polygon.PolygonGraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.util.geom.GeometryUtil;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import sun.security.krb5.internal.KDCOptions;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Ola Sundell
 */
public class GeoToolsTest {
	public static void main(String[] args) throws IOException {
		// display a data store file chooser dialog for shapefiles
		File file = new File("data/shape/10m_ocean.shp");
		if (file == null) {
			return;
		}

		ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(file);
		SimpleFeatureSource featureSource = store.getFeatureSource();
		SimpleFeatureCollection collection = featureSource.getFeatures();
		NewWorld world = createWorld(collection);
		//determine what the relationship will be
//		PolygonGraphGenerator.PolygonRelationship rel = new PolygonGraphGenerator.PolygonRelationship() {
//
//			@Override
//			public boolean related(Polygon p1, Polygon p2) {
//				return p1.intersects(p2);
//			}
//
//			@Override
//			public boolean equal(Polygon p1, Polygon p2) {
//				return p1.equals(p2);
//			}
//		};
  //create the generator
//		PolygonGraphGenerator gg = new PolygonGraphGenerator(new BasicGraphBuilder(),rel);
//		FeatureGraphGenerator featureGraphGenerator = new FeatureGraphGenerator(gg);
//		LineStringGraphGenerator lineStringGraphGenerator = new LineStringGraphGenerator();
//		FeatureGraphGenerator featureGraphGenerator = new FeatureGraphGenerator(lineStringGraphGenerator);

		FeatureIterator iter = collection.features();

//		addPolygons(gg, iter);

		iter.close();

//		Graph graph = gg.getGraph();

		System.out.println("Done!");

//		// Create a map context and add our shapefile to it
//		MapContext map = new DefaultMapContext();
//		map.setTitle("Quickstart");
//		map.addLayer(featureSource, null);
//
//		// Now display the map
//		JMapFrame.showMap(map);
	}

	private static NewWorld createWorld(SimpleFeatureCollection collection) {
		int latitudeSize = 180;
		int longitudeSize = 360;
		NewWorld world = new NewWorld(latitudeSize, longitudeSize);
		world.setScale(1);
		GeometryFactory factory = new GeometryFactory();

		ArrayList<Geometry> list = new ArrayList<Geometry>();

		SimpleFeatureIterator iter = collection.features();
		while (iter.hasNext()) {
			SimpleFeature feature = iter.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			list.add(geometry);
		}
		iter.close();

		for (int i=0;i< latitudeSize;i++) {
			System.out.println(i);
			for (int j=0;j< longitudeSize;j++) {
				Point point = factory.createPoint(new Coordinate(j-179,89-i));
				for (Geometry geometry: list) {
					if (geometry.contains(point)) {
						world.getPointsAsArray()[i][j] = new kop.map.routecalculator.Point(i,j,i-90,j- latitudeSize);
						break;
					}
				}
			}
		}

		File file = new File("worldgeotools.txt");
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(world.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return world;
	}

	private static void addPolygons(PolygonGraphGenerator polygonGraphGenerator, FeatureIterator iter) {
		boolean multi=false;
		while (iter.hasNext()) {
			SimpleFeature feature = (SimpleFeature) iter.next();
			Object value = feature.getDefaultGeometry();
			if (value instanceof MultiPolygon) {
				addMultiPolygon(polygonGraphGenerator, (MultiPolygon) value);
			} else if (value instanceof Polygon) {
				polygonGraphGenerator.add(value);
			}
		}
	}

	private static void addMultiPolygon(PolygonGraphGenerator polygonGraphGenerator, MultiPolygon value) {
		MultiPolygon multiPolygon = (MultiPolygon) value;

		for (int i=0;i<multiPolygon.getNumGeometries();i++) {
			Geometry geometry = multiPolygon.getGeometryN(i);
			if (geometry instanceof MultiPolygon) {
				addMultiPolygon(polygonGraphGenerator, (MultiPolygon) geometry);
			} else {
				polygonGraphGenerator.add(geometry);
			}
		}
	}
}
