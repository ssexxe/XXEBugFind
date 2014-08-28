/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

import java.io.File;
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
public class FileExtensionFilterTest {
    
    public FileExtensionFilterTest() {
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
     * Test of getExtension method, of class FileExtensionFilter.
     */
    @Test
    public void testGetExtension() {
        System.out.println("getExtension");
        FileExtensionFilter instance = null;
        String expResult = "";
        String result = instance.getExtension();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setExtension method, of class FileExtensionFilter.
     */
    @Test
    public void testSetExtension() {
        System.out.println("setExtension");
        String extension = "";
        FileExtensionFilter instance = null;
        instance.setExtension(extension);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of accept method, of class FileExtensionFilter.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        File dir = null;
        String name = "";
        FileExtensionFilter instance = null;
        boolean expResult = false;
        boolean result = instance.accept(dir, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
