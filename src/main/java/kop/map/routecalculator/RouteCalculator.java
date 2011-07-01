package kop.map.routecalculator;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.location.*;
import com.bbn.openmap.layer.shape.ShapeLayer;
import com.bbn.openmap.omGraphics.OMRect;
import kop.ports.NoRouteFoundException;
import kop.ships.ModelSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * This is a util and test class used to create the discrete approximation of a world, and to display ASroutes in a JFrame.
 * It is multithreaded and uses WorldCreationWorkers in a thread pool.
 * TODO serialise/deserialise the graticuled world with something else than XML (I went into that with open eyes, mind you)
 * TODO low priority: refactor the duplicated LocationHandler code.
 */
public class RouteCalculator {
//	ShapeLayer basicMapShape;
	MapBean mapBean;
	Logger logger;

	NewWorld points = null;
	List<Shape> shapeList;

	public RouteCalculator() {
		shapeList = new ArrayList<Shape>();
		mapBean = new MapBean();
		logger = LoggerFactory.getLogger(this.getClass());
//		basicMapShape = createWorldLayer();
//		mapBean.add(basicMapShape);
	}

	/**
	 * Draws the PointLayer, which overlays the world map with graticule points.
	 */
	public void drawPointLayer() {
		if (points == null) {
			// TODO serialise
//			try {
//				points = readWorldFromFile();
//			} catch (Exception e) {
				NewWorld w = calculateWorld(NewWorld.getDefaultLatitudeSize(), NewWorld.getDefaultLongitudeSize());
				try {
					ModelSerializer.saveToFile("newworld.xml", NewWorld.class, w);
				} catch (Exception e1) {
					logger.error("Failure to save world to XML file.",e1);
				}
				points = w;
//			}
		}
		LocationLayer pointLayer = createPointLayer();
		mapBean.add(pointLayer,0);

	}

	/**
	 * Calculates and draws a route between two lat/lon pairs.
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 */

	public void drawRoute(double lat1, double lon1, double lat2, double lon2) throws CouldNotFindPointException {
		int i = points.reverseLat(lat1);
		int i1 = points.reverseLon(lon1);
		Point start = points.lats[i].longitudes[i1];
		int i2 = points.reverseLat(lat2);
		int i3 = points.reverseLon(lon2);
		Point goal = points.lats[i2].longitudes[i3];
		if (start==null || goal == null) {
			throw new CouldNotFindPointException("Either start or goal is null");
		}
		drawRoute(start, goal);
	}

	/**
	 * Draws a route between two Points.
	 * @param start
	 * @param goal
	 */

	private void drawRoute(Point start, Point goal) {
		AStarUtil aStarUtil = new AStarUtil();
		ASRoute route = null;
		try {
			route = aStarUtil.aStar(start, goal, points);
			LocationLayer routeLayer = createRouteLayer(route);
			mapBean.add(routeLayer, 0);
		} catch (NoRouteFoundException e) {
			logger.error("No route found, thus we cannot create a map layer.",e);
		} catch (CouldNotFindPointException e) {
			logger.error("No route found, thus we cannot create a map layer.",e);
		}
	}

	/**
	 * TODO replace read from file with something more sane than XML.
	 * @return
	 * @throws Exception
	 */
	public NewWorld readWorldFromFile() throws Exception {
		return (NewWorld) ModelSerializer.readFromFile(new File("newworld.xml").toURI().toURL(), NewWorld.class);
	}

	/**
	 * Creates route layer from an ASRoute.
	 * @param route
	 * @return
	 */

	private LocationLayer createRouteLayer(ASRoute route) {
		LocationLayer locationLayer = new LocationLayer();
		Properties locationProps = new Properties();
		String locationHandlerName = "routelocationhandler";
		LocationHandler locationHandler = new RouteLocationHandler(route);
		LocationHandler[] handlers = {locationHandler};

		locationLayer.setLocationHandlers(handlers);

		String[] handlerNames = {locationHandlerName};
		locationLayer.setLocationHandlerNames(handlerNames);

		Properties handlerProperties = createHandlerProperties(locationHandlerName);

		locationHandler.setProperties(handlerProperties);

		locationProps.setProperty("loclayer.class","com.bbn.openmap.layer.location.LocationLayer");
		locationProps.setProperty("loclayer.useDeclutter","false");
		locationProps.setProperty("loclayer.declutterMatrix","com.bbn.openmap.layer.DeclutterMatrix");
		locationProps.setProperty("loclayer.addToBeanContext","true");
		locationProps.setProperty("loclayer.locationHandlers",locationHandlerName);
		locationProps.setProperty(locationHandlerName + ".class","kop.map.routecalculator.RouteLocationHandler");

		locationLayer.setProperties(locationProps);

		return locationLayer;
	}

