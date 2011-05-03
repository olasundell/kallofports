package kop.map;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.location.LocationHandler;
import com.bbn.openmap.layer.location.LocationLayer;
import com.bbn.openmap.layer.shape.ShapeLayer;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/30/11
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapBeanFactory {
	public MapBeanFactory() {

	}

	public MapBean createMapBean() {
		MapBean mapBean = new MapBean();

		ShapeLayer politicalMapLayer = createPoliticalMapLayer();
		LocationLayer portMapLayer = createPortOrShipLayer(new PortLocationHandler(), "portlocationhandler");
		LocationLayer shipLayer = createPortOrShipLayer(new ShipLocationHandler(), "shiplocationhandler");
		mapBean.add(shipLayer);
//		mapBean.add(portMapLayer);
//		mapBean.add(politicalMapLayer);

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
		locationProps.setProperty("loclayer.locationHandlers","portlocationhandler");
		locationProps.setProperty(portlocationhandler + ".class","kop.map.PortLocationHandler");

		locationLayer.setProperties(locationProps);

		return locationLayer;
	}

	private Properties createHandlerProperties() {
		Properties portLocProps = new Properties();
//		portLocProps.setProperty("portlocationhandler","locationColor=FF0000");
		portLocProps.setProperty("portlocationhandler","nameColor=008C54");
		portLocProps.setProperty("portlocationhandler","showNames=false");
		portLocProps.setProperty("portlocationhandler","showLocations=true");
		portLocProps.setProperty("portlocationhandler","override=true");
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
