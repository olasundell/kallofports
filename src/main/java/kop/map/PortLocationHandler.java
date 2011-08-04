package kop.map;

import com.bbn.openmap.layer.location.AbstractLocationHandler;
import com.bbn.openmap.layer.location.BasicLocation;
import com.bbn.openmap.layer.location.Location;
import com.bbn.openmap.omGraphics.OMRect;
import kop.ports.Port;
import kop.ports.PortMap;
import kop.ports.PortsOfTheWorld;
import kop.serialization.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Vector;

/**
 * LocationHandler for the mapBean.
 */
public class PortLocationHandler extends AbstractLocationHandler {
	private Logger logger;
	PortLocationHandler() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	@Override
	public Vector get(float v, float v1, float v2, float v3, Vector vector) {
		Vector retVector;

		if (vector == null) {
			retVector = new Vector();
		} else {
			retVector = vector;
		}

		PortMap ports = null;
		try {
			ports = PortsOfTheWorld.getPorts();
		} catch (SerializationException e) {
			logger.error("Could not get port collection", e);
			return null;
		}

		for (Port port: ports.values()) {

			if (port.getLatitude() != 0.0 && port.getLongitude() != 0.0) {
				OMRect rect = new OMRect((float)port.getLatitude(),
						(float)port.getLongitude(),
						-1,-1,1,1);
				rect.setFillPaint(Color.blue);
				rect.setLinePaint(Color.blue);
				rect.setVisible(true);

				Location location = new BasicLocation((float)port.getLatitude(),
						(float)port.getLongitude(),
						port.getName(),
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

	}
}
