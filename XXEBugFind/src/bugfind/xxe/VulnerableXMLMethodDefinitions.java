/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.xxe;

import bugfind.sootadapters.MethodDefinition;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import org.xml.sax.XMLReader;

/**
 *
 * @author Mikosh
 */
public final class VulnerableXMLMethodDefinitions {
    private static List<VulnerabilityDefinitionItem> vulnerableMethodDefinitionList; 

    public static List<VulnerabilityDefinitionItem> getVulnerableMethodDefinitionList() {
        if (vulnerableMethodDefinitionList == null) {
            vulnerableMethodDefinitionList = new ArrayList<>();            
        }
        
        if (vulnerableMethodDefinitionList.isEmpty()) {
            //List<MethodDefinition.MethodParameter> parameters = new ArrayList<>();            
            //parameters.add(new MethodDefinition.MethodParameter("java.io.InputStream", null));
            //MethodDefinition md = new MethodDefinition("org.jdom2.input.SAXBuilder", "build", parameters, "org.jdom2.Document");
            //vulnerableMethodDefinitionList.add(md);
           
            // first create vulnerable definition, then add various mitigations to it
            
            // create vdi
            MethodDefinition md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.io.InputStream");
            VulnerabilityDefinitionItem vdi = new VulnerabilityDefinitionItem(md);
            List<VulnerabilityMitigationItem> listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            VulnerabilityMitigationItem vmi = createLocalMitigationItem("org.jdom2.input.SAXBuilder", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            // now repeat the above for others
            // note if below is to be edited, remember that variables md, vdi, vmi, listMI are used repeatedly and hence
            // need to be reset before each use. failure to do so will lead to a bug where the wrong mitigation item is used
            // for the wrong vulnerability item
            
            
            // create vdi
            md = createMethodDefinition("javax.xml.parsers.DocumentBuilder", "parse", "org.w3c.dom.Documemt" //method defs
                    , "java.io.InputStream"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            vmi = createLocalMitigationItem("javax.xml.parsers.DocumentBuilderFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            vmi.setSolutionDescription("A call to DocumentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); or "
                    + "DocumentBuilderFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using DocumentBuilder.parse(...) methods");
            listMI.add(vmi);
            vmi = createLocalMitigationItem("javax.xml.parsers.DocumentBuilderFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            vmi.setSolutionDescription("A call to DocumentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); or "
                    + "DocumentBuilderFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using DocumentBuilder.parse(...) methods");            
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
            
            
            // create vdi
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.InputStream", "org.xml.sax.helpers.DefaultHandler"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            vmi = createLocalMitigationItem("javax.xml.parsers.SAXParserFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            vmi.setSolutionDescription("A call to SAXParserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); or "
                    + "SAXParserFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using SAXParser.parse(...) methods");
            listMI.add(vmi);
            vmi = createLocalMitigationItem("javax.xml.parsers.SAXParserFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            vmi.setSolutionDescription("A call to SAXParserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); or "
                    + "SAXParserFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using SAXParser.parse(...) methods");
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
            

            // create vdi
            md = createMethodDefinition("org.xml.sax.XMLReader", "parse", "void" // method def
                    , "org.xml.sax.InputSource"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            vmi = createLocalMitigationItem("org.xml.sax.XMLReader", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            vmi.setSolutionDescription("A call to XMLReader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); or "
                    + "XMLReader.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using XMLReader.parse(...) methods");
            listMI.add(vmi);
            vmi = createLocalMitigationItem("org.xml.sax.XMLReader", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            vmi.setSolutionDescription("A call to XMLReader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true); or "
                    + "XMLReader.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using XMLReader.parse(...) methods");
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
        }
        
        return vulnerableMethodDefinitionList;
    }

    private static MethodDefinition createMethodDefinition(String classname, String methodName, String methodReturnType 
            , String... parameterType) {
        List<MethodDefinition.MethodParameter> parameters = new ArrayList<>();            
        for (String pType : parameterType) {
            parameters.add(new MethodDefinition.MethodParameter(pType, null));
        }        

        //MethodDefinition md = new MethodDefinition("org.jdom2.input.SAXBuilder", "build", parameters, "org.jdom2.Document");
        MethodDefinition md = new MethodDefinition(classname, methodName, parameters, methodReturnType);

        return md;
    }
    
    private static VulnerabilityMitigationItem createMitigationItem(int type, String classname, String methodName, 
            String methodReturnType, List<MethodDefinition.MethodParameter> listMP, List<MethodParameterValue> listPV)  {
        MethodDefinition md = new MethodDefinition(classname, methodName, listMP, methodReturnType);
        VulnerabilityMitigationItem vmi = new VulnerabilityMitigationItem(md, listPV, type);
        return vmi;
    }
    
    private static VulnerabilityMitigationItem createGlobalMitigationItem(String classname, String methodName, 
            String methodReturnType, List<MethodDefinition.MethodParameter> listMP, List<MethodParameterValue> listPV)  {
        return createMitigationItem(VulnerabilityMitigationItem.GLOBAL, classname, methodName, methodReturnType, listMP, listPV);
    }
   
    private static VulnerabilityMitigationItem createLocalMitigationItem(String classname, String methodName, 
            String methodReturnType, List<MethodDefinition.MethodParameter> listMP, List<MethodParameterValue> listPV)  {
        return createMitigationItem(VulnerabilityMitigationItem.LOCAL, classname, methodName, methodReturnType, listMP, listPV);
    }
    
    private static List<MethodDefinition.MethodParameter> createMethodParameters(String... parameterType) {
        List<MethodDefinition.MethodParameter> parameters = new ArrayList<>();            
        for (String pType : parameterType) {
            parameters.add(new MethodDefinition.MethodParameter(pType, null));
        }
        
        return parameters;
    }
    
    private static class ParameterValueCreator {
        private List<MethodParameterValue> mpv = new ArrayList<MethodParameterValue>();
        
        private ParameterValueCreator add(String parameterType, String value) {
            mpv.add(new MethodParameterValue(parameterType, value));
            return this;
        }
        
        private List<MethodParameterValue> getParameters() {
            return mpv;
        }
    }
}
