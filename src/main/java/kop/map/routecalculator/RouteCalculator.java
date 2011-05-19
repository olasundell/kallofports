package kop.map.routecalculator;

import com.bbn.openmap.LatLonPoint;
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

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/15/11
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCalculator {
	private static float scale = 2;
	ShapeLayer world;
	MapBean mapBean;

	protected static final int SOUTH_OFFSET = 20;
	protected static final int NORTH_OFFSET = 20;

	private int LATITUDE_SIZE = (int) ((180 - NORTH_OFFSET - SOUTH_OFFSET)* scale);
	private int LONGITUDE_SIZE = (int) (360* scale);
	Point[][] points = new Point[LATITUDE_SIZE][LONGITUDE_SIZE];
	List<Shape> shapeList;

	RouteCalculator() throws Exception {
		shapeList = new ArrayList<Shape>();
		mapBean = new MapBean();
		world = createWorldLayer();

//		LocationLayer pointLayer = createPointLayer();
//
//		mapBean.add(pointLayer);
		mapBean.add(world);

//		drawPointLayer();
//
//		World obj = new World(points);
//		ModelSerializer.saveToFile("world.xml", World.class, obj);

		World obj2 = (World) ModelSerializer.readFromFile(new File("world.xml").toURI().toURL(), World.class);
		points = obj2.getPointsAsArray();

		AStarUtil aStarUtil = new AStarUtil();
		Point start = points[reverseLat(60)][reverseLon(0)];
		Point goal = points[reverseLat(0)][reverseLon(75)];
		AStarUtil.ASRoute route = aStarUtil.aStar(start, goal, points);
		LocationLayer routeLayer = createRouteLayer(route);
		mapBean.add(routeLayer, 0);
	}

	private LocationLayer createRouteLayer(AStarUtil.ASRoute route) {
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

	public void drawPointLayer() {
		OMGraphicList list = world.prepare();
		Iterator<OMGraphic> iterator = list.iterator();

		//	pointLayer.prepare();

		// make recursive.
		getShapes(iterator);

		float lat;
		float lon;

		Projection projection = world.getProjection();

		for (int i=0;i<points.length;i++) {
			System.out.println("i is "+i);
			for (int j=0;j<points[i].length;j++) {
				lat = calcLat(i);
				lon = calcLon(j);
				if (isWater(projection.forward(lat, lon))) {
					points[i][j] = new Point(i,j,lat,lon);
				}
			}
		}
	}

	protected static int reverseLat(float lat) {
		return Math.abs(Math.round((lat * scale - 90)));
	}

	protected static int reverseLon(float lon) {
		return Math.abs(Math.round((lon * scale - 180)));
	}

	protected static float calcLon(int j) {
		return j / scale - 180;
	}

	protected static float calcLat(int i) {
		return 90 - NORTH_OFFSET - i/scale;
	}

	public static float getScale() {
		return scale;
	}

	public static void setScale(float scale) {
		RouteCalculator.scale = scale;
	}

	private void getShapes(Iterator<OMGraphic> iterator) {
		while (iterator.hasNext()) {
			OMGraphic next = iterator.next();
			if (next instanceof OMGraphicList) {
				getShapes(((OMGraphicList) next).iterator());
			}
			shapeList.add(next.getShape());
		}
	}

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

	private Properties createHandlerProperties(String locationHandlerName) {
		Properties portLocProps = new Properties();
//		portLocProps.setProperty("portlocationhandler","locationColor=FF0000");
		portLocProps.setProperty(locationHandlerName,"nameColor=008C54");
		portLocProps.setProperty(locationHandlerName,"showNames=false");
		portLocProps.setProperty(locationHandlerName,"showLocations=true");
		portLocProps.setProperty(locationHandlerName,"override=true");
		return portLocProps;
	}

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

	private boolean isWater(java.awt.Point p) {
		for (Shape s: shapeList) {
//			if (s!=null && s.intersects(i, j , i , j)) {
			if (s!=null && s.contains(p)) {
				return true;
			}
		}
		return false;
	}
	private boolean isWater(double i, double j) {
		for (Shape s: shapeList) {
//			if (s!=null && s.intersects(i, j , i , j)) {
			if (s!=null && s.contains(i, j)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		RouteCalculator calc=null;
		try {
			calc = new RouteCalculator();
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		// Create a Swing frame
		JFrame frame = new JFrame("Simple Map");

		// Size the frame appropriately
		frame.setSize(1024, 768);

		// Add the map to the frame
		MapBean mapBean1 = calc.getMapBean();
		frame.getContentPane().add(mapBean1);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		frame.setVisible(true);
	}

	public MapBean getMapBean() {
			return mapBean;
	}

	private class PointLocationHandler extends AbstractLocationHandler {
		@Override
		public Vector get(float v, float v1, float v2, float v3, Vector vector) {
			Vector retVector;

			if (vector == null) {
				retVector = new Vector();
			} else {
				retVector = vector;
			}

			for (Point[] arr: points) {
				for (Point p: arr) {
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

	private class RouteLocationHandler extends AbstractLocationHandler {
		private AStarUtil.ASRoute route;

		public RouteLocationHandler(AStarUtil.ASRoute route) {
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

			for (LatLonPoint p: route.points) {
				if (p!=null) {
					OMRect rect = new OMRect(p.getLatitude(),
							p.getLongitude(),
							-1,-1,1,1);
					rect.setFillPaint(Color.white);
					rect.setLinePaint(Color.white);
					rect.setVisible(true);

					Location location = new BasicLocation(p.getLatitude(),
							p.getLongitude(),
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

}
