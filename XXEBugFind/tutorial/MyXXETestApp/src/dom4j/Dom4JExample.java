/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dom4j;

import java.io.IOException;
import myxetestapp.utils.Utils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

/**
 *
 * @author Mikosh
 */
public class Dom4JExample {
    
    public Document parse() throws DocumentException, SAXException {
        SAXReader reader = new SAXReader();
        
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
        
        //reader.setFeature("http://xml.org/sax/features/external-general-entities", true);//WORKS but throws exception if DTD tag is encountered
        //reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); // NOT WORKING FOR entity attack but should work for DOS attack
        Document document = reader.read(this.getClass().getResourceAsStream(Utils.INTERNAL_XML_LOCATION));
        return document;
    }   
    
    public void write(Document document) throws IOException {

        // lets write to a file
        XMLWriter writer;
//        = new XMLWriter(
//            new BufferedOutputStream(outputStream));
//        writer.write( document );
//        writer.close();


        // Pretty print the document to System.out
        System.out.println("\n\nPretty format");
        OutputFormat format = OutputFormat.createPrettyPrint();
        writer = new XMLWriter( System.out, format );
        writer.write( document );

        // Compact format to System.out
        System.out.println("\n\nCompact format");
        format = OutputFormat.createCompactFormat();
        writer = new XMLWriter( System.out, format );
        writer.write( document );
    }
    
    public static void main(String[] args) throws DocumentException, IOException, SAXException {
        Dom4JExample d4je = new Dom4JExample();
        Document doc = d4je.parse();
        d4je.write(doc);        
    }
}