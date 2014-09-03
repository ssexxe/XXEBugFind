/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.CombinedAnalysis;
import soot.toolkits.scalar.CombinedDUAnalysis;
import soot.toolkits.scalar.GuaranteedDefs;

/**
 * This class contains some utility methods for simple-intra dataflow analysis
 * @author Mikosh
 */
public class SimpleIntraDataFlowAnalysis {
    private SootMethod method;
    private UnitGraph unitGraph;
    private GuaranteedDefs guaranteedDefs;
    private CombinedAnalysis combinedAnalysis;

    /**
     * Creates a simple intra data flow analysis
     */
    public SimpleIntraDataFlowAnalysis() {
    }

    /**
     * Creates a simple intra data flow analysis when given the method and the unit graph
     * @param method the method
     * @param unitGraph the unit graph
     */
    public SimpleIntraDataFlowAnalysis(SootMethod method, UnitGraph unitGraph) {
        setConstraints(method, unitGraph);
    }

    /**
     * Sets constraints used by this class
     * @param method the method to use
     * @param unitGraph the unit graph to use
     */
    public void setConstraints(SootMethod method, UnitGraph unitGraph) {
        this.method = method;
        this.unitGraph = unitGraph;
        combinedAnalysis = CombinedDUAnalysis.v(this.unitGraph);
        guaranteedDefs = new GuaranteedDefs(this.unitGraph);
    }
    
    /**
     * Get definition locations for the specified local that is used in the specified unit
     * @param local the local
     * @param unit the unit that contains the local
     * @return  the definition locations for the specified local that is used in the specified unit or an empty list
     * if local is not defined in unit's parent method or the local is not contained in the unit's statement
     */
    public List<Unit> getDefineLocations(Local local, Unit unit) {
        List<Unit> retlist = combinedAnalysis.getDefsOfAt(local, unit);
        
        // now sort to ensure that list puts the latter definition after the initial definition. This is 
        // important as the latter definition must override the intial definition
        
        return retlist;
    }
    
    /**
     * Gets a list of the Units that use the Local that is defined by a given Unit. 
     * @param definitionPoint the unit where the local whose uses is to be obtained is defined
     * @return  a list of the Units that use the Local that is defined by a given Unit. 
     */
    public List<Unit> getUsesOfLocalDefinedHere(Unit definitionPoint) {
        return combinedAnalysis.getUsesOf(definitionPoint);
    }
    
    /**
     * Gets the occurrences of a local before the specified target unit whether it be a definition or use case in the specified parent method
     * @param parentMethod the parent method
     * @param local the local
     * @param targetPoint the target unit
     * @return the occurrences of a local before the specified target unit whether it be a definition or use case in the specified parent method
     */
    public List<Unit> getOccurrencesOfLocalBeforeHere(SootMethod parentMethod, Local local, Unit targetPoint) {
        Body body = parentMethod.getActiveBody();
        // get all the statements in the parent method
        PatchingChain<Unit> pUnits = body.getUnits();
        Iterator<Unit> ite = pUnits.iterator();
        
        List<Unit> matchingUnits = new ArrayList<>();

        // contine until the target unit is reached
        while (ite.hasNext()) {
            Unit u = ite.next();            
            if (u == targetPoint || pUnits.follows(u, targetPoint)) {
                break;
            }
            
            List<ValueBox> lst = u.getUseAndDefBoxes();
            for (ValueBox vb : lst) {
                if (vb.getValue().equals(local)) {
                    matchingUnits.add(u);
                    break;
                }
            }
        }
        
        return matchingUnits;
    }
    
    /**
     * Returns whether a unit 1 is before unit 2 in the method body. Throws 
     * @param unit1 unit 1
     * @param unit2 unit 2
     * @return true if unit 1 is before unit 2, and false if otherwise
     * @throws RuntimeException throws a runtime exception if the units do not belong to the same method
     */
    public boolean isBefore(Unit unit1, Unit unit2) {//JAssignStmt
        //throw new UnsupportedOperationException("not supported yet");        
        Iterator<Unit> ite = method.getActiveBody().getUnits().iterator(unit1, unit2);
        if (ite.hasNext()) {
            return true;
        }
        else {
            ite = method.getActiveBody().getUnits().iterator(unit2, unit1);
            if (ite.hasNext()) {
                return false;
            }
            else {
                throw new RuntimeException("Cannot determine whether " + unit1 + " is before " + unit2 
                        + " or not");
            }
        }
    }
    
