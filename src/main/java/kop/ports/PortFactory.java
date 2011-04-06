package kop.ports;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ola
 * Date: 4/5/11
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class PortFactory {
	// not used for anything except debugging purposes. :-)
	String[] header;
	private Map<String, String> countryCodes;

	public Map<String, Port> createPorts(String portFileName, String countryCodeFileName) throws IOException {
		countryCodes = createCountryCodes(countryCodeFileName);

		HashMap<String, Port> ports = new HashMap<String, Port>();
		CSVReader reader = new CSVReader(new FileReader(portFileName), '\t');
		String[] nextLine;

		Port p;

		// skip the first line
		header = reader.readNext();
		while ((nextLine = reader.readNext())!=null) {
			p = createPort(nextLine);
			ports.put(p.getName(), p);
		}

		reader.close();

		return ports;
	}

	private Map<String, String> createCountryCodes(String fileName) throws IOException {
		Map<String, String> countryCodes = new HashMap<String, String>();

		CSVReader reader = new CSVReader(new FileReader(fileName), '\t');
		String[] line;

		// skip the first line
		while ((line = reader.readNext())!=null) {
			countryCodes.put(line[0],line[1]);
		}

		reader.close();

		return countryCodes;
	}

	private Port createPort(String[] line) {
		Port p = new Port();
		p.setName(line[2]);
		p.setCountryCode(line[3]);
		p.setCountry(countryCodes.get(line[3]));
		p.setLatitude(line[4],line[5],line[6]);
		p.setLongitude(line[7],line[8],line[9]);
		p.setHarbourSize(line[12]);
		p.setHarbourType(line[13]);
		p.setBunkerOil(line[69]);
		p.setDieselOil(line[70]);
		p.setDrydock(line[74]);

		return p;
	}
}
