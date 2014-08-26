/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import bugfind.xxe.MethodParameterValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Local;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.IntConstant;
import soot.jimple.Stmt;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JNeExpr;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.JimpleLocalBox;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.CombinedAnalysis;
import soot.toolkits.scalar.CombinedDUAnalysis;

/**
 *
 * @author Mikosh
 */
public class MethodAnalysis {
    public static final int CALLED_BEFORE = -1, CALLED_AFTER = 1, CALLED_SAME_TIME = 0;
    public static final int INDETERMINABLE_ARGUMENT_VALUES = -99, SAME_ARGUMENT_VALUES = -78, DIFF_ARGUMENT_VALUES = -45;
    private static final int START = 0;
    private CallGraphObject cgo;
    private CallGraph callGraph;

    public MethodAnalysis(CallGraphObject cgo, CallGraph callGraph) {
        this.cgo = cgo;
        this.callGraph = callGraph;
    }

    
    public boolean isCalledInMethod(SootMethod targetCaller, SootMethod callee) {
        List<CallSite> listCS = this.cgo.getCallSites(callGraph, callee);
        for (CallSite cs : listCS) {
            if (cs.getSourceMethod().getSignature().equals(targetCaller.getSignature())) {
                return true;
            }
        }
        
        return false;
    }
    
   
    
    public int compare(SootMethod callerMethod, SootMethod callee1, SootMethod callee2) {
        if (!isCalledInMethod(callerMethod, callee1)) {
            throw new RuntimeException("Specified callee method not called in specified soot caller method.\nDetail: "
                    + callee1 + " is never called in " + callerMethod);            
        }
        if (!isCalledInMethod(callerMethod, callee2)) {
            throw new RuntimeException("Specified callee method not called in specified soot caller method.\nDetail: "
                    + callee2 + " is never called in " + callerMethod);            
        }
        
        List<CallSite> listCS1 = cgo.getCallSitesInMethod(callGraph, callee1, callerMethod);
        Collections.sort(listCS1);
        List<CallSite> listCS2 = cgo.getCallSitesInMethod(callGraph, callee2, callerMethod);
        Collections.sort(listCS2);
        
        if (listCS1.get(0).getLineLocation() < listCS2.get(0).getLineLocation()) {
            return CALLED_BEFORE;
        }
        else if (listCS1.get(0).getLineLocation() > listCS2.get(0).getLineLocation()) {
            return CALLED_AFTER;
        }
        else if (listCS1.get(0).getLineLocation() == listCS2.get(0).getLineLocation()) {
            throw new RuntimeException("both are on same line. try to get which statement is run first on line");
            //return CALLED_SAME_TIME;
        }
        else {
            throw new RuntimeException("Code should not reach here");
        }
    }
    
    public List<CallSite> getAllCallsBefore(SootMethod callerMethod, SootMethod calleeToBeBefore, SootMethod callee2) {
        if (!isCalledInMethod(callerMethod, calleeToBeBefore)) {
            throw new RuntimeException("Specified callee method not called in specified soot caller method.\nDetail: "
                    + calleeToBeBefore + " is never called in " + callerMethod);            
        }
        if (!isCalledInMethod(callerMethod, callee2)) {
            throw new RuntimeException("Specified callee method not called in specified soot caller method.\nDetail: "
                    + callee2 + " is never called in " + callerMethod);            
        }
        
        List<CallSite> listCBefore = cgo.getCallSitesInMethod(callGraph, calleeToBeBefore, callerMethod);
        Collections.sort(listCBefore);
        List<CallSite> listCS2 = cgo.getCallSitesInMethod(callGraph, callee2, callerMethod);
        Collections.sort(listCS2);
        
        Iterator<CallSite> ite = listCBefore.iterator();
        
        while (ite.hasNext()) {
            CallSite cs = ite.next();
            
            if (cs.getLineLocation() >= listCS2.get(0).getLineLocation()) {// remeber to account for same line
                ite.remove();
            }
        }
        
        return listCBefore;
    }