	/**
	 * @deprecated use calculateWorld(NewWorld world) instead
	 * @return
	 */
	public NewWorld calculateWorld() {
		return calculateWorld(NewWorld.getDefaultLatitudeSize(),NewWorld.getDefaultLongitudeSize());
	}

	/**
	 * @deprecated use calculateWorld(NewWorld world) instead
	 * @param latitudeSize
	 * @param longitudeSize
	 * @return
	 */
	public NewWorld calculateWorld(int latitudeSize, int longitudeSize) {
		return calculateWorld(new NewWorld(latitudeSize, longitudeSize));
	}

	/**
	 * Calculates world based on the settings in the parameter instance.
	 * @param world
	 * @return
	 */
	public NewWorld calculateWorld(NewWorld world) {
		points = world;

		ExecutorService service = Executors.newFixedThreadPool(3);
		ArrayList<WorldCreationWorker> workers = new ArrayList<WorldCreationWorker>();

		// TODO dependency inject these.
//		WaterVerifier waterVerifier = new OpenMapWaterVerifier("data/shape/10m_ocean.shp");
//		WaterVerifier waterVerifier = new GeoToolsWaterVerifier("data/shape/10m_ocean.shp");
		WaterVerifier waterVerifier = world.getWaterVerifier();

		for (int i=0;i<points.lats.length;i++) {
			logger.debug("i is "+i);
			workers.add(new WorldCreationWorker(points, points.calcLat(i), i, (WaterVerifier)waterVerifier.clone()));
		}

		try {
			service.invokeAll(workers);
		} catch (InterruptedException e) {
			logger.error("Interrupted!", e);
		}

		return points;
	}

	/**
	 * Creates point layer and handler.
	 * @return
	 */

	private LocationLayer createPointLayer() {
		LocationLayer locationLayer = new LocationLayer();
		Properties locationProps = new Properties();
		String locationHandlerName = "pointlocationhandler";
		LocationHandler locationHandler = new PointLocationHandler();
		LocationHandler[] handlers = {locationHandler};

		locationLayer.setLocationHandlers(handlers);

		String[] handlerNames = {locationHandlerName};
		locationLayer.setLocationHandlerNames(handlerNames);

		Properties handlerProperties = createHandlerProperties(locationHandlerName);

		locationHandler.setProperties(handlerProperties);

		locationProps.setProperty("loclayer.class","com.bbn.openmap.layer.location.LocationLayer");
		locationProps.setProperty("loclayer.useDeclutter","false");
		locationProps.setProperty("loclayer.declutterMatrix","com.bbn.openmap.layer.DeclutterMatrix");
		locationProps.setProperty("loclayer.addToBeanContext","true");
		locationProps.setProperty("loclayer.locationHandlers","pointlocationhandler");
		locationProps.setProperty(locationHandlerName + ".class","kop.map.PointLocationHandler");

		locationLayer.setProperties(locationProps);

		return locationLayer;
	}

