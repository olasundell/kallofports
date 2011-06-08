package kop.map.routecalculator;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.layer.shape.ShapeLayer;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;
import com.bbn.openmap.proj.Projection;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class OpenMapWaterVerifier implements WaterVerifier {
	private List<Shape> shapeList;
	private Projection projection;

	public void setupInstance(String shapeFileName) {
		// we need the map bean to get a Projection.
		MapBean mapBean = new MapBean();
		ShapeLayer basicMapShape = createWorldLayer(shapeFileName);
		mapBean.add(basicMapShape);

		shapeList = new ArrayList<Shape>();
		projection = basicMapShape.getProjection();
		getShapes(basicMapShape.prepare().iterator());
	}

	public OpenMapWaterVerifier() {
	}

	/**
	 * Creates the world map layer, from the shapefile. This ShapeLayer is used for all the graticule calculations.
	 * @return
	 * @param shapeFileName
	 */

	private ShapeLayer createWorldLayer(String shapeFileName) {
		ShapeLayer shapeLayer = new ShapeLayer();
		Properties shapeLayerProps = new Properties();
		shapeLayerProps.put("prettyName", "Political Solid");
		shapeLayerProps.put("lineColor", "000000");
		shapeLayerProps.put("fillColor", "1111FF");
		shapeLayerProps.put("shapeFile", shapeFileName);
		shapeLayerProps.put("shapeindex", "data/shape/10m_ocean.ssx");
		shapeLayer.setProperties(shapeLayerProps);
		return shapeLayer;
	}


	@Override
	public boolean isWater(double lat, double lon) {
		return isWater(projection.forward((float)lat,(float)lon));  //To change body of implemented methods use File | Settings | File Templates.
	}
	public boolean isWater(java.awt.Point p) {
		for (Shape s : shapeList) {
			if (s != null && s.contains(p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();	//To change body of overridden methods use File | Settings | File Templates.
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return o;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OpenMapWaterVerifier verifier = (OpenMapWaterVerifier) o;

		if (shapeList != null ? !shapeList.equals(verifier.shapeList) : verifier.shapeList != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return shapeList != null ? shapeList.hashCode() : 0;
	}

}