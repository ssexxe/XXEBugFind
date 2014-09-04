/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stax;

import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import myxetestapp.utils.Employee;

import myxetestapp.utils.Utils;

/**
 *
 * @author Mikosh
 */
public class StAXExample {

    public static void main(String[] args) throws XMLStreamException {
        List<Employee> empList = null;
        Employee currEmp = null;
        String tagContent = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();        
        //factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);// WORKS FOR INTERNAL ENTITIES
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);// WORKS FOR EXTERNAL ENTITIES
        //factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);//WORKS but leads to exception
        
        XMLStreamReader reader
                = factory.createXMLStreamReader(StAXExample.class.getResourceAsStream(Utils.INTERNAL_XML_LOCATION));
                        
        while (reader.hasNext()) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    if ("Employee".equals(reader.getLocalName())) {
                        currEmp = new Employee();
                        currEmp.setType(reader.getAttributeValue(0));
                    }
                    if ("Personnel".equals(reader.getLocalName())) {
                        empList = new ArrayList<>();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "Employee":
                            empList.add(currEmp);
                            break;
                        case "Name":
                            currEmp.setName(tagContent);
                            break;
                        case "Id":
                            currEmp.setId(Integer.parseInt(tagContent));
                            break;
                        case "Age":
                            currEmp.setAge(Integer.parseInt(tagContent));
                            break;
                    }
                    break;

                case XMLStreamConstants.START_DOCUMENT:
                    empList = new ArrayList<>();
                    break;
            }

        }

        //Print the employee list populated from XML
        for (Employee emp : empList) {
            System.out.println(emp);
        }
        
        

    }
    
    
}
