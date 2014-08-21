/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bugfind.xxe;

/**
 *
 * @author Mikosh
 */
public final class MethodParameterValue {
    private String type;
    private String value;

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
