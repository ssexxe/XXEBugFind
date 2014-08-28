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
import soottest.GuaranteedDefs;

/**
 *
 * @author Mikosh
 */
public class SimpleIntraDataFlowAnalysis {
    private SootMethod method;
    //private ExceptionalUnitGraph exceptionalUnitGraph;
    private UnitGraph unitGraph;
    private GuaranteedDefs guaranteedDefs;
    private CombinedAnalysis combinedAnalysis;

    public SimpleIntraDataFlowAnalysis() {
    }

    public SimpleIntraDataFlowAnalysis(SootMethod method, UnitGraph unitGraph) {
        setConstraints(method, unitGraph);
    }
    
    public void setConstraints(SootMethod method, UnitGraph unitGraph) {
        this.method = method;
        this.unitGraph = unitGraph;
        combinedAnalysis = CombinedDUAnalysis.v(this.unitGraph);
        guaranteedDefs = new GuaranteedDefs(this.unitGraph);
    }
    
    
    public List<Unit> getDefineLocations(Local local, Unit unit) {
        List<Unit> retlist = combinedAnalysis.getDefsOfAt(local, unit);
        
        // now sort to ensure that list puts the latter definition after the initial definition. This is 
        // important as the latter definition must override the intial definition
        
        return retlist;
    }
    
    public List<Unit> getUsesOfLocalDefinedHere(Unit definitionPoint) {
        return combinedAnalysis.getUsesOf(definitionPoint);
    }
    
    public List<Unit> getOccurrencesOfLocalBeforeHere(SootMethod parentMethod, Local local, Unit targetPoint) {
        Body body = parentMethod.getActiveBody();
        PatchingChain<Unit> pUnits = body.getUnits();
        Iterator<Unit> ite = pUnits.iterator();
        
        List<Unit> matchingUnits = new ArrayList<>();
        
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
   
    public List<Local> getParameterLocals() {
        return getParameterLocals(method);
    }
    
    public static List<Local> getParameterLocals(SootMethod sm) {
        List<Local> list = new ArrayList<>();
        int paramCount = sm.getParameterCount();
        
        for (int i=0; i<paramCount; ++i) {
            list.add(sm.getActiveBody().getParameterLocal(i));
        }
        
        return list;
    }
    
    public static Value getInvokedLocal(Unit unit) {
        ensureNonStaticExpr((Stmt) unit);
        return unit.getUseBoxes().get(0).getValue();
    }
    
    public static List<Value> getUsedLocals(Unit unit) {
        List<ValueBox> lVB = unit.getUseBoxes();
        
        List<Value> retList = new ArrayList<>();
        for (ValueBox vb : lVB) {
            retList.add(vb.getValue());
        }
        return retList;
    }
    
    
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
    
    public static boolean isParameterLocal(SootMethod sm, Local l) {
        List<Local> list = getParameterLocals(sm);
        return list.contains(l);
    }
    
    public static int getParameterLocalIndex(SootMethod sm, Local l) {
        List<Local> list = getParameterLocals(sm);
        return list.indexOf(l);
    }
    
}
