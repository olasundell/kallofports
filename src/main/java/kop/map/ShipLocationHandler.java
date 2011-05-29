package kop.map;

import com.bbn.openmap.layer.location.AbstractLocationHandler;
import com.bbn.openmap.layer.location.BasicLocation;
import com.bbn.openmap.layer.location.Location;
import com.bbn.openmap.omGraphics.OMGraphicConstants;
import com.bbn.openmap.omGraphics.OMLine;
import com.bbn.openmap.omGraphics.OMPoly;
import kop.game.Game;
import kop.map.routecalculator.*;
import kop.map.routecalculator.Point;
import kop.ships.model.ShipModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * LocationHandler for ships.
 */
public class ShipLocationHandler extends AbstractLocationHandler {
	@Override
	public Vector get(float v, float v1, float v2, float v3, Vector vector) {
		Vector retVector;

		if (vector == null) {
			retVector = new Vector();
		} else {
			retVector = vector;
		}

		List<ShipModel> ships = Game.getInstance().getPlayerCompany().getShips();

		for (ShipModel ship: ships) {

			OMLine line = new OMLine((float)ship.getLatitude(),
					(float)ship.getLongitude(),
					-1,-1,1,1);
			line.setFillPaint(Color.red);
			line.setLinePaint(Color.red);
			line.setVisible(true);

			Location location = new BasicLocation((float)ship.getLatitude(),
					(float)ship.getLongitude(),
					ship.getName(),
					line);


			location.setShowName(false);
			location.setShowLocation(true);
			location.setLocationHandler(this);

			//noinspection unchecked
			retVector.add(location);

			if (ship.isAtSea()) {
				// add route to map.
				ASRoute route = ship.getCurrentRoute();
				ArrayList<Point> points = route.getPoints();
				float f[] = new float[points.size()*2];
				for (int i=0;i < points.size(); i++) {
					f[i*2] = points.get(i).getLat();
					f[i*2+1] = points.get(i).getLon();
				}
				OMPoly poly = new OMPoly(f, OMGraphicConstants.DECIMAL_DEGREES, OMGraphicConstants.LINETYPE_STRAIGHT);

				poly.setLinePaint(Color.green);

				Location l2 = new BasicLocation((float)ship.getLatitude(),
						(float)ship.getLongitude(),
						ship.getName(),
						poly);
				retVector.add(l2);
			}

		}

		return retVector;
	}

	@Override
	public void reloadData() {

	}
}
