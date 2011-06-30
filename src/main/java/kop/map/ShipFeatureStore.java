package kop.map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import kop.game.Game;
import kop.ships.model.ShipModel;
import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Ola Sundell
 */
public class ShipFeatureStore extends AbstractFeatureStore {
	ShipDataStore shipDataStore = new ShipDataStore();
	Logger logger;

	public ShipFeatureStore() {
		super();
		logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public DataStore getDataStore() {
		return shipDataStore;
	}

	@Override
	public void addFeatureListener(FeatureListener featureListener) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void removeFeatureListener(FeatureListener featureListener) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public SimpleFeatureType getSchema() {
		try {
			return shipDataStore.getSchema("");
		} catch (IOException e) {
			logger.error("Could not get schema from datastore", e);
		}
		return null;
	}

	public static class ShipDataStore extends AbstractDataStore {
		SimpleFeatureType type = null;
		Logger logger;

		public ShipDataStore() {
			super();
			logger = LoggerFactory.getLogger(this.getClass());

			try {
				type = DataUtilities.createType("Location",
//					"location:Point:srid=4326," // <- the geometry attribute: Point type
						"location:Point:srid=4326," + // <- the geometry attribute: Point type
								"name:String," + // <- a String attribute
								"number:Integer" // a number attribute
				);
			} catch (SchemaException e) {
				logger.error("Could not create SimpleFeatureType",e);
			}
		}

		@Override
		public String[] getTypeNames() throws IOException {
			return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public SimpleFeatureType getSchema(String s) throws IOException {
			return type;
		}

		@Override
		protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String s) throws IOException {
			return new ShipFeatureReader(type);
		}

		private class ShipFeatureReader implements SimpleFeatureReader {
			Iterator<ShipModel> iterator;
			private SimpleFeatureType featureType;
			private SimpleFeatureBuilder featureBuilder;
			private GeometryFactory geometryFactory;

			ShipFeatureReader(SimpleFeatureType type) {
				featureType = type;
				iterator =  Game.getInstance().getPlayerCompany().getShips().iterator();
				geometryFactory = new GeometryFactory();
				featureBuilder = new SimpleFeatureBuilder(ShipDataStore.this.type);
			}

			@Override
			public SimpleFeatureType getFeatureType() {
				return featureType;
			}

			@Override
			public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
				ShipModel ship = iterator.next();

				Point point = geometryFactory.createPoint(new Coordinate(ship.getCurrentPosition().getLongitude(), ship.getCurrentPosition().getLongitude()));
				featureBuilder.add(point);
				featureBuilder.add(ship.getName());
				featureBuilder.add(0);
				return featureBuilder.buildFeature(null);
			}

			@Override
			public boolean hasNext() throws IOException {
				return iterator.hasNext();
			}

			@Override
			public void close() throws IOException {
				iterator = null;
			}
		}
	}
}
