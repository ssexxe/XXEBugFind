/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.util.Chain;

/**
 * This is a soot SootClass wrapper for use by the application. This provides a clean interface for the application
 * to work without knowing the details of the SootClass under the hood
 * @author Mikosh
 */
public class SootClassWrapper {
    /**
     * Holds a reference to it's soot class
     */
    protected SootClass sootClass;

    /**
     * Creates a SootClassWrapper from the specified SootClass
     * @param sootClass the soot class to wrap
     */
    public SootClassWrapper(SootClass sootClass) {
        this.sootClass = sootClass;
    }
    
    /**
     * Get all concrete methods declared in this class
     * @return a list containing all concrete methods returned in this class
     */
    public List<SootMethod> getAllMethodsDeclaredInThisClassOnly() {
        List<SootMethod> methods = getSootClass().getMethods();        
        return methods;
    }
    
    /**
     * Gets all the methods declared in this class including inherited ones
     * @return all the methods declared in this class including inherited ones 
     */
    public List<SootMethod> getAllMethodsDeclaredIncludingInherited() {
        List<SootMethod> methods = getSootClass().getMethods();
        
        // if it has no superclass (ie it is java.lang.Object) or if super class is Object class (which all java classes inherit) 
        // then return just the methods in this class
        if (!getSootClass().hasSuperclass() || getSootClass().getSuperclass().getName().equals("java.lang.Object")) {
            return methods;
        }        
        
        SootClass superClass = getSootClass().getSuperclass();         
        
        while(superClass != null) {// while the superclasses are not null, add the methods
            // Get the declared methods in superclass and add to this method. 
            addMethodsFromSuperClassToSpecifiedMethodList(superClass, methods);       

            ensureIsSupported(superClass);
            // if it doesnt have a super class or it is the java.lang.Object, break the loop
            if (!superClass.hasSuperclass() || superClass.getSuperclass().getName().equals("java.lang.Object")) {
                break;
            } else { // else get a reference to the superclass
                superClass = superClass.getSuperclass();
            }
        }
        
        // now add for interfaces
        //Scene.v().getFastHierarchy().getSuperinterfacesOf(getSootClass())
        Set<SootClass> setIfaces = new HashSet<>();//getSootClass().getSuperclass().getInterfaces().isApplicationClass()
        SootClass currClass = getSootClass();
        while(currClass != null) {
            Chain<SootClass> ifaces = currClass.getInterfaces();
            for (SootClass iface : ifaces) {
                setIfaces.add(iface);
            }
            currClass = (currClass.hasSuperclass()) ? currClass.getSuperclass() : null;
        }        
        
        for (SootClass iface : setIfaces) {
            ensureIsSupported(iface);
            // add methods from all implemeted interfaces
            addMethodsFromSuperClassToSpecifiedMethodList(iface, methods);
        }
        
        return methods;
    }
    
    /**
     * Adds method from super class to the specified method list
     * @param superClass the super class
     * @param methods the list which will contain the added methods
     */
    protected void addMethodsFromSuperClassToSpecifiedMethodList(SootClass superClass, List<SootMethod> methods) {
        List<SootMethod> superMethods = superClass.getMethods();
            List<SootMethod> tmpList = new ArrayList<>();

            // add only methods that are not overridden by this subclass
            for (SootMethod superMethod : superMethods) {
                boolean shouldAdd = true;
                for (SootMethod subMeth : methods) {
                    // if they are same name and same parameters, then that means this class has overridden that method, 
                    // also do no include static initializer methods
                    if ((subMeth.getName().equals(superMethod.getName()) && 
                            subMeth.getParameterTypes().equals(superMethod.getParameterTypes())) || superMethod.getName().contains("<clinit>")) {
                        shouldAdd = false;
                        break;
                    }
                }

                if (shouldAdd) {
                    tmpList.add(superMethod);
                }
            }

            // if tempList is not empty, now add the methods to the methods that will be returned
            if (tmpList.size() > 0) {
                for (SootMethod meth : tmpList) {
                    methods.add(meth);
                }
            }
    }

    /**
     * Gets the soot class
     * @return  the soot class
     */
    public SootClass getSootClass() {
        return sootClass;
    }

    /**
     * Sets the soot class
     * @param sootClass the soot class to set
     */
    public void setSootClass(SootClass sootClass) {
        this.sootClass = sootClass;
    }
    
    /**
     * Ensure the given class is supported by soot. This is by loading it
     * @param sc the soot class to use
     * @return true if the class was reloaded 
     */
    private static boolean ensureIsSupported(SootClass sc) {
        boolean neededSupport = false;
        if (!sc.isApplicationClass()) {
            Scene.v().loadClassAndSupport(sc.getName());
            //NSOT//sc.setApplicationClass();
            neededSupport = true;
        }
        return neededSupport;
    }
    
}
