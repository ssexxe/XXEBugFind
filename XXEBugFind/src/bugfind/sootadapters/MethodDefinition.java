/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import soot.BooleanType;
import soot.CharType;
import soot.DoubleType;
import soot.IntType;
import soot.LongType;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;

/**
 * This class encapsulates a Method Definition. It stores info on the class that contains the method, the 
 * method name, the method parameter signature and also the return type
 * @author Mikosh
 */
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class MethodDefinition {
    @XmlElement (name = "ClassName")
    private String className;
    
    @XmlElement (name = "MethodName")
    private String methodName;    
    
    @XmlElement (name = "MethodParameter")
    private List<MethodParameter> parameterList;
    
    @XmlElement (name = "ReturnType")
    private String returnType;

    /**
     * Creates a method definition object
     */
    public MethodDefinition() {}

    /**
     * Creates method definition when given class name, method name, parameter list and return type.
     * @param className
     * @param methodName
     * @param parameterList
     * @param returnType 
     */
    public MethodDefinition(String className, String methodName, List<MethodParameter> parameterList, String returnType) {
        this.className = className;
        this.methodName = methodName;
        this.parameterList = (parameterList == null) ? new ArrayList<MethodParameter>() : parameterList;
        this.returnType = returnType;
    }
    
    /**
     * Get class name.
     * @return class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Set class name with given class name.
     * @param className class name given.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Get the method name.
     * @return method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Set method name with given method name.
     * @param methodName method name given.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Gets method parameter list
     * @return method parameter list 
     */
    public List<MethodParameter> getParameterList() {
        return parameterList;
    }
    
    /**
     * Gets the parameter types as a string list
     * @return the parameter types as a string list 
     */
    public List<String> getParameterTypesOnlyAsString() {
        List<String> list = new ArrayList<>();
        for (MethodParameter mp : getParameterList()) {
            list.add(mp.getType());
        }
        return list;
    }
    
    /**
     * Gets the parameter types as a list of type objects
     * @return the parameter types as a list of type objects 
     */
    public List<Type> getParameterTypesOnly() {
        List<Type> list = new ArrayList<>();
        for (MethodParameter mp : getParameterList()) {
            list.add(convertToType(mp.getType()));
        }
        return list;
    }

    /**
     * Set the parameter list
     * @param parameterList the parameter list to set
     */
    public void setParameterList(List<MethodParameter> parameterList) {
        this.parameterList = parameterList;
    }

    /**
     * Gets the return type
     * @return  the return type
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * Sets the return type of the method definiton
     * @param returnType the return type to be set
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    
    
    
    
    
    /**
     * A method parameter class
     */
    @XmlRootElement
    @XmlAccessorType (XmlAccessType.FIELD)
    public static class MethodParameter {
        
        @XmlElement (name = "ParameterType")
        private String type;
        
        @XmlElement (name = "ParameterName")
        private String name;

        /**
         * Creates a new method parameter object
         */
        public MethodParameter() {
        }

        /**
         * Creates a method parameter object when given the type and its name
         * @param type the type of the method parameter
         * @param name the name of the method parameter
         */
        public MethodParameter(String type, String name) {
            this.type = type;
            this.name = name;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * Gets the method definition from the given method signature. This is used for the purpose of the user using the 
     * -vmd commanline option. The format is <code>ClassName.methodName(ParameterType1, ParameterType2)</code>
     * e.g.<code> org.ucl.MyClass.amethod(java.lang.String, int, org.rmx.Options)</code>
     * @param methodSignature the method sinature
     * @return the method definition from the given method signature.
     * @throws Exception 
     */
    public static MethodDefinition getMethodDefinition(String methodSignature) throws Exception {        
        if (methodSignature == null || methodSignature.trim().isEmpty()) {
            throw new Exception("The method signuature given cannot be null or empty. It should have the format "
                    + "ClassName.methodName(ParameterType1, ParameterType2) "
                    + "e.g. org.ucl.MyClass.amethod(java.lang.String, int, org.rmx.Options)" );            
        }
        if (!methodSignature.contains(".") || !methodSignature.contains("(") || !methodSignature.contains(")")) {
            throw new Exception("Invalid method signature given. It should have the format "
                    + "ClassName.methodName(ParameterType1, ParameterType2) "
                    + "e.g. org.ucl.MyClass.amethod(java.lang.String, int, org.rmx.OptionsList[])" );            
        }
        
        // use the given sematics to parse the method
        int lastDotIndexBeforeParenthesis = methodSignature.substring(0, methodSignature.indexOf("(")).lastIndexOf(".");
        int firstIndexOfParen = methodSignature.indexOf("(");
        String classname = methodSignature.substring(0, lastDotIndexBeforeParenthesis);
        String methodName = methodSignature.substring(lastDotIndexBeforeParenthesis + 1, firstIndexOfParen);
        String argumentStr =  methodSignature.substring(firstIndexOfParen + 1, methodSignature.lastIndexOf(")")).trim();
        
        if (argumentStr.length() == 0) {
            return new MethodDefinition(classname, methodName, null, null);
        }
        // now create the method parameters
        List<MethodParameter> parameterList = new ArrayList<>();
        String[] parameters = argumentStr.split(",");
        for (String parameter : parameters) {
            if (parameter.trim().contains(" ")) { // use a more wider range to cacth traps
                throw new Exception("Invalid method parameter '" + parameter + "' given" );           
            }
            else {
                parameterList.add(new MethodParameter(parameter.trim(), null));
            }
        }
                    
        return new MethodDefinition(classname, methodName, parameterList, null);
    }
    
    /**
     * Gets the soot method with the specified method definition. This is used becaus soot's SootClass.getMethodByName() 
     * does not return a soot method for abstact method or interface methods of class. that is methods that do not have
     * bodies in the the class or interface inwhich it was declared. This method handles such scenario
     * @param sc the sootclass to get the method from
     * @param md the method definition to use
     * @return the soot method with the specified method definition. 
     */
    public static SootMethod getSootMethod(SootClass sc, MethodDefinition md) {
        // Get a list of all the possible methods in this class including inherited ones
        List<SootMethod> list = new SootClassWrapper(sc).getAllMethodsDeclaredIncludingInherited();//sc.getMethods();
        
        // loop through looking for one that matches it
        for (SootMethod meth : list) { 
            if (meth.getName().equals(md.getMethodName())) {
                if (meth.getParameterCount() == md.getParameterList().size()) {       
                    if (meth.getParameterCount() == 0) { // special case of a method with no arguments
                        return meth;
                    }
                    
                    // method has parameters
                    List<Type> parameterTypeList = meth.getParameterTypes();
                    List<MethodParameter> methParameterList = md.getParameterList();
                    boolean allThesame = true;// only break when all the parameter types are equal
                    for (int i=0; i<meth.getParameterCount(); ++i) {
                        if (!parameterTypeList.get(i).toString().equals(methParameterList.get(i).getType())) {
                            allThesame = false;
                            break;//return meth;
                        }
                    }
                    if (allThesame) {
                        return meth;
                    }
                }
            }
        }
        
        // hanldes cases of phantom classes
        if (sc.isPhantomClass()) {
            md.getParameterTypesOnly();
            SootMethod m = new SootMethod(md.getMethodName(), md.getParameterTypesOnly(), convertToType(md.getReturnType()));
            m.setModifiers(Modifier.PUBLIC);//Scene.v().getPointsToAnalysis();
            sc.addMethod(m);
            return m;            
        }
     
        // code must not reach here// reaching here means no method of the md signature exists
        throw new RuntimeException("Couldn't find method signature " + md + " in " + sc);
    }

    /**
     * Gets a method in the specified class that overrides the specified method of the base class. ie if M of class BaseClass
     * return MO of subclass SC where MO is the method of SC tht overrides M of baseclass
     * @param sc the soot class
     * @param baseMethod the base method
     * @return a method in the specified class that overrides the specified method of the base class. return null if
     * no override method exists
     */
    public static SootMethod getOverrideSootMethod(SootClass sc, SootMethod baseMethod) {
        List<SootMethod> list = new SootClassWrapper(sc).getAllMethodsDeclaredIncludingInherited();//sc.getMethods();
        for (SootMethod meth : list) { 
            if (meth.getName().equals(baseMethod.getName())) {
                if (meth.getParameterCount() == baseMethod.getParameterCount()) {       
                    if (meth.getParameterCount() == 0) { // special case of a method with no arguments
                        return meth;
                    }
                    
                    // method has parameters
                    List<Type> parameterTypeList = meth.getParameterTypes();
                    List<Type> parameterTypeListParent = baseMethod.getParameterTypes();
                    //List<MethodParameter> methParameterList = md.getParameterList();
                    boolean allThesame = true;
                    for (int i=0; i<meth.getParameterCount(); ++i) {
                        if (!parameterTypeList.get(i).toString().equals(parameterTypeListParent.get(i).toString())) {
                            allThesame = false;
                            break;//return meth;
                        }
                    }
                    if (allThesame) {
                        return meth;
                    }
                }
            }
        }
        
        if (sc.isAbstract() || sc.isInterface()) {// it is permitted for abstract or iface not to have the method
            return null;
        }
        
        //SootClass sc1 = Scene.v().loadClassAndSupport("org.apache.xerces.parsers.SAXParser");//Scene.v().getSootClass("org.​apache.​xerces.​parsers.SAXParser"); 
        //sc1.setApplicationClass();
        
     //  sc.getMethodByName("parse")
        // code must not reach here// reaching here means no method of the md signature exists
        throw new RuntimeException("Couldn't find method that override " + baseMethod + " in " + sc);
    }
    
    /**
     * Convert the type name to a soot Type
     * @param type the string corresponding to the Soot type to be returned
     * @return the type name to a soot Type 
     */
    protected static Type convertToType(String type) {
        if (type.equals("void")) {
            return VoidType.v();
        }
        else if (type.equals("int")) {
            return IntType.v();
        }
        else if (type.equals("short")) {
            return ShortType.v();
        }
        else if (type.equals("long")) {
            return LongType.v();
        }
        else if (type.equals("boolean")) {
            return BooleanType.v();
        }
        else if (type.equals("char")) {
            return CharType.v();
        }
        else if (type.equals("float")) {
            return BooleanType.v();
        }
        else if (type.equals("double")) {
            return DoubleType.v();
        }
        else {
            return RefType.v(type);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb  = new StringBuilder(getClassName());
        sb.append(".").append(getMethodName()).append("(");
        for (MethodParameter mp: getParameterList()) {
            sb.append(mp.getType()).append(", ");
        }
        if (sb.toString().endsWith(", ")) {
            sb.replace(sb.lastIndexOf(", "), sb.length(), "");
        }
        sb.append(");");
        
        return sb.toString();
    }
    
    
}
