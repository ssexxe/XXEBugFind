/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import soot.SootClass;
import soot.SootMethod;

/**
 *
 * @author Mikosh
 */
public class MethodDefinitionTest {
    
    public MethodDefinitionTest() {
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
     * Test of getClassName method, of class MethodDefinition.
     */
    @Test
    public void testGetClassName() {
        System.out.println("getClassName");
        MethodDefinition instance = new MethodDefinition();
        String expResult = "";
        String result = instance.getClassName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setClassName method, of class MethodDefinition.
     */
    @Test
    public void testSetClassName() {
        System.out.println("setClassName");
        String className = "";
        MethodDefinition instance = new MethodDefinition();
        instance.setClassName(className);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethodName method, of class MethodDefinition.
     */
    @Test
    public void testGetMethodName() {
        System.out.println("getMethodName");
        MethodDefinition instance = new MethodDefinition();
        String expResult = "";
        String result = instance.getMethodName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMethodName method, of class MethodDefinition.
     */
    @Test
    public void testSetMethodName() {
        System.out.println("setMethodName");
        String methodName = "";
        MethodDefinition instance = new MethodDefinition();
        instance.setMethodName(methodName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterList method, of class MethodDefinition.
     */
    @Test
    public void testGetParameterList() {
        System.out.println("getParameterList");
        MethodDefinition instance = new MethodDefinition();
        List<MethodDefinition.MethodParameter> expResult = null;
        List<MethodDefinition.MethodParameter> result = instance.getParameterList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameterList method, of class MethodDefinition.
     */
    @Test
    public void testSetParameterList() {
        System.out.println("setParameterList");
        List<MethodDefinition.MethodParameter> parameterList = null;
        MethodDefinition instance = new MethodDefinition();
        instance.setParameterList(parameterList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReturnType method, of class MethodDefinition.
     */
    @Test
    public void testGetReturnType() {
        System.out.println("getReturnType");
        MethodDefinition instance = new MethodDefinition();
        String expResult = "";
        String result = instance.getReturnType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setReturnType method, of class MethodDefinition.
     */
    @Test
    public void testSetReturnType() {
        System.out.println("setReturnType");
        String returnType = "";
        MethodDefinition instance = new MethodDefinition();
        instance.setReturnType(returnType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethodDefinition method, of class MethodDefinition.
     */
    @Test
    public void testGetMethodDefinition() throws Exception {
        System.out.println("getMethodDefinition");
        String methodSignature = "";
        MethodDefinition expResult = null;
        MethodDefinition result = MethodDefinition.getMethodDefinition(methodSignature);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSootMethod method, of class MethodDefinition.
     */
    @Test
    public void testGetSootMethod() {
        System.out.println("getSootMethod");
        SootClass sc = null;
        MethodDefinition md = null;
        SootMethod expResult = null;
        SootMethod result = MethodDefinition.getSootMethod(sc, md);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOverrideSootMethod method, of class MethodDefinition.
     */
    @Test
    public void testGetOverrideSootMethod() {
        System.out.println("getOverrideSootMethod");
        SootClass sc = null;
        SootMethod baseMethod = null;
        SootMethod expResult = null;
        SootMethod result = MethodDefinition.getOverrideSootMethod(sc, baseMethod);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class MethodDefinition.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        MethodDefinition instance = new MethodDefinition();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
