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
    protected String name;
    
    /**
     * The type of the variable
     */
    protected String type;
    
    /**
     * The level of the variable
     */
    protected int level;

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

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }
    
    public boolean isStatic() {
        return (level == STATIC_VARIABLE);
    }
    
    public boolean isLocal() {
        return (level == LOCAL_VARIABLE);
    }
    
    public boolean isField() {
        return (level == FIELD_VARIABLE);
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

    
    
    
}
