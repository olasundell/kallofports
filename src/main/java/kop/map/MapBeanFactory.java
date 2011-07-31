package kop.map;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.location.LocationHandler;
import com.bbn.openmap.layer.location.LocationLayer;
import com.bbn.openmap.layer.shape.ShapeLayer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import kop.game.Game;
import kop.map.routecalculator.NewWorld;
import kop.ports.Port;
import kop.ports.PortMap;
import kop.ports.PortsOfTheWorld;
import kop.ships.model.ShipModel;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.*;
import org.geotools.styling.Stroke;
import org.geotools.swing.JMapPane;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Creates a map bean.
 * TODO add route layer or add routes to the ship layer.
 */
public class MapBeanFactory {

	public static final String MAP_LAYER = "Map layer";
	public static final String PORT_LAYER = "Port layer";
	public static final String SHIP_LAYER = "Ship layer";
	private static final String PORTLOCATIONHANDLER = "portlocationhandler";
	private static final String SHIPLOCATIONHANDLER = "shiplocationhandler";

	Logger logger;

	public MapBeanFactory() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public JMapPane createGeoToolsBean() {
		FileDataStore store = null;
		SimpleFeatureSource featureSource = null;

		try {
			store = FileDataStoreFinder.getDataStore(new File(NewWorld.SHAPE_FILENAME));
			featureSource = store.getFeatureSource();
		} catch (IOException e) {
			logger.error("Could not create feature source", e);
		}

		// Create a map context and add our shapefile to it
		MapContext mapContext = new DefaultMapContext();
		mapContext.setTitle("World mapContext");
		mapContext.addLayer(new MapLayer(featureSource, createPolygonStyle(), MAP_LAYER));

		SimpleFeatureType TYPE = null;

		try {
			TYPE = DataUtilities.createType("Location",
//					"location:Point:srid=4326," // <- the geometry attribute: Point type
					"location:Point:srid=4326," + // <- the geometry attribute: Point type
							"name:String," + // <- a String attribute
							"number:Integer" // a number attribute
			);
		} catch (SchemaException e) {
			logger.error("Could not create SimpleFeatureType",e);
		}
		com.vividsolutions.jts.geom.GeometryFactory geometryFactory = new com.vividsolutions.jts.geom.GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

//		SimpleFeatureCollection waterPointCollection = createWaterPointCollection(geometryFactory, featureBuilder);
//		mapContext.addLayer(waterPointCollection, createPointStyle());

		SimpleFeatureCollection portPointCollection = createPortPointCollection(geometryFactory, featureBuilder);
		mapContext.addLayer(new MapLayer(portPointCollection, createPointStyle(Color.BLACK, Color.BLACK), PORT_LAYER));

//		SimpleFeatureCollection shipPointCollection = createShipPointCollection(geometryFactory, featureBuilder);
//		MapLayer shipLayer = new MapLayer(shipPointCollection, createPointStyle(Color.RED, Color.YELLOW), SHIP_LAYER);
		MapLayer shipLayer = new MapLayer(new ShipFeatureStore(), createPointStyle(Color.RED, Color.YELLOW), SHIP_LAYER);
		mapContext.addLayer(shipLayer);

		JMapPane mapPane = new JMapPane(new StreamingRenderer(),mapContext);
//		mapPane.setMapContext();

		return mapPane;
	}
	     /**
     * Create a Style to draw polygon features with a thin blue outline and
     * a cyan fill
     */
    private Style createPolygonStyle() {
		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
		org.opengis.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.DARK_GRAY),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

	private SimpleFeatureCollection createShipPointCollection(GeometryFactory geometryFactory, SimpleFeatureBuilder featureBuilder) {
		SimpleFeatureCollection collection = FeatureCollections.newCollection();

		for (ShipModel ship: Game.getInstance().getPlayerCompany().getShips()) {
			Point point = geometryFactory.createPoint(new Coordinate(ship.getCurrentPosition().getLongitude(), ship.getCurrentPosition().getLongitude()));
			featureBuilder.add(point);
			featureBuilder.add(ship.getName());
			featureBuilder.add(0);
			collection.add(featureBuilder.buildFeature(null));
		}

		return collection;
	}

	private SimpleFeatureCollection createPortPointCollection(GeometryFactory geometryFactory, SimpleFeatureBuilder featureBuilder) {
		SimpleFeatureCollection collection = FeatureCollections.newCollection();
		PortMap map = null;
		try {
			map = PortsOfTheWorld.getPorts();
		} catch (Exception e) {
			logger.error("Could not get port collection", e);
			return null;
		}

		for (Map.Entry<String, Port> entry: map.entrySet()) {
			Point point = geometryFactory.createPoint(new Coordinate(entry.getValue().getLatitude(), entry.getValue().getLongitude()));
			featureBuilder.add(point);
			featureBuilder.add(entry.getKey());
			featureBuilder.add(0);
			collection.add(featureBuilder.buildFeature(null));
		}

		return collection;
	}

