/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

/**
 * This is Variable object and denotes a variable. It provides a uniform interface for soot's JimpleLocal, JInstanceFieldRef, etc
 * It hold enough information about the variable like the name, type and level (either static, field or local).
 * @author Mikosh
 */
public class Variable {
    /**
     * The three allowable levels of a variable
     */
    public static final int STATIC_VARIABLE = - 5, FIELD_VARIABLE = -10, LOCAL_VARIABLE = -2;
    /**
     * The name of the variable
     */
    private String name;
    
    /**
     * The type of the variable
     */
    private String type;
    
    /**
     * The level of the variable
     */
    private int level;

    /**
     * Constructs a Variable object when given the name, type and level
     * @param name the name of the variable
     * @param type the type of the variable
     * @param level the level of the variable. Values are either (Variable.STATIC_VARIABLE, FIELD_VARIABLE, LOCAL_VARIABLE)
     */
    public Variable(String name, String type, int level) {
        this.name = name;
        this.type = type;
        this.level = level;
    }

    
    /**
     * Returns true if this variable is static, or false otherwise
     * @return true if this variable is static, or false otherwise 
     */
    public boolean isStatic() {
        return (getLevel() == STATIC_VARIABLE);
    }
    
    /**
     * Returns true if this variable is static, or false otherwise
     * @return true if this variable is static, or false otherwise 
     */
    public boolean isLocal() {
        return (getLevel() == LOCAL_VARIABLE);
    }
    
    /**
     *  true if this variable is a field, or false otherwise
     * @return true if this variable is a field, or false otherwise 
     */
    public boolean isField() {
        return (getLevel() == FIELD_VARIABLE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("var-name: ");
        sb.append(getName()).append(" var-type: ").append(getType()).append(" var-level: ");
        switch (getLevel()) {
            case LOCAL_VARIABLE:
                sb.append("LOCAL_VAR");
                break;
            case FIELD_VARIABLE:
                sb.append("FIELD_VAR");
                break;
            case STATIC_VARIABLE:
                sb.append("STATIC_VAR");
                break;
            default:
                sb.append("UNKNOWN_VAR_LEVEL");

        }
        return sb.toString();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    
    
    
}