    public void setCallGraph(CallGraph callGraph) {
        this.callGraph = callGraph;
    }

    public CallGraph getCallGraph() {
        return callGraph;
    }
    
    protected Variable convertToVariable(JArrayRef ar) {        
        Type arrayRefType = ar.getType();
        String typeName = null;
        
        if (arrayRefType instanceof PrimType) {
            typeName = ((PrimType) arrayRefType).getClass().getName();
        } else if (arrayRefType instanceof RefType) {
            typeName = ((RefType) arrayRefType).getClassName();
        } else if (arrayRefType instanceof NullType) {
            typeName = null; //NSOT//((NullType)localvartype).getClassName();
        } else if (arrayRefType instanceof ArrayType) {
            typeName = arrayRefType.toString();
        } else {
            throw new RuntimeException("Currently cannot support variable of type " + arrayRefType);
        }

        Value base = ar.getBase();
        int varType = 0;
        String varName = null;
        
        if (base instanceof JimpleLocal) {
            varName = ((JimpleLocal)base).getName() + "[" + ar.getIndex() + "]";
            varType = Variable.LOCAL_VARIABLE;
        }
        else if (base instanceof JInstanceFieldRef) {            
            JInstanceFieldRef fr = (JInstanceFieldRef) base;
            SootField sf = fr.getField();
            varName = sf.getName() + "[" + ar.getIndex() + "]";            
            varType = (sf.isStatic()) ? Variable.STATIC_VARIABLE
                    : Variable.FIELD_VARIABLE;
        }
        else if (base instanceof SootField) {
            SootField sf = (SootField) base;
            varName = sf.getName() + "[" + ar.getIndex() + "]";
            varType = (sf.isStatic()) ? Variable.STATIC_VARIABLE
                    : Variable.FIELD_VARIABLE;
        }        
        
        Variable retVal = new Variable(varName, typeName, varType);
        return retVal;
    }
    
    protected Variable convertToVariable(Value v) {
        if (v instanceof JimpleLocal) {
            JimpleLocal jl = (JimpleLocal) v;

            Type localvartype = v.getType();

            String typeName = null;
            if (localvartype instanceof PrimType) {
                typeName = ((PrimType) localvartype).getClass().getName();
            } else if (localvartype instanceof RefType) {
                typeName = ((RefType) localvartype).getClassName();
            } else if (localvartype instanceof NullType) {
                typeName = null; //NSOT//((NullType)localvartype).getClassName();
            } else if (localvartype instanceof ArrayType) {
                typeName = localvartype.toString();
            } else {
                throw new RuntimeException("Currently cannot support variable of type " + localvartype);
            }
            Variable retVal = new Variable(jl.getName(), typeName, Variable.LOCAL_VARIABLE);
            return retVal;
        } else if (v instanceof JInstanceFieldRef) {
            JInstanceFieldRef fr = (JInstanceFieldRef) v;

            SootField sf = fr.getField();

            Type sootfieldType = v.getType();

            String typeName = null;
            if (sootfieldType instanceof PrimType) {
                typeName = ((PrimType) sootfieldType).getClass().getName();
            } else if (sootfieldType instanceof RefType) {
                typeName = ((RefType) sootfieldType).getClassName();
            } else if (sootfieldType instanceof NullType) {
                typeName = null; //NSOT//((NullType)localvartype).getClassName();
            } else if (sootfieldType instanceof ArrayType) {
                typeName = sootfieldType.toString();
            } else {
                throw new RuntimeException("Currently cannot support variable of type " + sootfieldType);
            }

            Variable retVal = new Variable(sf.getName(), typeName, (sf.isStatic()) ? Variable.STATIC_VARIABLE
                    : Variable.FIELD_VARIABLE);
            return retVal;
        } else if (v instanceof SootField) {
            SootField sf = (SootField) v;

            Type sootfieldType = v.getType();

            String typeName = null;
            if (sootfieldType instanceof PrimType) {
                typeName = ((PrimType) sootfieldType).getClass().getName();
            } else if (sootfieldType instanceof RefType) {
                typeName = ((RefType) sootfieldType).getClassName();
            } else if (sootfieldType instanceof NullType) {
                typeName = null; //NSOT//((NullType)localvartype).getClassName();
            } else if (sootfieldType instanceof ArrayType) {
                typeName = sootfieldType.toString();
            } else {
                throw new RuntimeException("Currently cannot support variable of type " + sootfieldType);
            }

            Variable retVal = new Variable(sf.getName(), typeName, (sf.isStatic()) ? Variable.STATIC_VARIABLE
                    : Variable.FIELD_VARIABLE);
            return retVal;
        } else if (v instanceof JArrayRef) {
            JArrayRef jar = (JArrayRef) v;
            Value av = jar.getBase();
            return convertToVariable(av);
        } else {
            throw new RuntimeException("Currently cannot support value " + v);
        }
    }
    