	/**
	 * Creates handler properties.
	 * @param locationHandlerName
	 * @return
	 */
	private Properties createHandlerProperties(String locationHandlerName) {
		Properties portLocProps = new Properties();
//		portLocProps.setProperty("portlocationhandler","locationColor=FF0000");
		portLocProps.setProperty(locationHandlerName,"nameColor=008C54");
		portLocProps.setProperty(locationHandlerName,"showNames=false");
		portLocProps.setProperty(locationHandlerName,"showLocations=true");
		portLocProps.setProperty(locationHandlerName,"override=true");
		return portLocProps;
	}
	/**
	 * isWater checks if a java.awt.Point p is contained by any shapes, and if so, returns true.
	 * @deprecated the relevant implementation of this method is now in the WorldCreationWorker class.
	 * @param p
	 * @return
	 */
	private boolean isWater(java.awt.Point p) {
		for (Shape s: shapeList) {
//			if (s!=null && s.intersects(i, j , i , j)) {
			if (s!=null && s.contains(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @deprecated this isn't used at all anymore, and we should remove it.
	 * @param i
	 * @param j
	 * @return
	 */
	private boolean isWater(double i, double j) {
		for (Shape s: shapeList) {
//			if (s!=null && s.intersects(i, j , i , j)) {
			if (s!=null && s.contains(i, j)) {
				return true;
			}
		}
		return false;
	}

	public MapBean getMapBean() {
			return mapBean;
	}

	/**
	 * LocationHandler for the graticule layer.
	 */
	private class PointLocationHandler extends AbstractLocationHandler {
		@Override
		public Vector get(float v, float v1, float v2, float v3, Vector vector) {
			Vector retVector;

			if (vector == null) {
				retVector = new Vector();
			} else {
				retVector = vector;
			}

			for (NewWorld.LatitudeArr arr: points.lats) {
				for (Point p: arr.longitudes) {
					if (p!=null) {
						OMRect rect = new OMRect(p.getCoord().getLatitude(),
								p.getCoord().getLongitude(),
								-1,-1,1,1);
						rect.setFillPaint(Color.red);
						rect.setLinePaint(Color.red);
						rect.setVisible(true);

						Location location = new BasicLocation(p.getCoord().getLatitude(),
								p.getCoord().getLongitude(),
								"",
								rect);


						location.setShowName(false);
						location.setShowLocation(true);
						location.setLocationHandler(this);

						//noinspection unchecked
						retVector.add(location);
					}
				}
			}

			return retVector;
		}

		@Override
		public void reloadData() {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}

	/**
	 * LocationHandler for the route layer.
	 */

	private static class RouteLocationHandler extends AbstractLocationHandler {
		private ASRoute route;

		public RouteLocationHandler(ASRoute route) {
			this.route = route;
		}

		@Override
		public Vector get(float v, float v1, float v2, float v3, Vector vector) {
			Vector retVector;

			if (vector == null) {
				retVector = new Vector();
			} else {
				retVector = vector;
			}

			for (Point p: route.getPoints()) {
				if (p!=null) {
					OMRect rect = new OMRect(p.getCoord().getLatitude(),
							p.getCoord().getLongitude(),
							-1,-1,1,1);
					rect.setFillPaint(Color.white);
					rect.setLinePaint(Color.white);
					rect.setVisible(true);

					Location location = new BasicLocation(p.getCoord().getLatitude(),
							p.getCoord().getLongitude(),
							"",
							rect);


					location.setShowName(false);
					location.setShowLocation(true);
					location.setLocationHandler(this);

					//noinspection unchecked
					retVector.add(location);
				}
			}

			return retVector;
		}

		@Override
		public void reloadData() {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}

	/**
	 * Creates a JFrame with a mapBean in it and calculates graticule points based on a shapefile.
	 * @param args
	 */

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(RouteCalculator.class);
		RouteCalculator calc=null;
		try {
			calc = new RouteCalculator();
		} catch (Exception e) {
			logger.error("Could not create route calculator", e);
			return;
		}

		NewWorld newWorld = new NewWorld(720, 1440);
		newWorld.setScale(4);

		NewWorld world = calc.calculateWorld(newWorld);
		String pathname = "world.txt";
		File file = new File(pathname);
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(world.toString());
			writer.close();
		} catch (IOException e) {
			logger.error(String.format("Could not write to file %s", pathname),e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("Could not close writer.",e);
				}
			}
		}
//		JFrame frame = new JFrame("Simple Map");
//		frame.setSize(1024, 768);
//
//		calc.drawPointLayer();
//		calc.drawRoute(60, 0, 0, 75);
//
//		MapBean mapBean = calc.getMapBean();
//		frame.getContentPane().add(mapBean);
////		ModelSerializer.saveToFile("world.xml", World.class, obj);
//
//		frame.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				System.exit(0);
//			}
//		});
//
//		frame.setVisible(true);
	}

}
