package kop.map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import kop.game.Game;
import kop.ships.model.ShipModel;
import org.geotools.data.*;
import org.geotools.data.memory.CollectionSource;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Ola Sundell
 */
public class ShipFeatureStore extends AbstractFeatureStore {
	ShipDataStore shipDataStore = new ShipDataStore();

	public ShipFeatureStore() {
		super();
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
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
	}

	public static class ShipDataStore extends AbstractDataStore {
		SimpleFeatureType TYPE = null;
		public ShipDataStore() {
			super();

			try {
				TYPE = DataUtilities.createType("Location",
//					"location:Point:srid=4326," // <- the geometry attribute: Point type
						"location:Point:srid=4326," + // <- the geometry attribute: Point type
								"name:String," + // <- a String attribute
								"number:Integer" // a number attribute
				);
			} catch (SchemaException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}

		@Override
		public String[] getTypeNames() throws IOException {
			return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public SimpleFeatureType getSchema(String s) throws IOException {
			return TYPE;
		}

		@Override
		protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String s) throws IOException {
			return new ShipFeatureReader(TYPE);
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
				featureBuilder = new SimpleFeatureBuilder(TYPE);
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