    public Variable getDefinedVariable(JAssignStmt stmt) {
        Value v = stmt.getLeftOp();        
        return convertToVariable(v);        
    }
    
    public Variable getInvokedVariable(JAssignStmt stmt) {
        //first ensure statement is non static
        ensureNonStaticExpr(stmt);
        
        Value v = stmt.getRightOp(); 
        
        if (v instanceof JVirtualInvokeExpr) {
            return getInvokedVariable((JVirtualInvokeExpr)v);
        }
        else {
            return convertToVariable(v);
        }
    }
    
    public Variable getInvokedVariable(JInvokeStmt stmt) {       
        //first ensure statement is non static
        ensureNonStaticExpr(stmt);
        
        // the first use box of a jinvoke statement is the variable being used, others may be arguments
        Value v = ((ValueBox) stmt.getUseBoxes().get(0)).getValue();//(Value) stmt.getUseBoxes().get(0); 
        return convertToVariable(v);
    }
    
    protected Variable getInvokedVariable(JVirtualInvokeExpr invokeExpr) {       
        // the first use box of a jinvoke expression is the variable being used, others may be arguments
        Value v = ((ValueBox) invokeExpr.getUseBoxes().get(0)).getValue();
        return convertToVariable(v);
    }
    
    public int compareArguments(CallSite cs, List<MethodParameterValue> listMPV) {
        Edge edge = cs.getEdge();
        Stmt stmt = edge.srcStmt();
        if (!stmt.containsInvokeExpr()) {
            throw new RuntimeException("Edge statement does not contain an invoke expression. ie no method call in statement "
                    + "corresponding to edge");
        }
        
        List<Value> argumentList = stmt.getInvokeExpr().getArgs();
        if (argumentList.size() != listMPV.size()) {
            throw new RuntimeException("The method arguments are not compatible. ");            
        }
        
        //SootMethod calleeMeth = edge.tgt();
        
        int comparison = START;
        for (int i=0; i<argumentList.size(); ++i) {
            Value v = argumentList.get(i);
            MethodParameterValue mpv = listMPV.get(i);
            
            if (v.getType().toString().equals(mpv.getType())) { // ensure they are similar types before comparison
                if (v instanceof Constant) {// if it is instance of constant
                    Constant c = (Constant) v;
                    if (!c.toString().equals(mpv.getValue())) {//if (c.getType().toString().equals(mpv.getType())) {// if same type, check if values are the same                        
                        comparison = DIFF_ARGUMENT_VALUES;
                        break;
                    }

                } 
                else {// otherwise it is variable                    
                    ValueString paramVal = resolveValue(v, edge);
                    if (paramVal == null) {// ie could not resolve value of the variavle
                        comparison = INDETERMINABLE_ARGUMENT_VALUES;
                        break;
                    }
                    if (!paramVal.getValue().equals(mpv.getValue())) {// if values are not equal
                        comparison = DIFF_ARGUMENT_VALUES;
                        break;
                    }
                    
                }
            } 
            else if (v instanceof Constant && (mpv.getType().equals("boolean"))) {
                //  A special case for boolean constant as soot jimple converts it to int (ie no boolean type)
                // and so will will make type comparisons false
                // check if the original parameter type is boolean from method signature
                Constant c = (Constant) v;
                int aval = Integer.parseInt(c.toString());
                String boolVal = converIntToBoolean(aval);
                if (!boolVal.equals(mpv.getValue())) {
                    comparison = DIFF_ARGUMENT_VALUES;
                    break;
                }
            }        
            else {
                throw new RuntimeException("The method specified at the given callsite differs from the one compared with. "
                        + "Details:\nArgument " + (i) + " differs. Expected parameter type is " + mpv.getType() 
                        + " while argument type " + v.getType() + " was found at Call site");
            }
        }
        
        if (comparison == START) { // if comparison remains at start, it means there was no problem and all the arguments are equal
            comparison = SAME_ARGUMENT_VALUES;
        }
        
        return comparison;
    }
    
