/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

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
public class UtilsTest {
    
    public UtilsTest() {
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
     * Test of join method, of class Utils.
     */
    @Test
    public void testJoin() {
        System.out.println("join");
        String[] strArray = new String[]{"str1", "str2", "str3"};
        String joinTerm = ";";
        String expResult = "str1;str2;str3";
        String result = Utils.join(strArray, joinTerm);
        assertEquals(expResult, result);
        assertEquals("str1;;;str3", Utils.join(new String[]{"str1", ";", "str3"}, joinTerm));
    }
    
}
