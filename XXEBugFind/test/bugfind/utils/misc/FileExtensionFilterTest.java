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
        FileExtensionFilter instance = new FileExtensionFilter(".png");
        String expResult = ".png";
        String result = instance.getExtension();
        assertEquals(expResult, result);
    }

    /**
     * Test of setExtension method, of class FileExtensionFilter.
     */
    @Test
    public void testSetExtension() {
        System.out.println("setExtension");
        String extension = ".bmp";
        FileExtensionFilter instance = new FileExtensionFilter(".jpg");
        instance.setExtension(extension);
        assertEquals(extension, instance.getExtension());
    }

    /**
     * Test of accept method, of class FileExtensionFilter.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        File dir = new File("dir");
        String name = "mypic.png";
        FileExtensionFilter instance = new FileExtensionFilter(".png");
        
        boolean expResult = true;
        boolean result = instance.accept(dir, name);
        assertEquals(expResult, result);
    }
    
}
