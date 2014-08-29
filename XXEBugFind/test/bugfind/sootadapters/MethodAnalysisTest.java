/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import bugfind.xxe.MethodParameterValue;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.Stmt;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

/**
 *
 * @author Mikosh
 */
public class MethodAnalysisTest {
    
    public MethodAnalysisTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isCalledInMethod method, of class MethodAnalysis.
     */
    @Test
    public void testIsCalledInMethod() {
        System.out.println("isCalledInMethod");
        SootMethod targetCaller = null;
        SootMethod callee = null;
        MethodAnalysis instance = null;
        boolean expResult = false;
        boolean result = instance.isCalledInMethod(targetCaller, callee);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compare method, of class MethodAnalysis.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        SootMethod callerMethod = null;
        SootMethod callee1 = null;
        SootMethod callee2 = null;
        MethodAnalysis instance = null;
        int expResult = 0;
        int result = instance.compare(callerMethod, callee1, callee2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCallsBefore method, of class MethodAnalysis.
     */
    @Test
    public void testGetAllCallsBefore_4args() {
        System.out.println("getAllCallsBefore");
        SootMethod callerMethod = null;
        SootMethod calleeToBeBefore = null;
        SootMethod callee2 = null;
        Unit callee2Location = null;
        MethodAnalysis instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.getAllCallsBefore(callerMethod, calleeToBeBefore, callee2, callee2Location);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCallsBefore method, of class MethodAnalysis.
     */
    @Test
    public void testGetAllCallsBefore_3args_1() {
        System.out.println("getAllCallsBefore");
        SootMethod callerMethod = null;
        SootMethod calleeToBeBefore = null;
        CallSite targetCS = null;
        MethodAnalysis instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.getAllCallsBefore(callerMethod, calleeToBeBefore, targetCS);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCallsBefore method, of class MethodAnalysis.
     */
    @Test
    public void testGetAllCallsBefore_3args_2() {
        System.out.println("getAllCallsBefore");
        SootMethod callerMethod = null;
        SootMethod calleeToBeBefore = null;
        Unit targetUnit = null;
        MethodAnalysis instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.getAllCallsBefore(callerMethod, calleeToBeBefore, targetUnit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCallGraph method, of class MethodAnalysis.
     */
    @Test
    public void testSetCallGraph() {
        System.out.println("setCallGraph");
        CallGraph callGraph = null;
        MethodAnalysis instance = null;
        instance.setCallGraph(callGraph);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallGraph method, of class MethodAnalysis.
     */
    @Test
    public void testGetCallGraph() {
        System.out.println("getCallGraph");
        MethodAnalysis instance = null;
        CallGraph expResult = null;
        CallGraph result = instance.getCallGraph();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertToVariable method, of class MethodAnalysis.
     */
    @Test
    public void testConvertToVariable_JArrayRef() {
        System.out.println("convertToVariable");
        JArrayRef ar = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.convertToVariable(ar);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertToVariable method, of class MethodAnalysis.
     */
    @Test
    public void testConvertToVariable_Value() {
        System.out.println("convertToVariable");
        Value v = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.convertToVariable(v);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefinedVariable method, of class MethodAnalysis.
     */
    @Test
    public void testGetDefinedVariable() {
        System.out.println("getDefinedVariable");
        JAssignStmt stmt = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.getDefinedVariable(stmt);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvokedVariable method, of class MethodAnalysis.
     */
    @Test
    public void testGetInvokedVariable_JAssignStmt() {
        System.out.println("getInvokedVariable");
        JAssignStmt stmt = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.getInvokedVariable(stmt);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvokedVariable method, of class MethodAnalysis.
     */
    @Test
    public void testGetInvokedVariable_JInvokeStmt() {
        System.out.println("getInvokedVariable");
        JInvokeStmt stmt = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.getInvokedVariable(stmt);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvokedVariable method, of class MethodAnalysis.
     */
    @Test
    public void testGetInvokedVariable_JVirtualInvokeExpr() {
        System.out.println("getInvokedVariable");
        JVirtualInvokeExpr invokeExpr = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.getInvokedVariable(invokeExpr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareArguments method, of class MethodAnalysis.
     */
    @Test
    public void testCompareArguments() {
        System.out.println("compareArguments");
        CallSite cs = null;
        List<MethodParameterValue> listMPV = null;
        MethodAnalysis instance = null;
        int expResult = 0;
        int result = instance.compareArguments(cs, listMPV);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of converIntToBoolean method, of class MethodAnalysis.
     */
    @Test
    public void testConverIntToBoolean() {
        System.out.println("converIntToBoolean");
        int val = 0;
        MethodAnalysis instance = null;
        String expResult = "";
        String result = instance.converIntToBoolean(val);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isAssignedTo method, of class MethodAnalysis.
     */
    @Test
    public void testIsAssignedTo() {
        System.out.println("isAssignedTo");
        SootMethod parentMethod = null;
        String assignerClassName = "";
        String assigneeClassName = "";
        MethodAnalysis instance = null;
        boolean expResult = false;
        boolean result = instance.isAssignedTo(parentMethod, assignerClassName, assigneeClassName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssignerVariable method, of class MethodAnalysis.
     */
    @Test
    public void testGetAssignerVariable() {
        System.out.println("getAssignerVariable");
        SootMethod parentMethod = null;
        String assignerClassName = "";
        String assigneeClassName = "";
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.getAssignerVariable(parentMethod, assignerClassName, assigneeClassName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvokedVariable method, of class MethodAnalysis.
     */
    @Test
    public void testGetInvokedVariable_Stmt() {
        System.out.println("getInvokedVariable");
        Stmt stmt = null;
        MethodAnalysis instance = null;
        Variable expResult = null;
        Variable result = instance.getInvokedVariable(stmt);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInvokedOnSameVariable method, of class MethodAnalysis.
     */
    @Test
    public void testIsInvokedOnSameVariable() {
        System.out.println("isInvokedOnSameVariable");
        Stmt stmt1 = null;
        Stmt stmt2 = null;
        MethodAnalysis instance = null;
        boolean expResult = false;
        boolean result = instance.isInvokedOnSameVariable(stmt1, stmt2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of filterByVariable method, of class MethodAnalysis.
     */
    @Test
    public void testFilterByVariable() {
        System.out.println("filterByVariable");
        List<CallSite> listCS = null;
        Variable filter = null;
        MethodAnalysis instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.filterByVariable(listCS, filter);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of constantToString method, of class MethodAnalysis.
     */
    @Test
    public void testConstantToString() {
        System.out.println("constantToString");
        Constant c = null;
        boolean isBoolean = false;
        MethodAnalysis instance = null;
        String expResult = "";
        String result = instance.constantToString(c, isBoolean);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resolveValue method, of class MethodAnalysis.
     */
    @Test
    public void testResolveValue() {
        System.out.println("resolveValue");
        Value value = null;
        CallSite cs = null;
        MethodAnalysis instance = null;
        ValueString expResult = null;
        ValueString result = instance.resolveValue(value, cs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSootValue method, of class MethodAnalysis.
     */
    @Test
    public void testGetSootValue() {
        System.out.println("getSootValue");
        Variable v = null;
        SootMethod parentMethod = null;
        MethodAnalysis instance = null;
        Local expResult = null;
        Local result = instance.getSootValue(v, parentMethod);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallType method, of class MethodAnalysis.
     */
    @Test
    public void testGetCallType() {
        System.out.println("getCallType");
        Edge edge = null;
        MethodAnalysis instance = null;
        int expResult = 0;
        int result = instance.getCallType(edge);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ensureNonStaticExpr method, of class MethodAnalysis.
     */
    @Test
    public void testEnsureNonStaticExpr() {
        System.out.println("ensureNonStaticExpr");
        Stmt stmt = null;
        MethodAnalysis instance = null;
        instance.ensureNonStaticExpr(stmt);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
