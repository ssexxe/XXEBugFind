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

/**
 *
 * @author Mikosh
 */
public class SootMethodWrapperTest {
    
    public SootMethodWrapperTest() {
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
     * Test of getSootMethod method, of class SootMethodWrapper.
     */
    @Test
    public void testGetSootMethod() {
        System.out.println("getSootMethod");
        SootMethodWrapper instance = null;
        SootMethod expResult = null;
        SootMethod result = instance.getSootMethod();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSootMethod method, of class SootMethodWrapper.
     */
    @Test
    public void testSetSootMethod() {
        System.out.println("setSootMethod");
        SootMethod sootMethod = null;
        SootMethodWrapper instance = null;
        instance.setSootMethod(sootMethod);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
