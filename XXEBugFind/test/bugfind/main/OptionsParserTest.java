/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.main;

import java.util.Map;
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
public class OptionsParserTest {
    
    public OptionsParserTest() {
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
     * Test of parse method, of class OptionsParser.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String[] args = new String[]{
            "-d", "C:/app/javaApp1", 
            "-l", "C:/app/lib/javaLib1",
            "-f", "xml",    
            "-o", "C:/app/output.xml",
            "-rs", "C:/myruleset.xml",
        };
        
        Map<String, String> result = OptionsParser.parse(args);
        assertEquals(result.get(OptionsParser.DIR_OPT), "C:/app/javaApp1");
        assertEquals(result.get(OptionsParser.LIB_OPT), "C:/app/lib/javaLib1");
        assertEquals(result.get(OptionsParser.OUTPUT_FORMAT_OPT), "xml");
        assertEquals(result.get(OptionsParser.OUTPUT_FILE_OPT), "C:/app/output.xml");
        assertEquals(result.get(OptionsParser.RULESET_OPT), "C:/myruleset.xml");
        assertEquals(result.get(OptionsParser.RT_LIB_LOC_OPT), null);
    }
    
}
