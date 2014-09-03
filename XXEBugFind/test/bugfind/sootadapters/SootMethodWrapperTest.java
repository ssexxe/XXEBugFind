/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import soot.IntType;
import soot.SootMethod;
import soot.VoidType;

/**
 *
 * @author Mikosh
 */
public class SootMethodWrapperTest {
    private SootMethodWrapper sootmethodwrapper;
    private SootMethod sootMethod;
    
    public SootMethodWrapperTest() {
        List list = new ArrayList();
        list.add(IntType.v());
        sootMethod = new SootMethod("meth", list, VoidType.v());
        sootmethodwrapper = new SootMethodWrapper(sootMethod);
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
     * Test of getSootMethod method, of class SootMethodWrapper.
     */
    @Test
    public void testGetSootMethod() {
        System.out.println("getSootMethod");
        SootMethodWrapper instance = sootmethodwrapper;
        SootMethod expResult = sootMethod;
        SootMethod result = instance.getSootMethod();
        boolean b = expResult == result;
        System.out.print(b);
        assertTrue(b);
    }

    /**
     * Test of setSootMethod method, of class SootMethodWrapper.
     */
    @Test
    public void testSetSootMethod() {
        System.out.println("setSootMethod");
        
        SootMethodWrapper instance = sootmethodwrapper;
        instance.setSootMethod(this.sootMethod);
        assertSame(sootMethod, instance.getSootMethod());
    }
    
}
