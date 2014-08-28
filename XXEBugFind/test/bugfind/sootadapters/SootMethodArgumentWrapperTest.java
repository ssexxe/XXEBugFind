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
public class SootMethodArgumentWrapperTest {
    
    public SootMethodArgumentWrapperTest() {
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
     * Test of getArgumentValue method, of class SootMethodArgumentWrapper.
     */
    @Test
    public void testGetArgumentValue() {
        System.out.println("getArgumentValue");
        SootMethodArgumentWrapper instance = null;
        Object expResult = null;
        Object result = instance.getArgumentValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setArgumentValue method, of class SootMethodArgumentWrapper.
     */
    @Test
    public void testSetArgumentValue() {
        System.out.println("setArgumentValue");
        Object argumentValue = null;
        SootMethodArgumentWrapper instance = null;
        instance.setArgumentValue(argumentValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
