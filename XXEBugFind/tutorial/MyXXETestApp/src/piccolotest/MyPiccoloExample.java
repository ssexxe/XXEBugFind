/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package piccolotest;

import com.bluecast.xml.Piccolo;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Mikosh
 */
public class MyPiccoloExample {
    
    public static void nonSecurePicoloXMLParse(String arg) throws SAXException, IOException {        
        Piccolo piccoloReader = new Piccolo();                
        piccoloReader.parse(arg);        
    }
    
    public static void securePicoloXMLParse(String arg) throws SAXException, IOException {        
        Piccolo piccoloReader = new Piccolo();        
        piccoloReader.setFeature("http://xml.org/sax/features/external-general-entities", false);

        piccoloReader.parse(arg);        
    }
    
    
    
}
