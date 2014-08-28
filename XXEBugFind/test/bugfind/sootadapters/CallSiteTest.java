/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;

/**
 *
 * @author Mikosh
 */
public class CallSiteTest {
    
    public CallSiteTest() {
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
     * Test of getLineLocation method, of class CallSite.
     */
    @Test
    public void testGetLineLocation() {
        System.out.println("getLineLocation");
        CallSite instance = null;
        int expResult = 0;
        int result = instance.getLineLocation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSourceMethod method, of class CallSite.
     */
    @Test
    public void testGetSourceMethod() {
        System.out.println("getSourceMethod");
        CallSite instance = null;
        SootMethod expResult = null;
        SootMethod result = instance.getSourceMethod();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEdge method, of class CallSite.
     */
    @Test
    public void testGetEdge() {
        System.out.println("getEdge");
        CallSite instance = null;
        Edge expResult = null;
        Edge result = instance.getEdge();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEdge method, of class CallSite.
     */
    @Test
    public void testSetEdge() {
        System.out.println("setEdge");
        Edge edge = null;
        CallSite instance = null;
        instance.setEdge(edge);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class CallSite.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        CallSite instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class CallSite.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        CallSite o = null;
        CallSite instance = null;
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
