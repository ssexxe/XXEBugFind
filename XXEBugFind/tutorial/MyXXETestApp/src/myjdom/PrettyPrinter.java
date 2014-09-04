/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjdom;

import javax.xml.XMLConstants;
import myxetestapp.utils.Utils;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.*;

public class PrettyPrinter {

    public static void main(String[] args) {        
        try {
            // Build the document with SAXBuilder of JDOM, 
            SAXBuilder builder = new SAXBuilder();    
            //builder.setFeature("http://xml.org/sax/features/external-general-entities", false);//NOT WORKING
             //builder.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);//NOT WORKING
            //builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);// WORKING
            // Create the document
            Document doc = builder.build(PrettyPrinter.class.getResourceAsStream(Utils.INTERNAL_XML_LOCATION));//(new File(filename));
            // Output the document, use standard formatter
            XMLOutputter fmt = new XMLOutputter();
            fmt.output(doc, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
