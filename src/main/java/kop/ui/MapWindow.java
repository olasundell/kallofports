package kop.ui;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.location.LocationHandler;
import com.bbn.openmap.layer.location.LocationLayer;
import com.bbn.openmap.layer.location.csv.CSVLocationHandler;
import com.bbn.openmap.layer.shape.ShapeLayer;
import kop.map.MapBeanFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/30/11
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapWindow {
	public static void main(String[] args) {
		// Create a Swing frame
		JFrame frame = new JFrame("Simple Map");

		// Size the frame appropriately
		frame.setSize(1024, 768);

		// Create a MapBean
		MapBean mapBean = new MapBeanFactory().createMapBean();

		// Add the map to the frame
		frame.getContentPane().add(mapBean);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Display the frame
		frame.setVisible(true);
	}

	private void doOldStuff() {
		// Create a ShapeLayer to show world political boundaries.
		// Set the properties of the layer. This assumes that the
		// datafiles "dcwpo-browse.shp" and "dcwpo-browse.ssx" are in
		// a path specified in the CLASSPATH variable. These files
		// are distributed with OpenMap and reside in the toplevel
		// "share" subdirectory.
		ShapeLayer shapeLayer = new ShapeLayer();
		Properties shapeLayerProps = new Properties();
		shapeLayerProps.put("prettyName", "Political Solid");
		shapeLayerProps.put("lineColor", "000000");
		shapeLayerProps.put("fillColor", "BDDE83");
		shapeLayerProps.put("shapeFile", "data/shape/dcwpo-browse.shp");
		shapeLayerProps.put("spatialIndex", "data/shape/dcwpo-browse.ssx");
		shapeLayer.setProperties(shapeLayerProps);

		LocationLayer locationLayer = new LocationLayer();
		Properties locationProps = new Properties();
		CSVLocationHandler locationHandler = new CSVLocationHandler();
		Properties csvProps = new Properties();
		LocationHandler[] handlers = { locationHandler };

		locationLayer.setLocationHandlers(handlers);

		csvProps.setProperty("locationFile","data/cities.csv");
		csvProps.setProperty("csvFileHasHeader","true");
		csvProps.setProperty("nameIndex","0");
		csvProps.setProperty("latIndex","5");
		csvProps.setProperty("lonIndex","4");
		csvProps.setProperty("showNames","false");
		csvProps.setProperty("showLocations","true");
		csvProps.setProperty("locationColor","FF0000");
		csvProps.setProperty("nameColor","008C54");
		locationHandler.setProperties(csvProps);

		String[] handlerNames = { "csvlocationhandler" };
		locationLayer.setLocationHandlerNames(handlerNames);

		locationProps.setProperty("loclayer.class","com.bbn.openmap.layer.location.LocationLayer");
		locationProps.setProperty("loclayer.useDeclutter","false");
		locationProps.setProperty("loclayer.declutterMatrix","com.bbn.openmap.layer.DeclutterMatrix");
		locationProps.setProperty("loclayer.addToBeanContext","true");
		locationProps.setProperty("loclayer.locationHandlers","csvlocationhandler");
		locationProps.setProperty("csvlocationhandler.class","com.bbn.openmap.layer.location.csv.CSVLocationHandler");

		locationLayer.setProperties(locationProps);

	//		mapBean.add(locationLayer);
		// Add the political layer to the map
//		mapBean.add(shapeLayer);
	}

}
