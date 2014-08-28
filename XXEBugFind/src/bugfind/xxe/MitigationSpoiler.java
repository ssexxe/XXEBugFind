/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.xxe;

import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a mitigation spoiler. In general a mitigation spoiler is a method call which if made can nullify 
 * the effect of a previous mitigation. Most times this usually represents a mistake on the part of the programmer. 
 * For instance the call to <code> SAXParser.setFeature("http://xml.org/sax/features/external-general-entities", true) </code>
 * is a mitigation spoiler to <code> SAXParser.setFeature("http://xml.org/sax/features/external-general-entities", false) </code>
 * cause the former if called after the latter will undo the prevention of XXE attacks. 
 * In general mitigation spoilers are a collection of incorrect arguments/parameter values that can reverse a mitigation attempt
 * @author Mikosh
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class MitigationSpoiler {
    @XmlElement (name = "ParameterValue")
    private final List<MethodParameterValue> parameterValues;

    /**
     * Creates a new MitigationSpoiler object
     */
    public MitigationSpoiler() {
        this.parameterValues = null;
    }
    
    /**
     * Creates a mitigation spoiter from the list of parameter values
     * @param parameterValues the parameter values to be set
     */
    public MitigationSpoiler(List<MethodParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }
    
    /**
     * Creates a mitigation spoiler from the given parameter values. This variable argument is just form
     * convenience
     * @param parameterValues one or more parameter values to be set
     */
    public MitigationSpoiler(MethodParameterValue... parameterValues) {
        this.parameterValues = Arrays.asList(parameterValues);
    }

    /**
     * Gets the parameter values for this object
     * @return the parameter values for this object 
     */
    public List<MethodParameterValue> getParameterValues() {
        return parameterValues;
    }  
    
    
}
