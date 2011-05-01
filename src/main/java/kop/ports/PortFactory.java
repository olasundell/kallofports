package kop.ports;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
	private String[] header;
	private Map<String, String> countryCodes;
	private final String portFileName = "kop/ports/worldports.xml";
	private final String countryCodeFileName = "kop/ports/countrycodes.xml";

	public Map<String, Port> createPorts() throws IOException, URISyntaxException {
		URL portURL = ClassLoader.getSystemClassLoader().getResource(portFileName);
		URL countryURL = ClassLoader.getSystemClassLoader().getResource(countryCodeFileName);

		countryCodes = createCountryCodes(countryURL.toURI());

		HashMap<String, Port> ports = new HashMap<String, Port>();

		File source = new File(portURL.toURI());
		CSVReader reader = new CSVReader(new FileReader(source), '\t');
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

	private Map<String, String> createCountryCodes(URI fileName) throws IOException, URISyntaxException {
		Map<String, String> countryCodes = new HashMap<String, String>();

		File source = new File(fileName);
		CSVReader reader = new CSVReader(new FileReader(source), '\t');
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
