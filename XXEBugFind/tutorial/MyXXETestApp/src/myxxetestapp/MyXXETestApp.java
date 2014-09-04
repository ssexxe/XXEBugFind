/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package myxxetestapp;

import java.io.IOException;
import org.xml.sax.SAXException;
import piccolotest.MyPiccoloExample;

/**
 *
 * @author Mikosh
 */
public class MyXXETestApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException, IOException {
        // Non Secure parsing
        MyPiccoloExample.nonSecurePicoloXMLParse(args[0]);
        // Secure parsing
        MyPiccoloExample.securePicoloXMLParse(args[0]);
    }
    
}
