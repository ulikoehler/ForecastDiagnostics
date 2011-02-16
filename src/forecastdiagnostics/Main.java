/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forecastdiagnostics;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author uli
 */
public class Main {

    private static int state = -1;
    private final static int LATLONLIST = 1;
    private final static int CITYLIST = 2;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        InputStream in = new BufferedInputStream(new URL("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php?listCitiesLevel=1234").openStream());
        XMLInputFactory fac = XMLInputFactory.newFactory();
        XMLStreamReader reader = fac.createXMLStreamReader(in);
        List<String> coordinates = Lists.newLinkedList();
        List<String> names = Lists.newLinkedList();
        while (reader.hasNext()) {
            int ev = reader.next();
            if (ev == XMLStreamReader.START_ELEMENT) {
                if (reader.getLocalName().equals("latLonList")) {
                    state = LATLONLIST;
                } else if (reader.getLocalName().equals("cityNameList")) {
                    state = CITYLIST;
                } else {
                    state = -1;
                }
            } else if (ev == XMLStreamReader.CHARACTERS) {
                String text = reader.getText();
                if (state == LATLONLIST) {
                    for (String coord : Splitter.on(",").omitEmptyStrings().trimResults().split(text)) {
                        coordinates.add(coord);
                    }
//                    coordinates = ImmutableList.copyOf(split);
//                    for(String s : coordinates) {
//                    System.out.println(s);}
                } else if (state == CITYLIST) {
                    for (String name : Splitter.on("|").omitEmptyStrings().trimResults().split(text)) {
                        names.add(name);
                    }
//                    names = ImmutableList.copyOf(split);
//                    for(String s : names) {
//                    System.out.println(s);}
                }
            }
        }
        in.close();
        System.out.println("Finished");
        System.out.println(coordinates.size());
        System.out.println(names.size());
        for (int i = 0; i < coordinates.size(); i++) {
            if (names.size() <= i) {
                break;
            }
            System.out.println(coordinates.get(i) + "     " + names.get(i));
        }
    }
}
