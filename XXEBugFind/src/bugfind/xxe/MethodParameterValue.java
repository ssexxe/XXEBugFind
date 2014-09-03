/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bugfind.xxe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A methhod parameter value
 * @author Mikosh
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public final class MethodParameterValue {
    @XmlElement (name = "ParameterType")
    private String type;
    
    @XmlElement (name = "ParameterValue")
    private String value;

    /**
     * Creates a new MethodParameterValue object
     */
    public MethodParameterValue() {}

    /**
     * Creates a new MethodParameterValue object when given the type and value
     * @param type the type of the method parameter
     * @param value the value of the method parameter as String
     */
    public MethodParameterValue(String type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the type
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the value
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the type of the parameter
     * @param type a String representing the type of the parameter to be set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the value of the method parameter
     * @param value the value to be set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Parameter {");
        sb.append(type).append(":").append(value).append("}");
        
        return sb.toString();
    }

    /**
     * Returns if the method parameter value is a constant e.g "fish", for string, 1 for int returns true , but fish 
     * will return false since in the latter case, it is a variable
     * @param mpv the mpv to check
     * @return  if the method parameter value is a constant e.g "fish", for string, 1 for int returns true , but fish 
     * will return false since in the latter case, it is a variable
     */
    public static boolean isConstant(MethodParameterValue mpv) {
        // try conveting to double, then boolean then string starts with "
        String value = mpv.getValue();
        String type = mpv.getType();
        if (value == null) {
            return false;
        }
        else if (value.startsWith("\"") && type.equals("java.lang.String")) {// this indicates a string constant
            return true;
        }
        else if (value.startsWith("'") && type.equals("char")) {// this indicates a char constant
            return true;
        }
        else if ((value.equals("true") || value.equals("false")) && type.equals("boolean")) { //this indicates boolean constant            
            return true;
        }
        else {
            try {// if the value is convertible to double then it is a number constant
                Double.parseDouble(value); 
            } catch (Exception ex) {
                return false;
            }
            return true;
        }     
    }
    
}
