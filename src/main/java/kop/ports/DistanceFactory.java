package kop.ports;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/6/11
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceFactory {
	public List<Distance> createDistance(String fileName, Map<String, Port> ports) throws IOException {
		List<Distance> list = new ArrayList<Distance>();

		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line=reader.readLine())!=null) {
			if (line.matches("[A-Z, ]*")) {
				// we have a port.
				Port port = ports.get(line.split(",")[0]);
				if (port != null) {

				}
			}
		}

		return list;
	}
}
