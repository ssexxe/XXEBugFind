/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.xxe;

import bugfind.sootadapters.MethodDefinition;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the key to creating the default XML VulnerableMethodDefinitions programmatically. The important 
 * method here is the static <code> VulnerableXMLMethodDefinitions.getVulnerableMethodDefinitionList() </cdoe>
 * 
 * @author Mikosh
 */
public final class VulnerableXMLMethodDefinitions {
    private static List<VulnerabilityDefinitionItem> vulnerableMethodDefinitionList; 

    /**
     * Gets the default XXE vulnerability definition list. The list returned should not be added by 
     * adding or removing from it. If that has been done in error, clear the list, and then call this method
     * again
     * @return the default XXE vulnerability definition list. 
     */
    public static List<VulnerabilityDefinitionItem> getVulnerableMethodDefinitionList() {
        if (vulnerableMethodDefinitionList == null) {
            vulnerableMethodDefinitionList = new ArrayList<>();            
        }
        
        if (vulnerableMethodDefinitionList.isEmpty()) {
           
            // first create vulnerable definition, then add various mitigations to it
            
//            // create vdi
            MethodDefinition md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.io.InputStream");
            VulnerabilityDefinitionItem vdi = new VulnerabilityDefinitionItem(md);
            List<VulnerabilityMitigationItem> listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            VulnerabilityMitigationItem vmi = createLocalMitigationItem("org.jdom2.input.SAXBuilder", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            // now add mitigation spoilers. mitigation spoilers are incorrect settings which if set/left can nullify the effect of a previous mitigation
            // Occurrence of this usually indicates a mistake by the programmer
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "false").getParameters()));
            // give a string describing solution 
            vmi.setSolutionDescription("A call to SAXBuilder.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); "
                    + "should be made before using SAXBuilder.parse(...) methods to prevent possible XXE attacks. Note that for JDOM version 2.0.5, the setting "
                    + "SAXBuilder.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); does not work and so is "
                    + "disregarded as a mitigation procedure. If this is not the case in a later version, you may decide passing a user-defined ruleset "
                    + "into the application");
                    
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            // for other JDOM build methods
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.io.File");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "org.xml.sax.InputSource");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.io.Reader");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            // for another vdi
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.lang.String");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.net.URL");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.io.InputStream", "java.lang.String");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.jdom2.input.SAXBuilder", "build", "org.jdom2.Document"
                    , "java.io.Reader", "java.lang.String");
            vdi = new VulnerabilityDefinitionItem(md);
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
            vmi = createFactoryMitigationItem("javax.xml.parsers.DocumentBuilderFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            // now add mitigation spoilers. mitigation spoilers are incorrect settings which if set/left can nullify the effect of a previous mitigation
            // Occurrence of this usually indicates a mistake by the programmer
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "false").getParameters()));
            vmi.setSolutionDescription("A call to DocumentBuilderFactory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "DocumentBuilderFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using DocumentBuilder.parse(...) methods");
            listMI.add(vmi);
            vmi = createFactoryMitigationItem("javax.xml.parsers.DocumentBuilderFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "true").getParameters()));
            vmi.setSolutionDescription("A call to DocumentBuilderFactory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "DocumentBuilderFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using DocumentBuilder.parse(...) methods");            
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
        
            // for other dom vdis
            md = createMethodDefinition("javax.xml.parsers.DocumentBuilder", "parse", "org.w3c.dom.Documemt" //method defs
                    , "java.io.File"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.DocumentBuilder", "parse", "org.w3c.dom.Documemt" //method defs
                    , "org.xml.sax.InputSource"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.DocumentBuilder", "parse", "org.w3c.dom.Documemt" //method defs
                    , "java.lang.String"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.DocumentBuilder", "parse", "org.w3c.dom.Documemt" //method defs
                    , "java.io.InputStream", "java.lang.String"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            // for sax
            // create vdi
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.InputStream", "org.xml.sax.helpers.DefaultHandler"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            vmi = createFactoryMitigationItem("javax.xml.parsers.SAXParserFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "false").getParameters()));
            vmi.setSolutionDescription("A call to SAXParserFactory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "SAXParserFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using SAXParser.parse(...) methods");
            listMI.add(vmi);
            vmi = createFactoryMitigationItem("javax.xml.parsers.SAXParserFactory", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "true").getParameters()));
            vmi.setSolutionDescription("A call to SAXParserFactory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "SAXParserFactory.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using SAXParser.parse(...) methods");
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
            
            // for other sax parser methods
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.File", "org.xml.sax.helpers.DefaultHandler"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.File", "org.xml.sax.HandlerBase"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "org.xml.sax.InputSource", "org.xml.sax.helpers.DefaultHandler"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "org.xml.sax.InputSource", "org.xml.sax.HandlerBase"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.InputStream", "org.xml.sax.HandlerBase"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.lang.String", "org.xml.sax.helpers.DefaultHandler"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.lang.String", "org.xml.sax.HandlerBase"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.InputStream", "org.xml.sax.helpers.DefaultHandler", "java.lang.String"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.parsers.SAXParser", "parse", "void" // method def
                    , "java.io.InputStream", "org.xml.sax.HandlerBase", "java.lang.String"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);

            // for xml reader
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
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "false").getParameters()));
            vmi.setSolutionDescription("A call to XMLReader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "XMLReader.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using XMLReader.parse(...) methods");
            listMI.add(vmi);
            vmi = createLocalMitigationItem("org.xml.sax.XMLReader", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "true").getParameters()));
            vmi.setSolutionDescription("A call to XMLReader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "XMLReader.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using XMLReader.parse(...) methods");
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
            
            //for other xml reader parse method
            md = createMethodDefinition("org.xml.sax.XMLReader", "parse", "void" // method def
                    , "java.lang.String"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            
            // for StAX            
            md = createMethodDefinition("javax.xml.stream.XMLStreamReader", "next", "int"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            listMI =  new ArrayList<>();

            // now create mitigation items and add to the vdi
            vmi = createFactoryMitigationItem("javax.xml.stream.XMLInputFactory", "setProperty", null, 
                    createMethodParameters("java.lang.String", "java.lang.Object"), 
                    new ParameterValueCreator().add("java.lang.String", "\"javax.xml.stream.isSupportingExternalEntities\"")
                            .add("java.lang.Object", "java.lang.Boolean.FALSE").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "javax.xml.stream.XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"javax.xml.stream.isSupportingExternalEntities\"")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.setSolutionDescription("A call to XMLInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE); "
                    + "or XMLInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE); should be "
                    + "made before using the StAX parser created from them i.e., before using XMLStreamReader.next() method");
            listMI.add(vmi);
            vmi = createFactoryMitigationItem("javax.xml.stream.XMLInputFactory", "setProperty", null, 
                    createMethodParameters("java.lang.String", "java.lang.Object"), 
                    new ParameterValueCreator().add("java.lang.String", "javax.xml.stream.XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES")
                            .add("java.lang.Object", "java.lang.Boolean.FALSE").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "javax.xml.stream.XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"javax.xml.stream.isSupportingExternalEntities\"")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.setSolutionDescription("A call to XMLInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE); "
                    + "or XMLInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE); should be "
                    + "made before using the StAX parser created from them i.e., before using XMLStreamReader.next() method");
            listMI.add(vmi);
            vmi = createFactoryMitigationItem("javax.xml.stream.XMLInputFactory", "setProperty", null, 
                    createMethodParameters("java.lang.String", "java.lang.Object"), 
                    new ParameterValueCreator().add("java.lang.String", "\"javax.xml.stream.supportDTD\"")
                            .add("java.lang.Object", "java.lang.Boolean.FALSE").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "javax.xml.stream.XMLInputFactory.SUPPORT_DTD")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"javax.xml.stream.supportDTD\"")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.setSolutionDescription("A call to XMLInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE); "
                    + "or XMLInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE); should be "
                    + "made before using the StAX parser created from them i.e., before using XMLStreamReader.next() method");
            listMI.add(vmi);
            vmi = createFactoryMitigationItem("javax.xml.stream.XMLInputFactory", "setProperty", null, 
                    createMethodParameters("java.lang.String", "java.lang.Object"), 
                    new ParameterValueCreator().add("java.lang.String", "javax.xml.stream.XMLInputFactory.SUPPORT_DTD")
                            .add("java.lang.Object", "java.lang.Boolean.FALSE").getParameters());
            //now add mitigation spoilers
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "javax.xml.stream.XMLInputFactory.SUPPORT_DTD")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"javax.xml.stream.supportDTD\"")
                            .add("java.lang.Object", "java.lang.Boolean.TRUE").getParameters()));
            vmi.setSolutionDescription("A call to XMLInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE); "
                    + "or XMLInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE); should be "
                    + "made before using the StAX parser created from them i.e., before using XMLStreamReader.next() method");
            listMI.add(vmi);
            vdi.addMitigationItemsFromList(listMI);            
            vulnerableMethodDefinitionList.add(vdi);
            
            // for other stax vuls ie nextTag(), nextEvent(), peek()
            md = createMethodDefinition("javax.xml.stream.XMLStreamReader", "nextTag", "int"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.stream.XMLEventReader", "nextEvent", "javax.xml.stream.events.XMLEvent"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.stream.XMLEventReader", "nextTag", "javax.xml.stream.events.XMLEvent"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("javax.xml.stream.XMLEventReader", "peek", "javax.xml.stream.events.XMLEvent"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            
            // for JAXB           
            // Note no mitigation item for jaxb as the context is xxe vul by default. Only the unmarshal method that takes 
            // SAXSource (via xmlinputfactory) and and saxparser 
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "java.io.File"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      

            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "org.xml.sax.InputSource"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "java.io.InputStream"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "java.io.Reader"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
           
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "java.net.URL"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "javax.xml.transform.Source"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
           
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "javax.xml.stream.XMLStreamReader"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "javax.xml.stream.XMLEventReader"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "javax.xml.transform.Source", "java.lang.Class"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
           
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "javax.xml.stream.XMLStreamReader", "java.lang.Class"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("javax.xml.bind.Unmarshaller", "unmarshal", "java.lang.Object"
                    , "javax.xml.stream.XMLEventReader", "java.lang.Class"); // method parameters
            vdi = new VulnerabilityDefinitionItem(md);
            vulnerableMethodDefinitionList.add(vdi);      
            
            
            
            // DOM4J
            //org.​dom4j.
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.io.InputStream");
            vdi = new VulnerabilityDefinitionItem(md);
            listMI =  new ArrayList<>();
            
            // now create mitigation items and add to the vdi
            vmi = createLocalMitigationItem("org.dom4j.io.SAXReader", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "true").getParameters());
            // now add mitigation spoilers. mitigation spoilers are incorrect settings which if set/left can nullify the effect of a previous mitigation
            // Occurrence of this usually indicates a mistake by the programmer
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://apache.org/xml/features/disallow-doctype-decl\"")
                            .add("boolean", "false").getParameters()));
            // give a string describing solution 
            vmi.setSolutionDescription("A call to SAXReader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "SAXReader.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using SAXReader.read(...) methods");
                    
            listMI.add(vmi);
            
            vmi = createLocalMitigationItem("org.dom4j.io.SAXReader", "setFeature", null, 
                    createMethodParameters("java.lang.String", "boolean"), 
                    new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "false").getParameters());
            // now add mitigation spoilers. mitigation spoilers are incorrect settings which if set/left can nullify the effect of a previous mitigation
            // Occurrence of this usually indicates a mistake by the programmer
            vmi.addMitigationSpoiler(new MitigationSpoiler(new ParameterValueCreator().add("java.lang.String", "\"http://xml.org/sax/features/external-general-entities\"")
                            .add("boolean", "true").getParameters()));
            // give a string describing solution 
            vmi.setSolutionDescription("A call to SAXReader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true); or "
                    + "SAXReader.setFeature(\"http://xml.org/sax/features/external-general-entities\", false); should be "
                    + " made before using parsers created from them i.e., before using SAXReader.read(...) methods");
                    
            listMI.add(vmi);            
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.io.File");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "org.xml.sax.InputSource");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.io.Reader");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.lang.String");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.net.URL");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);      
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.io.InputStream", "java.lang.String");
            vdi = new VulnerabilityDefinitionItem(md);
            vdi.addMitigationItemsFromList(listMI);
            vulnerableMethodDefinitionList.add(vdi);       
            
            md = createMethodDefinition("org.dom4j.io.SAXReader", "read", "org.dom4j.Document"
                    , "java.io.Reader", "java.lang.String");
            vdi = new VulnerabilityDefinitionItem(md);
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
    
    private static VulnerabilityMitigationItem createMitigationItem(String type, String classname, String methodName, 
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
    
    private static VulnerabilityMitigationItem createFactoryMitigationItem(String classname, String methodName, 
            String methodReturnType, List<MethodDefinition.MethodParameter> listMP, List<MethodParameterValue> listPV)  {
        return createMitigationItem(VulnerabilityMitigationItem.FACTORY, classname, methodName, methodReturnType, listMP, listPV);
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