    protected final String converIntToBoolean(int val) {
        return (val == 0) ? "false" : "true";
    }
    
    public boolean isAssignedTo(SootMethod parentMethod, String assignerClassName, String assigneeClassName) {
        Iterator<Unit> iteUnits = parentMethod.getActiveBody().getUnits().iterator();
        
        while (iteUnits.hasNext()) {
            Unit unit = iteUnits.next();
            if (unit instanceof JAssignStmt) {
                JAssignStmt stmt = (JAssignStmt) unit;
                
                if (stmt.containsInvokeExpr()) {
                    Variable assigneeVar = getDefinedVariable(stmt);
                    if (assigneeVar.getType().equals(assigneeClassName)) {
                        //System.out.println("assigner typename: " + stmt.getRightOp().getType().g);
                        ValueBox vb = (ValueBox) stmt.getUseBoxes().get(0);
                        
                        if (((RefType)vb.getValue().getType()).getClassName().equals(assignerClassName)) {
                            return true;
                        }                        
                    }
                }
            } 
            
        }
        
        return false;
    }
    
    public Variable getAssignerVariable(SootMethod parentMethod, String assignerClassName, String assigneeClassName) {
        Iterator<Unit> iteUnits = parentMethod.getActiveBody().getUnits().iterator();
        
        while (iteUnits.hasNext()) {
            Unit unit = iteUnits.next();
            if (unit instanceof JAssignStmt) {
                JAssignStmt stmt = (JAssignStmt) unit;
                Variable assigneeVar = getDefinedVariable(stmt);
                if (stmt.containsInvokeExpr()) {
                    if (assigneeVar.getType().equals(assigneeClassName)) {
                        //System.out.println("assigner typename: " + stmt.getRightOp().getType().g);
                        ValueBox vb = (ValueBox) stmt.getUseBoxes().get(0);
                        
                        if (((RefType)vb.getValue().getType()).getClassName().equals(assignerClassName)) {
                            return getInvokedVariable(stmt);
                        }                        
                    }
                }
                       else if (stmt.getRightOp() instanceof JNewExpr) {// if instance of new expression, just return the left var
                    // ie AClassB b = new AClassB(); // ie return b, since b is not assigned to any other variable
                    return assigneeVar;
                }
            } 
            
        }
        
        return null;
    }
    
    public Variable getInvokedVariable(Stmt stmt) {
        if (stmt instanceof JInvokeStmt) {
            return getInvokedVariable((JInvokeStmt)stmt);
        }
        else if (stmt instanceof JAssignStmt) {
            return getInvokedVariable((JAssignStmt)stmt);
        }
        else {
            throw new RuntimeException("Statement type not supported. The statement not an assign or an invoke statement");
        }
    }
    
