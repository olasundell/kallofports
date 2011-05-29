package kop.ports;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @deprecated Now we do our own routing. This isn't used anymore.
 */
public class GenerateScriptForRouting {
	private static final String WGET_URL_BEGIN = "http://www.portworld.com/map/map-route.php?fromport=";
	private static final String WGET_URL_MID = "&port=";

	public static void main(String[] args) {
		try {
			PortsOfTheWorld	 world = new PortsOfTheWorld();

			FileWriter writer = new FileWriter("porturls.txt");

			ArrayList<Port> list = new ArrayList<Port>();
			list.addAll(world.getPortsAsList());
//			     http://www.portworld.com/map/map-route.php?fromport=Rotterdam%20(NL%20RTM%20%20)&port=Singapore%20(SG%20SIN%20%20)&bos=true&suez=true&pana=true

			for (int i=0;i<list.size();i++) {
				for (int j=i+1;j<list.size();j++) {
					String str = WGET_URL_BEGIN + getPortString(list.get(i)) + WGET_URL_MID + getPortString(list.get(j));
					writer.write(str + "&bos=true&suez=true&pana=true\n");
					writer.write(str + "&bos=true&suez=false&pana=true\n");
					writer.write(str + "&bos=true&suez=true&pana=false\n");
					writer.write(str + "&bos=true&suez=false&pana=false\n");
				}
			}

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}


	}

	private static String getPortString(Port port) {
		String str;
		str = port.getName().replaceAll(" ", "%20");
		str += "%20";
		str += "(" + port.getUnlocode().replaceAll(" ", "%20") + "%20%20)";
		return str;
	}
}