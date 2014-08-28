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
public class SootMethodArgumentWrapper {
    private Object argumentValue;

    public SootMethodArgumentWrapper(Object argumentValue) {
        this.argumentValue = argumentValue;
    }

    public Object getArgumentValue() {
        return argumentValue;
    }

    public void setArgumentValue(Object argumentValue) {
        this.argumentValue = argumentValue;
    }
    
   
    
}
