/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import soot.SootMethod;

/**
 * A soot method wrapper
 * @author Mikosh
 */
public class SootMethodWrapper {
    private SootMethod sootMethod;

    /**
     * Creates a new soot method wrapper
     * @param sootMethod 
     */
    public SootMethodWrapper(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

    /**
     * @return the sootMethod
     */
    public SootMethod getSootMethod() {
        return sootMethod;
    }

    /**
     * @param sootMethod the sootMethod to set
     */
    public void setSootMethod(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }
    
    
    
    
    
    
    
    
}
