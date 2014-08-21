/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import soot.SootMethod;

/**
 *
 * @author Mikosh
 */
public class SootMethodWrapper {
    protected SootMethod sootMethod;

    public SootMethodWrapper(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }
    
    
    

    public SootMethod getSootMethod() {
        return sootMethod;
    }

    public void setSootMethod(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }
    
    
    
    
    
}
