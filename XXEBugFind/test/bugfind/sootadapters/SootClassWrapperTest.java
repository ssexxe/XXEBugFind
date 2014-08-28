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
import soot.SootClass;
import soot.SootMethod;

/**
 *
 * @author Mikosh
 */
public class SootClassWrapperTest {
    
    public SootClassWrapperTest() {
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
     * Test of getAllMethodsDeclaredInThisClassOnly method, of class SootClassWrapper.
     */
    @Test
    public void testGetAllMethodsDeclaredInThisClassOnly() {
        System.out.println("getAllMethodsDeclaredInThisClassOnly");
        SootClassWrapper instance = null;
        List<SootMethod> expResult = null;
        List<SootMethod> result = instance.getAllMethodsDeclaredInThisClassOnly();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMethodsDeclaredIncludingInherited method, of class SootClassWrapper.
     */
    @Test
    public void testGetAllMethodsDeclaredIncludingInherited() {
        System.out.println("getAllMethodsDeclaredIncludingInherited");
        SootClassWrapper instance = null;
        List<SootMethod> expResult = null;
        List<SootMethod> result = instance.getAllMethodsDeclaredIncludingInherited();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addMethodsFromSuperClassToThisClassMethods method, of class SootClassWrapper.
     */
    @Test
    public void testAddMethodsFromSuperClassToThisClassMethods() {
        System.out.println("addMethodsFromSuperClassToThisClassMethods");
        SootClass superClass = null;
        List<SootMethod> methods = null;
        SootClassWrapper instance = null;
        instance.addMethodsFromSuperClassToThisClassMethods(superClass, methods);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSootClass method, of class SootClassWrapper.
     */
    @Test
    public void testGetSootClass() {
        System.out.println("getSootClass");
        SootClassWrapper instance = null;
        SootClass expResult = null;
        SootClass result = instance.getSootClass();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSootClass method, of class SootClassWrapper.
     */
    @Test
    public void testSetSootClass() {
        System.out.println("setSootClass");
        SootClass sootClass = null;
        SootClassWrapper instance = null;
        instance.setSootClass(sootClass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of needsApplicationElevation method, of class SootClassWrapper.
     */
    @Test
    public void testNeedsApplicationElevation() {
        System.out.println("needsApplicationElevation");
        SootClass sc = null;
        boolean expResult = false;
        boolean result = SootClassWrapper.needsApplicationElevation(sc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
