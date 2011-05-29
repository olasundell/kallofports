package kop.map.routecalculator;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.location.*;
import com.bbn.openmap.layer.shape.ShapeLayer;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.omGraphics.OMRect;
import com.bbn.openmap.proj.Projection;
import kop.ships.ModelSerializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
	ShapeLayer basicMapShape;
	MapBean mapBean;

	NewWorld points = null;
	List<Shape> shapeList;

	public RouteCalculator() {
		shapeList = new ArrayList<Shape>();
		mapBean = new MapBean();
		basicMapShape = createWorldLayer();
		mapBean.add(basicMapShape);
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
					e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

	public void drawRoute(double lat1, double lon1, double lat2, double lon2) {
		int i = points.reverseLat(lat1);
		int i1 = points.reverseLon(lon1);
		Point start = points.lats[i].longitudes[i1];
		int i2 = points.reverseLat(lat2);
		int i3 = points.reverseLon(lon2);
		Point goal = points.lats[i2].longitudes[i3];
		if (start==null || goal == null) {
			throw new NullPointerException("Either start or goal is null");
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
		ASRoute route = aStarUtil.aStar(start, goal, points);
		if (route!= null) {
			LocationLayer routeLayer = createRouteLayer(route);
			mapBean.add(routeLayer, 0);
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
		OMGraphicList list = basicMapShape.prepare();
		Iterator<OMGraphic> iterator = list.iterator();

		getShapes(iterator);

		Projection projection = basicMapShape.getProjection();

		points = world;

		ExecutorService service = Executors.newFixedThreadPool(3);
		ArrayList<WorldCreationWorker> workers = new ArrayList<WorldCreationWorker>();

		for (int i=0;i<points.lats.length;i++) {
			System.out.println("i is "+i);
			workers.add(new WorldCreationWorker(points, points.calcLat(i), i, projection, shapeList));
		}
		try {
			service.invokeAll(workers);
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return points;
	}

	/**
	 * Creates a list of Shapes used in isWater.
	 * @param iterator
	 */

	private void getShapes(Iterator<OMGraphic> iterator) {
		while (iterator.hasNext()) {
			OMGraphic next = iterator.next();
			if (next instanceof OMGraphicList) {
				getShapes(((OMGraphicList) next).iterator());
			}
			shapeList.add(next.getShape());
		}
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
	 * Creates the world map layer, from the shapefile. This ShapeLayer is used for all the graticule calculations.
	 * @return
	 */

	private ShapeLayer createWorldLayer() {
		ShapeLayer shapeLayer = new ShapeLayer();
		Properties shapeLayerProps = new Properties();
		shapeLayerProps.put("prettyName", "Political Solid");
		shapeLayerProps.put("lineColor", "000000");
		shapeLayerProps.put("fillColor", "1111FF");
		shapeLayerProps.put("shapeFile", "data/shape/10m_ocean.shp");
		shapeLayerProps.put("shapeindex", "data/shape/10m_ocean.ssx");
		shapeLayer.setProperties(shapeLayerProps);
		return shapeLayer;
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

	private class RouteLocationHandler extends AbstractLocationHandler {
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

			for (Point p: route.points) {
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
		RouteCalculator calc=null;
		try {
			calc = new RouteCalculator();
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		JFrame frame = new JFrame("Simple Map");
		frame.setSize(1024, 768);

		calc.drawPointLayer();
		calc.drawRoute(60, 0, 0, 75);

		MapBean mapBean = calc.getMapBean();
		frame.getContentPane().add(mapBean);
//		ModelSerializer.saveToFile("world.xml", World.class, obj);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		frame.setVisible(true);
	}

}
