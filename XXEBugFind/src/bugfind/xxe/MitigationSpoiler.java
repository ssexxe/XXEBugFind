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
 *
 * @author Mikosh
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class MitigationSpoiler {
    @XmlElement (name = "ParameterValue")
    private final List<MethodParameterValue> parameterValues;

    public MitigationSpoiler() {
        this.parameterValues = null;
    }
    
    public MitigationSpoiler(List<MethodParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }
    
    public MitigationSpoiler(MethodParameterValue... parameterValues) {
        this.parameterValues = Arrays.asList(parameterValues);
    }

    public List<MethodParameterValue> getParameterValues() {
        return parameterValues;
    }  
    
    
}
