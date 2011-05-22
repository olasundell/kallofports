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
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/15/11
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouteCalculator {
	ShapeLayer basicMapShape;
	MapBean mapBean;

	NewWorld points = null;
	List<Shape> shapeList;

	RouteCalculator() {
		shapeList = new ArrayList<Shape>();
		mapBean = new MapBean();
		basicMapShape = createWorldLayer();
		mapBean.add(basicMapShape);
	}

	public void drawPointLayer() {
		if (points == null) {
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

	private void drawRoute(Point start, Point goal) {
		AStarUtil aStarUtil = new AStarUtil();
		ASRoute route = aStarUtil.aStar(start, goal, points);
		if (route!= null) {
			LocationLayer routeLayer = createRouteLayer(route);
			mapBean.add(routeLayer, 0);
		}
	}

	public NewWorld readWorldFromFile() throws Exception {
		return (NewWorld) ModelSerializer.readFromFile(new File("newworld.xml").toURI().toURL(), NewWorld.class);
	}


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

	public NewWorld calculateWorld() {
		return calculateWorld(NewWorld.getDefaultLatitudeSize(),NewWorld.getDefaultLongitudeSize());
	}

	public NewWorld calculateWorld(int latitudeSize, int longitudeSize) {
		return calculateWorld(new NewWorld(latitudeSize, longitudeSize));
	}

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
