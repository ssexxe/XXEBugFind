/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

/**
 *
 * @author Mikosh
 */
public class Variable {
    public static final int STATIC_VARIABLE = - 5, FIELD_VARIABLE = -10, LOCAL_VARIABLE = -2;
    protected String name;
    protected String type;
    protected int level;

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
    
    
    
}
