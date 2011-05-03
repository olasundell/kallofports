package kop;

import au.com.bytecode.opencsv.CSVReader;
import kop.map.LatLong;
import kop.ports.Port;
import kop.ports.PortMap;
import kop.ports.PortsOfTheWorld;
import kop.ships.ModelSerializer;

import java.io.*;
import java.util.ArrayList;

public class ConvertUnlocodeToXML {
	private static final String LONGLAT_REGEXP = "[0-9]{4}[NS] [0-9]{5}[EW]";

	public static void main(String[] args) throws Exception {
//		createLatLongFromTSV();
		PortMap htmlPortMap = createPortMapFromHTML();

		PortMap portMap	= PortsOfTheWorld.getPorts();

		for (Port htmlPort: htmlPortMap.values()) {
			Port mappedPort = portMap.get(htmlPort.getName());
			// the names won't coincide, let's lookup via UN/LOCODE
			if (mappedPort == null) {
				for (Port p: portMap.values()) {
					if (p.getUnlocode().replaceAll(" ","").equals(htmlPort.getUnlocode())) {
						mappedPort = p;
						break;
					}
				}
			}

			if (mappedPort == null) {
				continue;
			}

			System.out.println(mappedPort);

			mappedPort.setLatitude(htmlPort.getLatitude());
			mappedPort.setLongitude(htmlPort.getLongitude());
		}


		ModelSerializer.saveToFile("portmap.xml", PortMap.class, portMap);

	}

	private static void createLatLongFromTSV() throws Exception {
		File source = new File("unlocode.csv");
		CSVReader reader = new CSVReader(new FileReader(source), ',');
		String[] line;
		ArrayList<Port> tsvPorts = new ArrayList<Port>();

		// skip the first line, headers.
		reader.readNext();

		while ((line = reader.readNext())!=null) {
			try {
				Port port = createPort(line);

				if (port != null) {
					tsvPorts.add(port);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}


		reader.close();

		PortMap portMap	= PortsOfTheWorld.getPorts();

		for (Port tsvPort: tsvPorts) {
			Port mappedPort = portMap.get(tsvPort.getName());
			// the names won't coincide, let's lookup via UN/LOCODE
			if (mappedPort == null) {
				for (Port p: portMap.values()) {
					if (p.getUnlocode().equals(tsvPort.getUnlocode())) {
						mappedPort = p;
						break;
					}
				}
			}

			if (mappedPort == null) {
				continue;
			}

			System.out.println(mappedPort);

			mappedPort.setLatitude(tsvPort.getLatitude());
			mappedPort.setLongitude(tsvPort.getLongitude());
		}


		ModelSerializer.saveToFile("portmap.xml", PortMap.class, portMap);
	}

	protected static PortMap createPortMapFromHTML() throws IOException {
		PortMap ports = new PortMap();

		File portlist = new File("e-ships.net/ports/portlist.txt");
		BufferedReader portListReader = new BufferedReader(new FileReader(portlist));

		String fileName;

		while ((fileName = portListReader.readLine())!=null) {
			Port port = createPortFromFile("e-ships.net/ports/"+fileName);
			if (port != null) {
				ports.put(port.getUnlocode(), port);
			}
		}

		return ports;
	}

	protected static Port createPortFromFile(String fileName) throws IOException {
		System.out.println(fileName);
		File portfile = new File(fileName);
		BufferedReader portFileReader = new BufferedReader(new FileReader(portfile));
		String row;
		while ((row = portFileReader.readLine())!=null) {
			if (row.trim().startsWith("<br><br><table cellpadding=6")) {
				// skip a little, brother...
				portFileReader.readLine();
				portFileReader.readLine();
				String latitude = portFileReader.readLine();
				latitude = latitude.split("<td class=main.*>")[1];

				portFileReader.readLine();
				portFileReader.readLine();
				String longitude = portFileReader.readLine();
				longitude = longitude.split("<td class=main.*>")[1];

				portFileReader.readLine();
				portFileReader.readLine();
				String unlocode = portFileReader.readLine();
				unlocode = unlocode.split("<td class=main.*>")[1];
				if (!unlocode.matches("[A-Z]{5}")) {
					return null;
				}

				portFileReader.close();

				return createPort(latitude, longitude, unlocode);

			}
		}

		return null;
	}

	protected static LatLong parse(String instr) {
		LatLong latLong = new LatLong();
		String[] splitted = instr.split("&deg ");
		String deg = splitted[0];

		deg = deg.replaceFirst("^[0]+","");

		if (deg.isEmpty()) {
			// obviously the string came up all zeroes.
			deg = "0";
		}

		latLong.setDeg(deg);
		latLong.setMin(splitted[1].substring(0,2));
		latLong.setHemisphere(String.valueOf(instr.charAt(instr.length() - 1)));

		return latLong;
	}

	protected static Port createPort(String latitude, String longitude, String unlocode) {
		Port port = new Port();

		port.setLatitude(parse(latitude));
		port.setLongitude(parse(longitude));
		port.setUnlocode(unlocode);

		return port;
	}

	protected static Port createPort(String[] line) {
		if (line.length < 8) {
			return null;
		}

		String countryCode = line[1];
		String portCode = line[2];
		String portName = line[3];
		String facilities = line[7];

		if (portCode.isEmpty() || !facilities.startsWith("1") || line[10].isEmpty()) {
			return null;
		}

//		if (line[10].matches(LONGLAT_REGEXP)) {
//			longitude = line[10].substring(0,line[10].indexOf(' '));
//			latitude = line[10].substring(line[10].indexOf(' '), line[10].length());
//		} else if (line.length >=13 && line[12].matches(LONGLAT_REGEXP)) {
//			longitude = line[12].substring(0,line[12].indexOf(' '));
//			latitude = line[12].substring(line[12].indexOf(' '), line[12].length());
//		} else {
//			return null;
//		}

		Port port = new Port();

		port.setCountryCode(countryCode);
		port.setUnlocode(countryCode + " " + portCode);
		port.setName(portName);
		port.setLatitude(line[4], line[5], line[6]);
		port.setLongitude(line[7],line[8],line[9]);
//		port.setLongitude(longitude.substring(0, 1), longitude.substring(2, 3), longitude.substring(4, 4));
//		port.setLatitude(latitude.substring(0, 1), latitude.substring(2, 3), latitude.substring(5, 5));

		return port;
	}
}
