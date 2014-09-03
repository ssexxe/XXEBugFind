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
import polyglot.ext.param.Topics;
import soot.SootClass;
import soot.SootMethod;

/**
 *
 * @author Mikosh
 */
public class MethodDefinitionTest {
    private final MethodDefinition methodDefinition;
    private final List<MethodDefinition.MethodParameter> listParam;
    public MethodDefinitionTest() {
        listParam = new ArrayList<>();
        listParam.add(new MethodDefinition.MethodParameter("java.lang.String", "param1"));
        methodDefinition = new MethodDefinition("com.sun.Class", "method1", listParam, "void");
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
        MethodDefinition instance = methodDefinition;
        String expResult = "com.sun.Class";
        String result = instance.getClassName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setClassName method, of class MethodDefinition.
     */
    @Test
    public void testSetClassName() {
        System.out.println("setClassName");
        String className = "com.sun.Class1";
        MethodDefinition instance = new MethodDefinition();
        instance.setClassName(className);
        assertEquals(className, instance.getClassName());
    }

    /**
     * Test of getMethodName method, of class MethodDefinition.
     */
    @Test
    public void testGetMethodName() {
        System.out.println("getMethodName");
        MethodDefinition instance = methodDefinition;
        String expResult = "method1";
        String result = instance.getMethodName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMethodName method, of class MethodDefinition.
     */
    @Test
    public void testSetMethodName() {
        System.out.println("setMethodName");
        String methodName = "method1";
        MethodDefinition instance = new MethodDefinition();
        instance.setClassName(methodName);
        assertEquals(methodName, instance.getClassName());
    }

    /**
     * Test of getParameterList method, of class MethodDefinition.
     */
    @Test
    public void testGetParameterList() {
        System.out.println("getParameterList");
        MethodDefinition instance = methodDefinition;
        List<MethodDefinition.MethodParameter> expResult = listParam;
        List<MethodDefinition.MethodParameter> result = instance.getParameterList();
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameterList method, of class MethodDefinition.
     */
    @Test
    public void testSetParameterList() {
        System.out.println("setParameterList");
        List<MethodDefinition.MethodParameter> parameterList = listParam;
        MethodDefinition instance = new MethodDefinition();
        instance.setParameterList(parameterList);
        assertEquals(listParam, instance.getParameterList());
    }

    /**
     * Test of getReturnType method, of class MethodDefinition.
     */
    @Test
    public void testGetReturnType() {
        System.out.println("getReturnType");
        MethodDefinition instance = methodDefinition;
        String expResult = "void";
        String result = instance.getReturnType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setReturnType method, of class MethodDefinition.
     */
    @Test
    public void testSetReturnType() {
        System.out.println("setReturnType");
        String returnType = "int";
        MethodDefinition instance = new MethodDefinition();
        instance.setReturnType(returnType);
        assertEquals("int", instance.getReturnType());
        
    }

    /**
     * Test of getMethodDefinition method, of class MethodDefinition.
     */
    @Test
    public void testGetMethodDefinition() throws Exception {
        System.out.println("getMethodDefinition");
        String methodSignature = "com.sun.Class1.parse()";
        MethodDefinition expResult = new MethodDefinition("com.sun.Class1", "parse", null, "null");
        MethodDefinition result = MethodDefinition.getMethodDefinition(methodSignature);
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of toString method, of class MethodDefinition.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        MethodDefinition instance = methodDefinition;
        String expResult = "com.sun.Class.method1(java.lang.String);";
        String result = instance.toString();System.out.println(result);
        assertEquals(expResult, result);
    }
    
}
