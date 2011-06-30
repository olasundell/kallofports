package kop.ports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
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
			Logger logger = LoggerFactory.getLogger(GenerateScriptForRouting.class);
			logger.error("Error while generating script", e);
		}


	}

	private static String getPortString(Port port) {
		StringBuffer str = new StringBuffer();
		str.append(port.getName().replaceAll(" ", "%20"));
		str.append("%20");
		str.append(String.format("(%s%20%20)", port.getUnlocode().replaceAll(" ", "%20")));
		return str.toString();
	}
}