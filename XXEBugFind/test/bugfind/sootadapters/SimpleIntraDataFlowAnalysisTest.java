/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

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
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;

/**
 *
 * @author Mikosh
 */
public class SimpleIntraDataFlowAnalysisTest {
    
    public SimpleIntraDataFlowAnalysisTest() {
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
     * Test of setConstraints method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testSetConstraints() {
        System.out.println("setConstraints");
        SootMethod method = null;
        UnitGraph unitGraph = null;
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        instance.setConstraints(method, unitGraph);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefineLocations method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetDefineLocations() {
        System.out.println("getDefineLocations");
        Local local = null;
        Unit unit = null;
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        List<Unit> expResult = null;
        List<Unit> result = instance.getDefineLocations(local, unit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsesOfLocalDefinedHere method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetUsesOfLocalDefinedHere() {
        System.out.println("getUsesOfLocalDefinedHere");
        Unit definitionPoint = null;
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        List<Unit> expResult = null;
        List<Unit> result = instance.getUsesOfLocalDefinedHere(definitionPoint);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOccurrencesOfLocalBeforeHere method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetOccurrencesOfLocalBeforeHere() {
        System.out.println("getOccurrencesOfLocalBeforeHere");
        SootMethod parentMethod = null;
        Local local = null;
        Unit targetPoint = null;
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        List<Unit> expResult = null;
        List<Unit> result = instance.getOccurrencesOfLocalBeforeHere(parentMethod, local, targetPoint);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBefore method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testIsBefore_Unit_Unit() {
        System.out.println("isBefore");
        Unit unit1 = null;
        Unit unit2 = null;
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        boolean expResult = false;
        boolean result = instance.isBefore(unit1, unit2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBefore method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testIsBefore_3args() {
        System.out.println("isBefore");
        SootMethod parentMethod = null;
        Unit unit1 = null;
        Unit unit2 = null;
        boolean expResult = false;
        boolean result = SimpleIntraDataFlowAnalysis.isBefore(parentMethod, unit1, unit2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocalsOfType method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetLocalsOfType() {
        System.out.println("getLocalsOfType");
        String localType = "";
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        List<Local> expResult = null;
        List<Local> result = instance.getLocalsOfType(localType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterLocals method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetParameterLocals_0args() {
        System.out.println("getParameterLocals");
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        List<Local> expResult = null;
        List<Local> result = instance.getParameterLocals();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterLocals method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetParameterLocals_SootMethod() {
        System.out.println("getParameterLocals");
        SootMethod sm = null;
        List<Local> expResult = null;
        List<Local> result = SimpleIntraDataFlowAnalysis.getParameterLocals(sm);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvokedLocal method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetInvokedLocal() {
        System.out.println("getInvokedLocal");
        Unit unit = null;
        Value expResult = null;
        Value result = SimpleIntraDataFlowAnalysis.getInvokedLocal(unit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsedLocals method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetUsedLocals() {
        System.out.println("getUsedLocals");
        Unit unit = null;
        List<Value> expResult = null;
        List<Value> result = SimpleIntraDataFlowAnalysis.getUsedLocals(unit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ensureNonStaticExpr method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testEnsureNonStaticExpr() {
        System.out.println("ensureNonStaticExpr");
        Stmt stmt = null;
        SimpleIntraDataFlowAnalysis.ensureNonStaticExpr(stmt);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isParameterLocal method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testIsParameterLocal_Local() {
        System.out.println("isParameterLocal");
        Local l = null;
        SimpleIntraDataFlowAnalysis instance = new SimpleIntraDataFlowAnalysis();
        boolean expResult = false;
        boolean result = instance.isParameterLocal(l);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isParameterLocal method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testIsParameterLocal_SootMethod_Local() {
        System.out.println("isParameterLocal");
        SootMethod sm = null;
        Local l = null;
        boolean expResult = false;
        boolean result = SimpleIntraDataFlowAnalysis.isParameterLocal(sm, l);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterLocalIndex method, of class SimpleIntraDataFlowAnalysis.
     */
    @Test
    public void testGetParameterLocalIndex() {
        System.out.println("getParameterLocalIndex");
        SootMethod sm = null;
        Local l = null;
        int expResult = 0;
        int result = SimpleIntraDataFlowAnalysis.getParameterLocalIndex(sm, l);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