    public boolean isInvokedOnSameVariable(Stmt stmt1, Stmt stmt2) {
        Variable var1 = getInvokedVariable(stmt1);
        Variable var2 = getInvokedVariable(stmt2);
        if (var1.getName() == null || var2.getName() == null) {
            return false;
        }
        else {
            return (var1.getName().equals(var2.getName()));
        }          
    }
    
    public List<CallSite> filterByVariable(List<CallSite> listCS, Variable filter) {
        
        if (filter == null) {
            throw new RuntimeException("The variable filter given cannot be null");
        }
        
//        List<CallSite> retListCS = new ArrayList<>(listCS.size());
//        
//        for (CallSite cs: listCS) {
//            retListCS.add(cs);
//        }
        
        Iterator<CallSite> ite = listCS.iterator();        
        
        while (ite.hasNext()) {
            CallSite cs = ite.next();
            Stmt stmt = cs.getEdge().srcStmt();
            
            Variable vCS = getInvokedVariable(stmt);
            
            if (vCS == null) {
                ite.remove();
                continue;
            }
            if (!vCS.getName().equals(filter.getName()) || !vCS.getType().equals(filter.getType())) {
                ite.remove();                
            }
        }
        
        return listCS;
    }
    
    protected String constantToString(Constant c, boolean isBoolean) {
       
        if (isBoolean && c instanceof IntConstant) {
            if (!c.toString().equals("0") && !c.toString().equals("0")) {
                throw new RuntimeException("cannot convert int value " + c + " to boolean");
            }
            return (c.toString().equals("0")) ? "false" : "true";
                    
        }
        
        return c.toString();
    }
    
    protected ValueString resolveValue(Value value, Edge edge) {
        Variable v = convertToVariable(value);
        
        Local localrepr = getSootValue(v, edge.src());
        if (localrepr == null) {
            throw new RuntimeException("Cannot resolve variable " + v + " in edge " + edge + " with src-method " + edge.src());
        }
        
        UnitGraph uv = new ExceptionalUnitGraph(edge.src().getActiveBody());
        CombinedAnalysis ca = CombinedDUAnalysis.v(uv);
        List<Unit> list = ca.getDefsOfAt(localrepr, edge.srcStmt());
        if (list.isEmpty()) {
            return null;
        }
        
        if (list.size() > 1) {
            System.out.println("warning: number defsofvar > " + 1);
        }
        
        JAssignStmt stmt = (JAssignStmt) list.get(list.size()-1);
        
        if (stmt.getRightOp() instanceof Constant) {
            Constant constnt = (Constant) stmt.getRightOp();
            String valstr = constantToString(constnt, v.getType().equals("boolean"));
            
            return new ValueString(v.getType(), v.getName(), valstr);
        }
        else if (stmt.getRightOp() instanceof JimpleLocal) {
            return resolveValue(stmt.getRightOp(), edge);
        }
        else {// currently doesnt handle fieldref and static values
            return null;
        }
        
        
    }
    
    public Local getSootValue(Variable v, SootMethod parentMethod) {
        Iterator<Local> ite =  parentMethod.getActiveBody().getLocals().iterator();
        while (ite.hasNext()) {
            Local localvar = ite.next();
            if (localvar.getName().equals(v.getName()) && localvar.getType().toString().equals(v.getType())) {
                return localvar;
            }
        }
        
        return null;
    }
    
    protected int getCallType(Edge edge) {
        throw new UnsupportedOperationException("not supported yet");
    }
    
    protected void ensureNonStaticExpr(Stmt stmt) {
        if (!stmt.containsInvokeExpr()) {
            throw new RuntimeException("This statement is not an invoke expression");
        }
        if (stmt instanceof JStaticInvokeExpr) {
            throw new RuntimeException("this is a static statement");
        }
    }
    
    
}
