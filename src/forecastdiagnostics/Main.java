/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forecastdiagnostics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.io.input.AutoCloseInputStream;

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
        InputStream in = new AutoCloseInputStream(new BufferedInputStream(new URL("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php?listCitiesLevel=1234").openStream()));
        XMLInputFactory fac = XMLInputFactory.newFactory();
        XMLStreamReader reader = fac.createXMLStreamReader(in);
        List<String> coordinates = null;
        List<String> names = null;
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
                    String[] split = text.split(",");
                    coordinates = ImmutableList.copyOf(split);
//                    for(String s : coordinates) {
//                    System.out.println(s);}
                } else if (state == CITYLIST) {
                    String[] split = text.split("\\|");
                    names = ImmutableList.copyOf(split);
//                    for(String s : names) {
//                    System.out.println(s);}
                }
            }
        }
        System.out.println(coordinates.size());
        System.out.println(names.size());
        for (int i = 0; i < coordinates.size(); i++) {
            System.out.println(coordinates.get(i) + "     " + names.get(i));
        }
    }
}
