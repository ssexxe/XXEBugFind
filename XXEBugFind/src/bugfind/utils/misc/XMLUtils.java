/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

import bugfind.xxe.xmlobjects.ActualVulnerabilityItems;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *  Provides some XML utilities used by the app
 * @author Mikosh
 */
public class XMLUtils {
    
    /**
     * Converts the XML file specified into the specified POJO type
     * @param <T> the object type of the POJO
     * @param xmlfile the XML file to convert
     * @param classOfT the class of the POJO
     * @return the POJO object if conversion was successful
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws FileNotFoundException 
     */
    public static <T> T convertToPojo(File xmlfile, Class<T> classOfT) throws JAXBException, XMLStreamException, FileNotFoundException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classOfT);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        
        XMLInputFactory xif = XMLInputFactory.newFactory();
        // settings to prevent xxe // would be funny if this tool is itsef is vulnerable to xxe :D
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        
        XMLStreamReader xsr = xif.createXMLStreamReader(new FileReader(xmlfile));
        T t = (T) jaxbUnmarshaller.unmarshal(xsr);//(xmlfile);
        
        return t;
    }
    
    /**
     * Writes the specified AVI to the specified output stream
     * @param avis the avis
     * @param os the output stream to use
     * @throws JAXBException 
     */
    public static void writeXMLToStream(ActualVulnerabilityItems avis,  OutputStream os) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ActualVulnerabilityItems.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(avis, System.out);
        jaxbMarshaller.marshal(avis, os);
    }
    
    
            
}
