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
public class ValueStringTest {
    private ValueString valueString;    
    
    public ValueStringTest() {
        valueString = new ValueString("java.lang.String", "s4", "\"a string value\"");
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
     * Test of getType method, of class ValueString.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        ValueString instance = valueString;
        String expResult = "java.lang.String";
        String result = instance.getType();
        assertEquals(expResult, result);        
    }

    /**
     * Test of getName method, of class ValueString.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        ValueString instance = valueString;
        String expResult = "s4";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class ValueString.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        ValueString instance = valueString;
        String expResult = "\"a string value\"";
        String result = instance.getValue();
        assertEquals(expResult, result);
    }
    
}