	private SimpleFeatureCollection createWaterPointCollection(GeometryFactory geometryFactory, SimpleFeatureBuilder featureBuilder) {
		SimpleFeatureCollection collection = FeatureCollections.newCollection();
		NewWorld world = Game.getInstance().getWorld();
		kop.map.routecalculator.Point[][] worldArr = world.getPointsAsArray();

		for (int i=0;i<worldArr.length;i++) {
			for (int j=0;j<worldArr[i].length;j++) {
				if (worldArr[i][j]==null) {
					continue;
				}
		                    /* Longitude (= x coord) first ! */
				Point point = geometryFactory.createPoint(new Coordinate(worldArr[i][j].getLat(), worldArr[i][j].getLon()));

				featureBuilder.add(point);
				featureBuilder.add("");
				featureBuilder.add(0);
				SimpleFeature feature = featureBuilder.buildFeature(null);
				collection.add(feature);
			}
		}
		return collection;
	}

	/**
	 * Create a Style to draw point features as circles with blue outlines
	 * and cyan fill
	 * @param stroke stroke colour
	 * @param fill fill colour
	 * @return a point style
	 */
	private org.geotools.styling.Style createPointStyle(Color stroke, Color fill) {
		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
		org.opengis.filter.FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

		Graphic gr = styleFactory.createDefaultGraphic();

		Mark mark = styleFactory.getCircleMark();

		mark.setStroke(styleFactory.createStroke(
				filterFactory.literal(stroke), filterFactory.literal(1)));

		mark.setFill(styleFactory.createFill(filterFactory.literal(fill)));

		gr.graphicalSymbols().clear();
		gr.graphicalSymbols().add(mark);
		gr.setSize(filterFactory.literal(5));

		/*
				 * Setting the geometryPropertyName arg to null signals that we want to
				 * draw the default geomettry of features
				 */
		PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add((Symbolizer) sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
		org.geotools.styling.Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	public MapBean createOpenMapBean() {
		MapBean mapBean = new MapBean();

		ShapeLayer politicalMapLayer = createPoliticalMapLayer();
		LocationLayer portMapLayer = createPortOrShipLayer(new PortLocationHandler(), PORTLOCATIONHANDLER);
		LocationLayer shipLayer = createPortOrShipLayer(new ShipLocationHandler(), SHIPLOCATIONHANDLER);
		mapBean.add(shipLayer);
		mapBean.add(portMapLayer);
		mapBean.add(politicalMapLayer);

		return mapBean;
	}

	private LocationLayer createPortOrShipLayer(LocationHandler locationHandler, String portlocationhandler) {
		LocationLayer locationLayer = new LocationLayer();
		Properties locationProps = new Properties();
		LocationHandler[] handlers = {locationHandler};

		locationLayer.setLocationHandlers(handlers);

		String[] handlerNames = {portlocationhandler};
		locationLayer.setLocationHandlerNames(handlerNames);

		Properties handlerProperties = createHandlerProperties();

		locationHandler.setProperties(handlerProperties);

		locationProps.setProperty("loclayer.class","com.bbn.openmap.layer.location.LocationLayer");
		locationProps.setProperty("loclayer.useDeclutter","false");
		locationProps.setProperty("loclayer.declutterMatrix","com.bbn.openmap.layer.DeclutterMatrix");
		locationProps.setProperty("loclayer.addToBeanContext","true");
		locationProps.setProperty("loclayer.locationHandlers",portlocationhandler);
		locationProps.setProperty(portlocationhandler + ".class","kop.map.PortLocationHandler");

		locationLayer.setProperties(locationProps);

		return locationLayer;
	}

	private Properties createHandlerProperties() {
		Properties portLocProps = new Properties();
//		portLocProps.setProperty("portlocationhandler","locationColor=FF0000");
		portLocProps.setProperty(PORTLOCATIONHANDLER,"nameColor=008C54");
		portLocProps.setProperty(PORTLOCATIONHANDLER,"showNames=false");
		portLocProps.setProperty(PORTLOCATIONHANDLER,"showLocations=true");
		portLocProps.setProperty(PORTLOCATIONHANDLER,"override=true");
		return portLocProps;
	}

	private ShapeLayer createPoliticalMapLayer() {
		ShapeLayer shapeLayer = new ShapeLayer();
		Properties shapeLayerProps = new Properties();
		shapeLayerProps.put("prettyName", "Political Solid");
		shapeLayerProps.put("lineColor", "000000");
		shapeLayerProps.put("fillColor", "BDDE83");
		shapeLayerProps.put("shapeFile", "data/shape/dcwpo-browse.shp");
		shapeLayerProps.put("spatialIndex", "data/shape/dcwpo-browse.ssx");
		shapeLayer.setProperties(shapeLayerProps);
		return shapeLayer;
	}
}
