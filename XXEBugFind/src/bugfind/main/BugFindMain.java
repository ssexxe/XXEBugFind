/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.main;

import bugfind.sootadapters.CallGraphObject;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;



// note
// 1. dependency of soot on the path to the soot lib


/**
 * This is the main class for BugFind. Entry into the application is via BugFindMain.main(...)
 * @author Mikosh
 */
public class BugFindMain {

    /**
     * This is the main entry point into the application
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        setUpLogging();
        
        //args = mockargs();
        
        Map<String, String> inputMap = OptionsParser.parse(args);
  
        CallGraphObject cgo = CallGraphObject.getInstance(inputMap);
        try {
            cgo.runAnalysis();
        } catch (FileSystemException ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static String[] mockargs() {
                return new String[]{"-d",      
            //"C:\\Users\\Mikosh\\Documents\\bugfind\\others\\tmp\\eaxy-0.2.jar",        
            //"C:\\Users\\Mikosh\\Dropbox\\Bug Variant Detection project\\xxebugfindtool\\xxebugfind\\play_2.10.jar",
            //"C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\others\\play-2.0.8\\play-2.0.8\\repository\\local\\play\\play_2.9.1\\2.0.8\\jars\\play_2.9.1",
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest3\\dist\\MyXMLTest3.jar",
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\MyXMLTest.jar",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest2\\dist\\MyXMLTest2.jar",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\dist\\xmlmaster2.jar",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\xml-apis.jar;" +//,    
////            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\jaxen-1.1.6.jar;" +
////            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\jdom-2.0.5.jar;" +
////            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\resolver.jar;" +
////            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\serializer.jar;" +
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\xercesImpl.jar",    
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\EazyChat\\dist\\EazyChat.jar",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\GP_Manager\\dist\\GP_Manager.jar",
////            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\XXETestProject\\dist\\XXETestProject.jar",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\XXETestProject\\src",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\___others\\temp\\eazy chats\\EazyChatServer\\src; C:\\f",
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\DbNetStarter.jar;" +
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\GP_Calc\\dist\\GP_Calc.jar",    
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\lib\\hsqldb.jar;",// +
//            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\lib\\derbyclient.jar",
            "-l",
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\lib\\derby.jar;" +
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\lib\\derbyclient.jar;" +
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\lib\\derbynet.jar;" +
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\DbNetStarter\\dist\\lib\\hsqldb.jar",
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\XXETestProject\\dist\\lib\\h2-1.3.174.jar",
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\jdom-1.1.3\\jdom\\build\\jdom-1.1.3.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\jaxen-1.1-beta-6.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\jaxme-api-0.3.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\jsr173_1.0_api.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\msv-20030807.jar;" +    
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\pull-parser-2.1.10.jar;" +    
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\relaxngDatatype-20030807.jar;" +    
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\xpp3-1.1.3.3.jar;" +    
//            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\lib\\xsdlib-20030807.jar;" +    
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\jaxen-1.1.6.jar;" +
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\stax2-api-3.1.1.jar;" +
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\woodstox-core-lgpl-4.2.0.jar;" +  
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\woodstox-core-asl-4.2.0.jar;" +    
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\jdom-2.0.5.jar;" +
            "C:\\Users\\Mikosh\\Documents\\Netbeans Libs\\XML\\dom4j-1.6.1\\dom4j-1.6.1\\dom4j-1.6.1.jar;" + 
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\resolver.jar;" +
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\serializer.jar;" +
            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\xercesImpl.jar",//;" +
            //"C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\MyXMLTest\\dist\\lib\\xml-apis.jar",
            
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\commons-cli-1.1.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\core-renderer-R8pre2.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\dom4j-1.6.1.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\htmlcleaner-2.2.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\igpp-util-1.0.8.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\iText-2.0.8.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\javax.servlet.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\javax.servlet.jsp.jar;" +
//            "C:\\Users\\Mikosh\\Documents\\NetbeansProjects\\xmlmaster2\\lib\\saxon9he.jar",

//            "-rs",
//            "C:\\f\\vmis.xml",
//            "-f",
//            "xml",
            
            "-vmd",
            //"java.net.InetAddress.getLocalHost()"
            //"database.objects.Post.getPost();eazychat.ChatClient.requestInterSystemViewer(java.lang.String)"
            //"gp_manager.FileUtils.openFileToFrame(javax.swing.JFrame, java.lang.String, int) "
            //"xxetestproject.libs.ConcreteB.shoot(int);xxetestproject.libs.AInterface.shoot(int)"            
            //"xxetestproject.libs.AInterface.shoot(int)"    
            //"org.​w3c.​dom.DocumentBuilder.parse(java.io.InputStream)"
            "javax.xml.parsers.DocumentBuilderFactory.setFeature(java.lang.String, boolean);" +
            "javax.xml.parsers.DocumentBuilder.parse(java.io.InputStream);"
                + "javax.xml.parsers.SAXParser.parse(java.io.InputStream, org.xml.sax.helpers.DefaultHandler);"
                + "org.xml.sax.XMLReader.parse(org.xml.sax.InputSource)"
            //"javax.​xml.​parsers.DocumentBuilder.parse(java.io.InputStream)"
            //"org.apache.xerces.parsers.SAXParser.parse(java.lang.String)"    
            //"org.jdom2.input.SAXBuilder.build(java.io.File)"// without hidden chars
            //"org.​jdom2.​input.SAXBuilder.build(java.io.File)".length() // with two hidden chars
            //"org.h2.Driver.getMajorVersion()"
        };
    }

    private static void setUpLogging() {
       
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();
        try {
            logManager.readConfiguration(BugFindMain.class.getResourceAsStream("/bugfind/utils/misc/logging.properties"));
            //System.setProperty("java.Util.logging.ConsoleHandler.level", "FINE");
            
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (IOException ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
