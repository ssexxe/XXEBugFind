/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

/**
 * Encapsulates the value of a variable in string form. It stores the type, name and value as a string. For simple 
 * types like int, float, char, the value can retrieved by appropriate conversion mechanisms.
 * It has only getter methods to make these objects immutable.
 * @author Mikosh
 */
public class ValueString {
    private String type;
    private String name;
    private String value;

    /**
     * Creates a new ValueString object
     * @param type the type pf the variable
     * @param name the name of the variable
     * @param value the string form corresponding to the value of the variable
     */
    public ValueString(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Type: ").append(getType()).append(" Name: ")
                .append(getName()).append(" Value: ").append(getValue());
        return sb.toString();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    
    
    
}
