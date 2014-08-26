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
 *
 * @author Mikosh
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public final class MethodParameterValue {
    @XmlElement (name = "ParameterType")
    private String type;
    
    @XmlElement (name = "ParameterValue")
    private String value;

    public MethodParameterValue() {}

    public MethodParameterValue(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String name) {
        this.value = name;
    }

}