    /**
     * Returns whether a unit 1 is before unit 2 in the same parent method body. Throws 
     * @param parentMethod the parent method
     * @param unit1 unit 1
     * @param unit2 unit 2
     * @return true if unit 1 is before unit 2, and false if otherwise
     * @throws RuntimeException throws a runtime exception if the units do not belong to the same method
     */
    public static boolean isBefore(SootMethod parentMethod, Unit unit1, Unit unit2) {//JAssignStmt
        //throw new UnsupportedOperationException("not supported yet");        
        Iterator<Unit> ite = parentMethod.getActiveBody().getUnits().iterator(unit1, unit2);
        if (ite.hasNext()) {
            return true;
        }
        else {
            ite = parentMethod.getActiveBody().getUnits().iterator(unit2, unit1);
            if (ite.hasNext()) {
                return false;
            }
            else {
                throw new RuntimeException("Cannot determine whether " + unit1 + " is before " + unit2 
                        + " or not");
            }
        }
    }
    
    /**
     * Get locals of the specified type in the method pointed to by this object
     * @param localType the type
     * @return  locals of the specified type in the method pointed to by this object or an empty list if none exists
     */
    public List<Local> getLocalsOfType(String localType) {
        Iterator<Local> ite = method.getActiveBody().getLocals().iterator();
        List<Local> list = new ArrayList<>();
        while (ite.hasNext()) {
            Local l = ite.next();
            if (l.getType().toString().equals(localType)) {
                list.add(l);
            }
        }
       
        return list;
    }
   
    /**
     * Gets the parameter locals for the method
     * @return the parameter locals for the method 
     */
    public List<Local> getParameterLocals() {
        return getParameterLocals(method);
    }
    
    /**
     * Gets the parameter locals for the specified method 
     * @param sm the method to use
     * @return the parameter locals for the specified method  
     */
    public static List<Local> getParameterLocals(SootMethod sm) {
        List<Local> list = new ArrayList<>();
        int paramCount = sm.getParameterCount();
        
        for (int i=0; i<paramCount; ++i) {
            list.add(sm.getActiveBody().getParameterLocal(i));
        }
        
        return list;
    }
    
    /**
     * Get a soot value corresponding to the object invoked at the specified unit. 
     * Note a soot value can be a jimple-local.
     * @param unit the unit
     * @return  a soot value corresponding to the object invoked at the specified unit. 
     */
    public static Value getInvokedLocal(Unit unit) {
        ensureNonStaticExpr((Stmt) unit);
        return unit.getUseBoxes().get(0).getValue();
    }
    
    /**
     * Get the locals used in the specified statement
     * @param unit the unit to use
     * @return  the locals used in the specified statement
     */
    public static List<Value> getUsedLocals(Unit unit) {
        List<ValueBox> lVB = unit.getUseBoxes();
        
        List<Value> retList = new ArrayList<>();
        for (ValueBox vb : lVB) {
            retList.add(vb.getValue());
        }
        return retList;
    }
    
    /**
     * Used to ensure that the method invocation in the method is non static. Throws an exception if this is the case
     * otherwise it just returns
     * 
     * @param stmt the statement to be tested
     */
    protected static void ensureNonStaticExpr(Stmt stmt) {
        if (!stmt.containsInvokeExpr()) {
            throw new RuntimeException("This statement is not an invoke expression");
        }
        if (stmt instanceof JStaticInvokeExpr) {
            throw new RuntimeException("this is a static statement");
        }
    }
  
    /**
     * Returns true if the local passed is a parameter to this object's  soot method
     * @param l the local to be tested whether it is a parameter local or not
     * @return true if the local passed is a parameter to this object's  soot method 
     */
    public boolean isParameterLocal(Local l) {
        List<Local> list = getParameterLocals();
        return list.contains(l);
    }
    
    /**
     * Checks if the specified local is a parameter local in the specified soot method
     * @param sm the soot method to check
     * @param l the local to be tested
     * @return  true if the specified local is a parameter local in the specified soot method, false otherwise
     */
    public static boolean isParameterLocal(SootMethod sm, Local l) {
        List<Local> list = getParameterLocals(sm);
        return list.contains(l);
    }
    
    /**
     * Gets the index of the parameter local in the specified method.
     * @param sm the specified method
     * @param l the parameter local whose index is to be obtained
     * @return  the index of the parameter local in the specified method or -1 if the local is not a parameter for
     * the specified method
     */
    public static int getParameterLocalIndex(SootMethod sm, Local l) {
        List<Local> list = getParameterLocals(sm);
        return list.indexOf(l);
    }
    
}
