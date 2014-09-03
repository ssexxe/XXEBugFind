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
public class SootRunnerTest {
    
    public SootRunnerTest() {
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
     * Test of getInstance method, of class SootRunner.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        SootRunner result = SootRunner.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of main method, of class SootRunner.
     */
//    @Test
//    public void testMain() {
//        System.out.println("main");
//        String[] args = null;
//        String libPaths = "";
//        SootRunner.main(args, libPaths);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of run method, of class SootRunner.
//     */
//    @Test
//    public void testRun() {
//        System.out.println("run");
//        String[] args = null;
//        String libPaths = "";
//        SootRunner instance = null;
//        instance.run(args, libPaths);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    
    
}
