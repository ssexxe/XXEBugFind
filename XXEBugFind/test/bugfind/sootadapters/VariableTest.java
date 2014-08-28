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

/**
 *
 * @author Mikosh
 */
public class VariableTest {
    
    public VariableTest() {
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
     * Test of getName method, of class Variable.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Variable instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class Variable.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Variable instance = null;
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLevel method, of class Variable.
     */
    @Test
    public void testGetLevel() {
        System.out.println("getLevel");
        Variable instance = null;
        int expResult = 0;
        int result = instance.getLevel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isStatic method, of class Variable.
     */
    @Test
    public void testIsStatic() {
        System.out.println("isStatic");
        Variable instance = null;
        boolean expResult = false;
        boolean result = instance.isStatic();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isLocal method, of class Variable.
     */
    @Test
    public void testIsLocal() {
        System.out.println("isLocal");
        Variable instance = null;
        boolean expResult = false;
        boolean result = instance.isLocal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isField method, of class Variable.
     */
    @Test
    public void testIsField() {
        System.out.println("isField");
        Variable instance = null;
        boolean expResult = false;
        boolean result = instance.isField();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Variable.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Variable instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
