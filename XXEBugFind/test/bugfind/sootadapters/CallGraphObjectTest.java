/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import bugfind.xxe.ActualVulnerabilityItem;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;

/**
 *
 * @author Mikosh
 */
public class CallGraphObjectTest {
    
    public CallGraphObjectTest() {
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
     * Test of runAnalysis method, of class CallGraphObject.
     */
    @Test
    public void testRunAnalysis() throws Exception {
        System.out.println("runAnalysis");
        CallGraphObject instance = null;
        instance.runAnalysis();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallingAncestors method, of class CallGraphObject.
     */
    @Test
    public void testGetCallingAncestors() {
        System.out.println("getCallingAncestors");
        CallGraph cg = null;
        SootMethod src = null;
        List<SootMethod> expResult = null;
        List<SootMethod> result = CallGraphObject.getCallingAncestors(cg, src);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallSites method, of class CallGraphObject.
     */
    @Test
    public void testGetCallSites() {
        System.out.println("getCallSites");
        CallGraph cg = null;
        SootMethod method = null;
        CallGraphObject instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.getCallSites(cg, method);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallSitesInMethod method, of class CallGraphObject.
     */
    @Test
    public void testGetCallSitesInMethod() {
        System.out.println("getCallSitesInMethod");
        CallGraph cg = null;
        SootMethod callee = null;
        SootMethod caller = null;
        CallGraphObject instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.getCallSitesInMethod(cg, callee, caller);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethodCallEdgesOutOfMethod method, of class CallGraphObject.
     */
    @Test
    public void testGetMethodCallEdgesOutOfMethod() {
        System.out.println("getMethodCallEdgesOutOfMethod");
        CallGraph cg = null;
        SootMethod method = null;
        CallGraphObject instance = null;
        List<CallSite> expResult = null;
        List<CallSite> result = instance.getMethodCallEdgesOutOfMethod(cg, method);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallStackTraces method, of class CallGraphObject.
     */
    @Test
    public void testGetCallStackTraces() {
        System.out.println("getCallStackTraces");
        CallGraph cg = null;
        SootMethod meth = null;
        List<SootMethod> atraceList = null;
        List<List<SootMethod>> mainList = null;
        CallGraphObject instance = null;
        List<List<SootMethod>> expResult = null;
        List<List<SootMethod>> result = instance.getCallStackTraces(cg, meth, atraceList, mainList);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of duplicateList method, of class CallGraphObject.
     */
    @Test
    public void testDuplicateList() {
        System.out.println("duplicateList");
        CallGraphObject instance = null;
        List expResult = null;
        List result = instance.duplicateList(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVulnerableMethodDefinitions method, of class CallGraphObject.
     */
    @Test
    public void testGetVulnerableMethodDefinitions() throws Exception {
        System.out.println("getVulnerableMethodDefinitions");
        String methodDefListAsString = "";
        CallGraphObject instance = null;
        List<MethodDefinition> expResult = null;
        List<MethodDefinition> result = instance.getVulnerableMethodDefinitions(methodDefListAsString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayExecutionTraceForMethod method, of class CallGraphObject.
     */
    @Test
    public void testDisplayExecutionTraceForMethod_CallGraph_MethodDefinition() {
        System.out.println("displayExecutionTraceForMethod");
        CallGraph cg = null;
        MethodDefinition md = null;
        CallGraphObject instance = null;
        instance.displayExecutionTraceForMethod(cg, md, null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayExecutionTraceForMethod method, of class CallGraphObject.
     */
    @Test
    public void testDisplayExecutionTraceForMethod_CallGraph_SootMethod() {
        System.out.println("displayExecutionTraceForMethod");
        CallGraph cg = null;
        SootMethod meth = null;
        CallGraphObject instance = null;
        instance.displayExecutionTraceForMethod(cg, meth, null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLineNumber method, of class CallGraphObject.
     */
    @Test
    public void testGetLineNumber() {
        System.out.println("getLineNumber");
        Stmt s = null;
        CallGraphObject instance = null;
        int expResult = 0;
        int result = instance.getLineNumber(s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printCallTraces method, of class CallGraphObject.
     */
    @Test
    public void testPrintCallTraces() {
        System.out.println("printCallTraces");
        ActualVulnerabilityItem avi = null;
        CallGraph cg = null;
        CallGraphObject instance = null;
        instance.printCallTraces(avi, cg, null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printStackTraces method, of class CallGraphObject.
     */
    @Test
    public void testPrintStackTraces() {
        System.out.println("printStackTraces");
        List<List<SootMethod>> list = null;
        CallGraphObject instance = null;
        instance.printStackTraces(list, null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadMyNecessaryClasses method, of class CallGraphObject.
     */
    @Test
    public void testLoadMyNecessaryClasses() {
        System.out.println("loadMyNecessaryClasses");
        CallGraphObject instance = null;
        instance.loadMyNecessaryClasses();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doTest2 method, of class CallGraphObject.
     */
    @Test
    public void testDoTest2() {
        System.out.println("doTest2");
        CallGraphObject.doTest2();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBugFindParametersMap method, of class CallGraphObject.
     */
    @Test
    public void testGetBugFindParametersMap() {
        System.out.println("getBugFindParametersMap");
        CallGraphObject instance = null;
        Map<String, String> expResult = null;
        Map<String, String> result = instance.getBugFindParametersMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getElevatedClasses method, of class CallGraphObject.
     */
    @Test
    public void testGetElevatedClasses() {
        System.out.println("getElevatedClasses");
        List<SootClass> expResult = null;
        List<SootClass> result = CallGraphObject.getElevatedClasses();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of elevateClassToApplicationLevel method, of class CallGraphObject.
     */
    @Test
    public void testElevateClassToApplicationLevel() {
        System.out.println("elevateClassToApplicationLevel");
        SootClass sc = null;
        CallGraphObject.elevateClassToApplicationLevel(sc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
