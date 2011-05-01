package kop.map;

import com.bbn.openmap.layer.location.AbstractLocationHandler;
import com.bbn.openmap.layer.location.BasicLocation;
import com.bbn.openmap.layer.location.Location;
import com.bbn.openmap.omGraphics.OMRect;
import kop.game.Game;
import kop.ports.Port;
import kop.ports.PortMap;
import kop.ports.PortsOfTheWorld;
import kop.ships.ShipModel;

import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 5/1/11
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class PortLocationHandler extends AbstractLocationHandler {
	@Override
	public Vector get(float v, float v1, float v2, float v3, Vector vector) {
		Vector retVector;

		if (vector == null) {
			retVector = new Vector();
		} else {
			retVector = vector;
		}

		PortMap ports = PortsOfTheWorld.getPorts();

		for (Port port: ports.values()) {

			if (port.getLatitude() != null && port.getLongitude() != null) {
				OMRect rect = new OMRect((float)port.getLatitude().getCoordinate(),
						(float)port.getLongitude().getCoordinate(),
						-1,-1,1,1);
				rect.setFillPaint(Color.red);
				rect.setLinePaint(Color.red);
				rect.setVisible(true);

				Location location = new BasicLocation((float)port.getLatitude().getCoordinate(),
						(float)port.getLongitude().getCoordinate(),
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
