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
    Variable variable;
    
    public VariableTest() {
        variable = new Variable("var1", "java.lang.String", Variable.STATIC_VARIABLE);
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
        Variable instance = variable;
        String expResult = "var1";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class Variable.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Variable instance = variable;
        String expResult = "java.lang.String";
        String result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLevel method, of class Variable.
     */
    @Test
    public void testGetLevel() {
        System.out.println("getLevel");
        Variable instance = variable;
        int expResult = Variable.STATIC_VARIABLE;
        int result = instance.getLevel();
        assertEquals(expResult, result);
    }

    /**
     * Test of isStatic method, of class Variable.
     */
    @Test
    public void testIsStatic() {
        System.out.println("isStatic");
        Variable instance = variable;
        boolean expResult = true;
        boolean result = instance.isStatic();
        assertEquals(expResult, result);
    }

    /**
     * Test of isLocal method, of class Variable.
     */
    @Test
    public void testIsLocal() {
        System.out.println("isLocal");
        Variable instance = variable;
        boolean expResult = false;
        boolean result = instance.isLocal();
        assertEquals(expResult, result);
    }

    /**
     * Test of isField method, of class Variable.
     */
    @Test
    public void testIsField() {
        System.out.println("isField");
        Variable instance = variable;
        boolean expResult = false;
        boolean result = instance.isField();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Variable.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Variable instance = variable;
        String expResult = "var-name: var1 var-type: java.lang.String var-level: STATIC_VAR";
        String result = instance.toString();System.out.println(result);
        assertEquals(expResult, result);
    }
    
}
