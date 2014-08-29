/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import bugfind.main.OptionsParser;
import bugfind.utils.misc.BugFindConstants;
import bugfind.utils.misc.FileUtil;
import bugfind.utils.misc.Utils;
import bugfind.utils.misc.XMLUtils;
import bugfind.xxe.ActualVulnerabilityItem;
import bugfind.xxe.xmlobjects.ActualVulnerabilityItems;
import bugfind.xxe.VulnerabilityDefinitionItems;
import bugfind.xxe.VulnerableXMLMethodDefinitions;
import bugfind.xxe.XXEVulnerabilityDetector;
import bugfind.xxe.xmlobjects.ActualVulnerabilityItemForXML;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import soot.Body;
import soot.G;
import soot.PackManager;
import soot.PatchingChain;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.grimp.internal.GNewInvokeExpr;
import soot.grimp.internal.GRValueBox;
import soot.grimp.internal.GStaticInvokeExpr;
import soot.grimp.internal.GVirtualInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.StmtBox;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;

/**
 *
 * @author Mikosh
 */
public class CallGraphObject {
    protected static CallGraphObject callGraphObject;
    
    protected Map<String, String> bugFindParametersMap;
    protected List<MethodDefinition> vulMethodDefinitionList;
    private static List<SootClass> elevatedClasses = new ArrayList<>();

    public static CallGraphObject getInstance() {
        if (callGraphObject == null) {
            callGraphObject = new CallGraphObject();
        }
        
        return callGraphObject;
    }
    
    public static CallGraphObject getInstance(Map<String, String> bugFindParametersMap) {
        CallGraphObject cgo = getInstance();        
        cgo.setBugFindParametersMap(bugFindParametersMap);
        
        return cgo;
    }
    
    
    protected CallGraphObject() {}

    
    protected CallGraphObject(Map<String, String> bugFindParametersMap) {
        this.bugFindParametersMap = bugFindParametersMap;
    }   
    
    
    
    public void runAnalysis() throws FileNotFoundException, FileSystemException, Exception {
        FileUtil fileUtil = FileUtil.getFileUtil();        
        StringBuilder sb = new StringBuilder();
        String libLocation = bugFindParametersMap.get(OptionsParser.LIB_OPT);
        String dirLocation = bugFindParametersMap.get(OptionsParser.DIR_OPT);        
        String outputFormat = bugFindParametersMap.get(OptionsParser.OUTPUT_FORMAT_OPT);
        String outputFile = bugFindParametersMap.get(OptionsParser.OUTPUT_FILE_OPT);
        String[] dirLocs = null, libLocs = null;
        String pathSep = File.pathSeparator;
        
        // first parse target application location
        if (dirLocation == null) {
            throw new Exception("There is no specified value for " + OptionsParser.DIR_OPT + " option");
        }
        
        if (dirLocation.contains(";")) { // means it contains more than one main jar file/dirs
            dirLocs = dirLocation.split(";");
            for (String dirLoc : dirLocs) {
                if (!dirLoc.trim().isEmpty()) {
                    sb.append(fileUtil.getFullPathName(dirLoc.trim())).append(pathSep);
                }
            }
            dirLocation = (sb.lastIndexOf(pathSep) == sb.length() -1) ? sb.substring(0, sb.length() - 1) : sb.toString();
        }
        else {
            dirLocs = new String[]{fileUtil.getFullPathName(dirLocation)};
        }
        
        // check if the application depends on supporting libs and process process 
        sb.setLength(0);
        if (libLocation == null && bugFindParametersMap.containsKey(OptionsParser.LIB_OPT)) {
            throw new Exception("There is no specified value for " + OptionsParser.LIB_OPT + " option");
        }
        else {        
            if (libLocation == null || libLocation.trim().isEmpty()) {
                libLocation = "";
            }
            else if (libLocation.contains(";")) { // means it contains more than one main jar file/dirs
                libLocs = libLocation.split(";");
                for (String libLoc : libLocs) {
                    if (!libLoc.trim().isEmpty()) {
                        sb.append(fileUtil.getFullPathName(libLoc.trim())).append(pathSep);
                        //System.out.println("splitting libs: " + fileUtil.getFullPathName(libLoc.trim()));
                    }
                }
                libLocation = (sb.lastIndexOf(pathSep) == sb.length() -1) ? sb.substring(0, sb.length() - 1) : sb.toString();
            } else {
                libLocs = new String[]{libLocation};
            }
        }
        
        // do checkings for output type and output file
        if (outputFormat == null && bugFindParametersMap.containsKey(OptionsParser.OUTPUT_FORMAT_OPT)) {
            throw new Exception("There is no specified value for " + OptionsParser.OUTPUT_FORMAT_OPT + " option");
        }
        
        if (outputFormat != null) {
            if (!outputFormat.trim().toLowerCase().equals(BugFindConstants.TEXT_FORMAT) && 
                    !outputFormat.trim().toLowerCase().equals(BugFindConstants.XML_FORMAT)) {
                throw new Exception("Invalid value specified for " + OptionsParser.OUTPUT_FORMAT_OPT + " option. "
                        + "Acceptable values are either TEXT or XML ");
            }
            outputFormat = outputFormat.trim().toLowerCase();
            // ensure the exact value is set in bugfind
            bugFindParametersMap.put(OptionsParser.OUTPUT_FORMAT_OPT, outputFormat);
        }
        
        if (outputFile == null && bugFindParametersMap.containsKey(OptionsParser.OUTPUT_FILE_OPT)) {
            throw new Exception("There is no specified value for " + OptionsParser.OUTPUT_FILE_OPT + " option");
        }
        
        // get rt directory location
        
        String rtDirectory = null;
        String userSpecifiedRTLocation = bugFindParametersMap.get(OptionsParser.RT_LIB_LOC_OPT);
        
        if (userSpecifiedRTLocation != null) {
            File f = new File(userSpecifiedRTLocation);
            if (!f.exists()) {
                throw new Exception("The user specified directory containing java's rt.jar lib was not found. The path '"
                        + userSpecifiedRTLocation + "' does not exist");
            }
            rtDirectory = fileUtil.getFullPathName(userSpecifiedRTLocation.trim());
        }
        else {
            String guessedRTdir = fileUtil.getRTDirectory();
            if (guessedRTdir == null) {
                throw new Exception("Cannot find the directory containing java's rt.jar lib. This is needed"
                        + "for call graph generation. Use the " + 
                        OptionsParser.RT_LIB_LOC_OPT + " option to specify the directory containing rt.jar");
            }
            
            rtDirectory = guessedRTdir;
        }
      
        String javaNecessaryJarsLoc = Utils.join(fileUtil.getAllFilesWithExtension(rtDirectory, ".jar"), pathSep);
        String cpOptionString = (!libLocation.trim().isEmpty()) ? dirLocation + pathSep + libLocation + pathSep + javaNecessaryJarsLoc
                :dirLocation + pathSep + javaNecessaryJarsLoc;
        
        List<String> argsList = new ArrayList<String>();
        
        // add all the necessary aguments to start the call graph generation
        argsList.addAll(Arrays.asList(new String[]{
			   "-w",  
                           //"-allow-phantom-refs",
                           //"-via-grimp",
                           //"-f",
                           //"dava",
                           "-p",
                           "cg",
                           "all-reachable:true",
                           "-p",
                           "jb",
                           "use-original-names:false", //"-f", "dava",not in graph!
                           "-cp",
                           cpOptionString}));
        for (String s: dirLocs) {
            if (!s.trim().isEmpty()) {
                argsList.add("-process-dir");
                argsList.add(s.trim());
                System.out.println("-pdir: " + s.trim());
            }
        }
        System.out.println("-cp opt string:\n" + cpOptionString);
        // get get vulnerable method definition list
        String vulmethodDefs = bugFindParametersMap.get(OptionsParser.VMD_OPT);
        //if (vulmethodDefs == null || vulmethodDefs.trim().isEmpty()) {
        //    throw new Exception("There is no specified value for " + OptionsParser.VMD_OPT + " option");
        //}
        this.vulMethodDefinitionList = getVulnerableMethodDefinitions(vulmethodDefs);
        // check if method def list is empty
        final CallGraphObject cgo = this;
        
        // add our custom scene transformer to deal with the call graph finds
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.BugFindTrans", new SceneTransformer() {
	//PackManager.v().getPack("gb").add(new Transform("gb.BugFindTrans", new SceneTransformer() {

		@Override
		protected void internalTransform(String phaseName, Map options) {
		       //CHATransformer.v().transform();
		       CallGraph cg = Scene.v().getCallGraph();//src.getActiveBody().
                       XXEVulnerabilityDetector xvd = new XXEVulnerabilityDetector(cgo);
                       List<ActualVulnerabilityItem> actualVulnerabilities = xvd.findVulnerabilities();
                       String outputFormat = (bugFindParametersMap.get(OptionsParser.OUTPUT_FORMAT_OPT) == null) ?
                               BugFindConstants.TEXT_FORMAT: bugFindParametersMap.get(OptionsParser.OUTPUT_FORMAT_OPT); 
                    try {
                        String outputFile = bugFindParametersMap.get(OptionsParser.OUTPUT_FILE_OPT);
                        printActualVunerabilitesFound(cg, actualVulnerabilities, outputFormat, (outputFile != null));
                    } catch (JAXBException ex) {
                        Logger.getLogger(CallGraphObject.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CallGraphObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                       
//                       if (actualVulnerabilities.size() > 0) {
//                           System.out.println("\n" + actualVulnerabilities.size() + " variant(s) of xxe vulnerabilities found");
//                       }                                        
//                       int n = 0;
//                       for (ActualVulnerabilityItem avi : actualVulnerabilities) {
//                           ++n;
//                           String classShortName = avi.getVulnerabilityDefinitionItem().getMethodDefinition().getClassName();
//                           
//                           if (classShortName.contains(".")) {
//                               int index = classShortName.lastIndexOf(".");
//                               classShortName = classShortName.substring(index+1);
//                           }
//                           System.out.println("XXE Var-" + n + " due to using " + classShortName
//                                   + " API. See detail: \n" + avi);
//                           System.out.println("Reason: " + avi.getReason());
//                           
//                           System.out.println("Exploitation route");
//                           printCallTraces(avi, cg);
//                           System.out.println("");
//                       }
                       //List l = CallGraphObject.getElevatedClasses();
//                       
                       for (MethodDefinition md : vulMethodDefinitionList) {
                           //Scene.v().loadClassAndSupport("org.apache.xerces.jaxp.DocumentBuilderImpl")//getLibraryClasses().toString()
                           //Scene.v().getSootClass("org.apache.xerces.jaxp.DocumentBuilderImpl")
                           //new FastHierarchy().getSubclassesOf(Scene.v().getSootClass(md.getClassName()));
                                   
                           //List l = Scene.v().getActiveHierarchy().getSubclassesOf(Scene.v().getSootClass(md.getClassName()));
                           if (false) displayExecutionTraceForMethod(cg, md, System.out);
                       }
		}
                
		   
	   }));

           String[] args = argsList.toArray(new String[0]);
           
           Options.v().set_keep_line_number(true);
           Options.v().set_include_all(true);
           Options.v().set_allow_phantom_refs(true);
           PhaseOptions.v().setPhaseOption("tag.ln", "on");
           //PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
           //PhaseOptions.v().setPhaseOption("cg.paddle", "enabled:true");
           G.v().out = new PrintStream(new File("soot.txt"));
           System.out.println("started...");
           
           SootRunner.main(args, libLocation);//        soot.Main.main(args);
	}
        
        
        
    public static List<SootMethod> getCallingAncestors(CallGraph cg, SootMethod src) {
        List<SootMethod> ancestors = new ArrayList<SootMethod>();
        List<SootMethod> workList = new ArrayList<SootMethod>();
        ancestors.add(src); // this is the corner case, a node is the ancestor of itself.
        workList.add(src);

        while (!workList.isEmpty()) {
            SootMethod tmpSrc = workList.remove(0);
            Iterator<Edge> eIte = cg.edgesInto(tmpSrc);

            while (eIte.hasNext()) {
                Edge e = eIte.next();
                // TODO: for now we only consider applications classes (to scale up at the cost of soundness)
                SootMethod srcMethod = e.src();
                if (srcMethod.getDeclaringClass().isApplicationClass()) {
                    boolean isAddedIn = ancestors.add(srcMethod);
                                        // if the current src method is already contained in the ancestor set,
                    // we do not add it into the work list.
                    if (isAddedIn) {
                        workList.add(srcMethod);
                    }
                }
            }

        }

        return ancestors;
    }
    
    public List<CallSite> getCallSites(CallGraph cg, SootMethod method) {
        List<CallSite> listCS = new ArrayList<>();
        
        Iterator<Edge> ite = cg.edgesInto(method);
        while (ite.hasNext()) {
            Edge edge = ite.next();
            if (edge.src().getDeclaringClass().isApplicationClass()) {
                listCS.add(new CallSite(edge));
            }
        }
        
        if (listCS.isEmpty() && method.getDeclaringClass().isAbstract() || method.getDeclaringClass().isInterface()) {
            if (method.getDeclaringClass().isInterface()) {
                List<SootClass> subclasses = Scene.v().getActiveHierarchy().getImplementersOf(method.getDeclaringClass());
                
                for (SootClass subclass : subclasses) {
                    SootMethod sm = MethodDefinition.getOverrideSootMethod(subclass, method);
                    if (sm != null) {
                        Iterator<Edge> iteT = cg.edgesInto(sm);
                        while (iteT.hasNext()) {
                            Edge edge = iteT.next();
                            if (edge.src().getDeclaringClass().isApplicationClass()) {
                                listCS.add(new CallSite(edge));
                            }
                        }
                    }
                }
            }
            else if (method.getDeclaringClass().isAbstract()) {
                List<SootClass> subclasses = Scene.v().getActiveHierarchy().getSubclassesOf(method.getDeclaringClass());
                
                for (SootClass subclass : subclasses) {
                    SootMethod sm = MethodDefinition.getOverrideSootMethod(subclass, method);
                    if (sm != null) {
                        Iterator<Edge> iteT = cg.edgesInto(sm);
                        while (iteT.hasNext()) {
                            listCS.add(new CallSite(iteT.next()));
                        }
                    }
                }
                
            }
        }
        
        return listCS;
    }
    
    public List<CallSite> getCallSitesInMethod(CallGraph cg, SootMethod callee, SootMethod caller) {
        List<CallSite> listCS = getCallSites(cg, callee);
        Iterator<CallSite> ite = listCS.iterator();
        
        while (ite.hasNext()) {
            if (!ite.next().getEdge().src().getSignature().equals(caller.getSignature())) {
                ite.remove();
            }
        }
        
//        List<CallSite> listCS = new ArrayList<>();
//        
//        Iterator<Edge> ite = cg.edgesInto(callee);
//        while (ite.hasNext()) {
//            if (ite.next().src().getSignature().equals(caller.getSignature())) {
//                listCS.add(new CallSite(ite.next()));
//            }
//        }
        
        return listCS;
    }
    
    public List<CallSite> getMethodCallEdgesOutOfMethod(CallGraph cg, SootMethod method) {
        List<CallSite> listCS = new ArrayList<>();
        
        Iterator<Edge> ite = cg.edgesOutOf(method);
        while (ite.hasNext()) {
            listCS.add(new CallSite(ite.next()));
        }
        
        return listCS;
    }
    
    protected List<List<SootMethod>> getCallStackTraces(CallGraph cg, SootMethod meth, List<SootMethod> atraceList, List<List<SootMethod>> mainList) {
        if (mainList.size() > 3000) return mainList;
        
        List<SootMethod> currCallersList = new ArrayList<>();
        if (!mainList.contains(atraceList)) {
            mainList.add(atraceList);
        }
        
        Iterator<Edge> ite = cg.edgesInto(meth);
        
        // first check if it has a caller
        if (ite.hasNext() && (!atraceList.isEmpty() && !atraceList.get(atraceList.size() - 1).equals(meth)) 
                && (meth.getDeclaringClass().isApplicationClass()) || atraceList.isEmpty()) {
            atraceList.add(meth);
        }
        
        //SootClass sc = meth.getDeclaringClass();Scene.v().getActiveHierarchy().
        //        getDirectSubclassesOf(Scene.v().getActiveHierarchy().getDirectImplementersOf(sc).get(2))

        while (ite.hasNext()) {
            Edge currEdge = ite.next();// the tostring of this gives me what i want
            if (currEdge.src().getDeclaringClass().isApplicationClass() && !atraceList.contains(currEdge.src())) {
                currCallersList.add(currEdge.src());       //((JAssignStmt)currEdge.srcUnit()).leftBox.getValue().getType()
            }
        }
        if (currCallersList.size() > 1) {
            List<SootMethod>[] l = new List[currCallersList.size()];
            for (int i=1; i<currCallersList.size(); ++i) {
                l[i] = duplicateList(atraceList);
                l[i].add(currCallersList.get(i));
                mainList.add(l[i]);
            }
            l[0] = atraceList;
            atraceList.add(currCallersList.get(0));
        
            for (int i=0; i<currCallersList.size(); ++i) { 
                if (!currCallersList.get(i).equals(meth)) {// the if statement is to stop recursion to itself
                    getCallStackTraces(cg, currCallersList.get(i), l[i], mainList);
                }
            }
        }
        else if (currCallersList.size() == 1) {
            atraceList.add(currCallersList.get(0));
            if (!currCallersList.get(0).equals(meth)) {// the if statement is to stop recursion to itself
                getCallStackTraces(cg, currCallersList.get(0), atraceList, mainList);
            }
        }
        
        return mainList;
    }
    
    protected <T> List<T> duplicateList(List<T> list) {
        List<T> l = new ArrayList<>();
        for(T t : list) {
            l.add(t);
        }
        
        return l;
    }
   
    protected List<MethodDefinition> getVulnerableMethodDefinitions(String methodDefListAsString) throws Exception {
        List<MethodDefinition> list = new ArrayList<>();
        if (methodDefListAsString == null) {
            return list;
        }
        
        String[] methods;
        String stringSep = ";";
        
        if (methodDefListAsString.contains(stringSep)) {
            methods = methodDefListAsString.split(stringSep);            
            
            for (String method : methods) {
                if (!method.trim().isEmpty()) {
                    list.add(MethodDefinition.getMethodDefinition(method.trim()));
                }
            }
        }
        else {
            list.add(MethodDefinition.getMethodDefinition(methodDefListAsString.trim()));
        }
        
        return list;
    }

    protected void displayExecutionTraceForMethod(CallGraph cg, MethodDefinition md, PrintStream ps) {
        List<MethodDefinition.MethodParameter> lparams = md.getParameterList();
        List<String> paramList2 = new ArrayList<>();
        
        for (MethodDefinition.MethodParameter mp : lparams) {
            paramList2.add(mp.getType());
        }
       
        //System.out.println(Options.v().classes().getFirst());
        //System.out.println("dynamic_class");
        //System.out.println(Options.v().dynamic_class().get(0));
        //Scene.v().addBasicClass("org.apache.xerces.parsers.SAXParser");//, SootClass.SIGNATURES);
        //System.out.println("scp: "+Options.v().soot_classpath());
        //Scene.v().tryLoadClass(md.getClassName(), SootClass.BODIES);
        SootClass msc = //Scene.v().tryLoadClass("org.apache.xerces.parsers.SAXParser", SootClass.SIGNATURES);//loadClassAndSupport("org.apache.xerces.parsers.SAXParser");//(md.getClassName());
                Scene.v().getSootClass(md.getClassName());
                //Scene.v().tryLoadClass(md.getClassName(), SootClass.SIGNATURES);
        if (msc.isJavaLibraryClass() || msc.isLibraryClass()) {
            msc = Scene.v().loadClassAndSupport(msc.getName());
            CallGraphObject.elevateClassToApplicationLevel(msc);//msc.setApplicationClass();
        }
        
        //SootClass sc = Scene.v().getSootClass(md.getClassName());        
        SootMethod meth = MethodDefinition.getSootMethod(msc, md);//.ge//getMethodByName(md.getMethodName());//getMethod(md.getMethodName(), paramList2);

        

        //meth.getActiveBody().getDefBoxes().ge
//        Iterator<Edge> ite = cg.edgesInto(meth);
//        if (ite.hasNext()) {
//            System.out.println("Possible calls on method: " + meth);
//        }
//        while (ite.hasNext()) {
//            Edge edge = ite.next();
//            SootMethod srcMth = edge.src(); //LineNumberTag tag = (LineNumberTag)srcMth.getActiveBody().getAllUnitBoxes().get(0).getUnit().getTags().getTag("LineNumberTag");//srcMth.getActiveBody().toString()
//            System.out.println("src context " + edge.srcCtxt());//srcMth.getActiveBody().getLocals().
//            System.out.println("src stmt " + edge.srcStmt());//edge.tgt()
//            System.out.println("src unit " + edge.srcUnit());
//            System.out.println(meth + " may be called by " + srcMth);
//        }
//        List mm = getCallingAncestors(cg, meth);
//        System.out.println("Tgt CI src No: " + meth.getNumber()//getActiveBody()
//                + "\nTgt: " + meth + "\nSet: " + mm);
        
        if (msc.isInterface()) {
            List<SootClass> listSC = Scene.v().getActiveHierarchy().getImplementersOf(meth.getDeclaringClass());
            for (SootClass sc : listSC) {
                List<List<SootMethod>> lList = new ArrayList<>();
                List<SootMethod> lsm = new ArrayList<>();

                lList = getCallStackTraces(cg, MethodDefinition.getSootMethod(sc, md)//sc.getMethod(meth.getName(), meth.getParameterTypes())
                        , lsm, lList);
                int i = 0;
                ps.println("printing call trace for [" + meth.getDeclaringClass().getName() + " " + meth.getDeclaration() + "]");
                printStackTraces(lList, ps);
            }
        }
        else if (msc.isAbstract()) {//Scene.v().getActiveHierarchy().getSubclassesOf(Scene.v().getSootClass("javax.xml.parsers.DocumentBuilder"));//meth.getDeclaringClass().getMethods()
            List<SootClass> listSC = Scene.v().getActiveHierarchy().getSubclassesOfIncluding(meth.getDeclaringClass());//getDirectSubclassesOf(meth.getDeclaringClass());
            for (SootClass sc : listSC) {
                List<List<SootMethod>> lList = new ArrayList<>();
                List<SootMethod> lsm = new ArrayList<>();

                lList = getCallStackTraces(cg, MethodDefinition.getSootMethod(sc, md)//sc.getMethod(meth.getName(), meth.getParameterTypes())
                            , lsm, lList);
                int i = 0;
                ps.println("printing call trace for [" + meth.getDeclaringClass().getName() + " " + meth.getDeclaration() + "]");
                printStackTraces(lList, ps);
            }
        }        
        else {

            List<List<SootMethod>> lList = new ArrayList<>();
            List<SootMethod> lsm = new ArrayList<>();

            lList = getCallStackTraces(cg, meth, lsm, lList);
            int i = 0;
            ps.println("printing call trace for [" + meth.getDeclaringClass().getName() + " " + meth.getDeclaration() + "]");
            printStackTraces(lList, ps);
        //for(List<SootMethod> l : ml) {
            //    System.out.println("Tgt ML src No: " + ++i
            //        + "\nTgt: " + meth + "\nSet: " + l);
            //}
        }
    }
    
    protected void displayExecutionTraceForMethod(CallGraph cg, SootMethod meth, PrintStream ps) {
      
        SootClass msc = meth.getDeclaringClass();
                //Scene.v().tryLoadClass(md.getClassName(), SootClass.SIGNATURES);
        if (msc.isJavaLibraryClass() || msc.isLibraryClass()) {
            msc = Scene.v().loadClassAndSupport(msc.getName());
            CallGraphObject.elevateClassToApplicationLevel(msc);//msc.setApplicationClass();
        }
        
        if (msc.isInterface()) {
            List<SootClass> listSC = Scene.v().getActiveHierarchy().getImplementersOf(meth.getDeclaringClass());
            for (SootClass sc : listSC) {
                List<List<SootMethod>> lList = new ArrayList<>();
                List<SootMethod> lsm = new ArrayList<>();

                lList = getCallStackTraces(cg, MethodDefinition.getOverrideSootMethod(sc, meth)//sc.getMethod(meth.getName(), meth.getParameterTypes())
                        , lsm, lList);
                int i = 0;
                ps.println("printing call trace for [" + meth.getDeclaringClass().getName() + " " + meth.getDeclaration() + "]");
                printStackTraces(lList, ps);
            }
        }
        else if (msc.isAbstract()) {//Scene.v().getActiveHierarchy().getSubclassesOf(Scene.v().getSootClass("javax.xml.parsers.DocumentBuilder"));//meth.getDeclaringClass().getMethods()
            List<SootClass> listSC = Scene.v().getActiveHierarchy().getSubclassesOfIncluding(meth.getDeclaringClass());//getDirectSubclassesOf(meth.getDeclaringClass());
            for (SootClass sc : listSC) {
                List<List<SootMethod>> lList = new ArrayList<>();
                List<SootMethod> lsm = new ArrayList<>();

                lList = getCallStackTraces(cg, MethodDefinition.getOverrideSootMethod(sc, meth)//sc.getMethod(meth.getName(), meth.getParameterTypes())
                            , lsm, lList);
                int i = 0;
                System.out.println("printing call trace for [" + meth.getDeclaringClass().getName() + " " + meth.getDeclaration() + "]");
                printStackTraces(lList, ps);
            }
        }        
        else {

            List<List<SootMethod>> lList = new ArrayList<>();
            List<SootMethod> lsm = new ArrayList<>();

            lList = getCallStackTraces(cg, meth, lsm, lList);
            int i = 0;
            System.out.println("printing call trace for [" + meth.getDeclaringClass().getName() + " " + meth.getDeclaration() + "]");
            printStackTraces(lList, ps);
        //for(List<SootMethod> l : ml) {
            //    System.out.println("Tgt ML src No: " + ++i
            //        + "\nTgt: " + meth + "\nSet: " + l);
            //}
        }
    }
    
    protected int getLineNumber(Stmt s) {
        Iterator ti = s.getTags().iterator();
        while (ti.hasNext()) {
            Object o = ti.next();
            if (o instanceof LineNumberTag) {
                return Integer.parseInt(o.toString());
            }
        }
        return -1;
    }
    
    public void printCallTraces(ActualVulnerabilityItem avi, CallGraph cg, PrintStream ps) {
        List<List<SootMethod>> ltrunk = new ArrayList<>();
        List<SootMethod> atrace = new ArrayList<>();
        for (CallSite cs : avi.getOccurrencesList()) {

            ltrunk = getCallStackTraces(cg, cs.getEdge().src(), atrace, ltrunk);
            for (List<SootMethod> l : ltrunk) {
                l.add(0, cs.getEdge().tgt());
            }
            
            printStackTraces(ltrunk, ps);
        }      
    }
    
    public void printStackTraces(List<List<SootMethod>> list, PrintStream ps) {
        StringBuilder sb = new StringBuilder();
        int n=0;
        for (List<SootMethod> l : list) {
            ++n;
            sb.setLength(0);
            sb.append("* ");//sb.append(n).append(". ");
            for(int i=l.size(); i>0; --i) {
                String s = l.get(i-1).toString();
                s = s.replace("<", "[");
                s= s.replace(">", "]");
                sb.append(s).append(" --> ");
            }
            if (sb.toString().endsWith(" --> ")) {
                sb.delete(sb.lastIndexOf(" --> "), sb.length());
            }
            ps.println(sb);
        }
        //System.out.println("\n");
    }
    
    public List<String> getStackTracesAsStringList(List<List<SootMethod>> list) {
        List<String> retList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        
      
        for (List<SootMethod> l : list) {            
            sb.setLength(0);
            sb.append("* ");//sb.append(n).append(". ");
            for(int i=l.size(); i>0; --i) {
                String s = l.get(i-1).toString();
                s = s.replace("<", "[");
                s= s.replace(">", "]");
                sb.append(s).append(" --> ");
            }
            if (sb.toString().endsWith(" --> ")) {
                sb.delete(sb.lastIndexOf(" --> "), sb.length());
            }
            retList.add(sb.toString());
        }
        
        return retList;
    }
    
    public static List<String> getCallTracesAsStringList(List<CallSite> listCS) {
        CallGraph cg = Scene.v().getCallGraph();
        CallGraphObject cgo = CallGraphObject.getInstance();
        List<List<SootMethod>> ltrunk = new ArrayList<>();
        List<SootMethod> atrace = new ArrayList<>();
        for (CallSite cs : listCS) {

            ltrunk = cgo.getCallStackTraces(cg, cs.getEdge().src(), atrace, ltrunk);
            for (List<SootMethod> l : ltrunk) {
                l.add(0, cs.getEdge().tgt());
            }
        }
        
        return cgo.getStackTracesAsStringList(ltrunk);
    }
    
    public static List<String> getCallTracesForCallSiteAsStringList(CallSite cs) {
        CallGraph cg = Scene.v().getCallGraph();
        CallGraphObject cgo = CallGraphObject.getInstance();
        List<List<SootMethod>> ltrunk = new ArrayList<>();
        List<SootMethod> atrace = new ArrayList<>();
       
        ltrunk = cgo.getCallStackTraces(cg, cs.getEdge().src(), atrace, ltrunk);
        for (List<SootMethod> l : ltrunk) {
            l.add(0, cs.getEdge().tgt());
        }
        
        return cgo.getStackTracesAsStringList(ltrunk);
    }
    
    
    
    private void loadNecessaryClass(String name) {
        SootClass c;
        c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
    }
    
    public void loadMyNecessaryClasses() {
	Scene.v().loadBasicClasses();

        Iterator<String> it = Options.v().classes().iterator();

        while (it.hasNext()) {
            String name = (String) it.next();
            loadNecessaryClass(name);
        }

        Scene.v().loadDynamicClasses();

        for( Iterator<String> pathIt = Options.v().process_dir().iterator(); pathIt.hasNext(); ) {

            final String path = (String) pathIt.next();
            for (String cl : SourceLocator.v().getClassesUnder(path)) {
                Scene.v().loadClassAndSupport(cl).setApplicationClass();
            }
        }

        //Scene.v().prepareClasses();
        //Scene.v().setDoneResolving();
    
    }
    
    private void doTest() {
        SootClass sc = Scene.v().getSootClass("mydom.DomParserExample");
        SootMethod sm = sc.getMethodByName("parseXmlFile");
        PatchingChain<Unit> pcu = sm.getActiveBody().getUnits();JInvokeStmt h;
        
        List<CallSite> listCs = getCallSites(Scene.v().getCallGraph(), sm);
        List<CallSite> listMCE = getMethodCallEdgesOutOfMethod(Scene.v().getCallGraph(), sm);
        for (CallSite cs : listCs) {
            System.out.println(cs);
        }
        for (CallSite cs : listMCE) {
            System.out.println(cs);
        }
        
        Body b = sm.getActiveBody();
        GrimpBody gb = Grimp.v().newBody(b, "jb");//.getUnits()
        List<UnitBox> lut = gb.getUnitBoxes(true); //(lut.get(3))//getUnit().getTags().getUnitBoxes()
        List<UnitBox> luf = gb.getUnitBoxes(false);        
        
        Iterator<Unit> units = gb.getUnits().iterator();
        
        while (units.hasNext()) {
            Unit unit = units.next();
            List<Tag> listTag = unit.getTags();
            List<UnitBox> listUB = unit.getUnitBoxes();
            List<ValueBox> listDB = unit.getDefBoxes();//GStaticInvokeExpr gsie; //gsie.
            List<ValueBox> listVB = unit.getUseBoxes();//Value v; v.getType()
            List<ValueBox> listUDB = unit.getUseAndDefBoxes();
            
            for (ValueBox vb : listUDB) {
                Value v = vb.getValue();
                if (v instanceof InvokeExpr) {
                    AbstractInvokeExpr aie = (AbstractInvokeExpr) v;//Scene.v().getCallGraph().edgesInto(aie.getMethod()).next().srcUnit().getTags()getSrc()//aie.getMethod()
                    GStaticInvokeExpr gsie; GVirtualInvokeExpr gvie; GNewInvokeExpr gnie; 
                    int i=7;
                }                
            }
            
            //((GStaticInvokeExpr)listVB.get(0).getValue()).getMethod();
            GRValueBox grv;//((GRValueBox)listVB.get(0)).
            int i = 6;
            i=5;
            
        }
        StmtBox sb;ValueBox vb;Value v; //v.
        //gb.getUnits().getFirst().getDefBoxes().get(0).getValue().getType().getUseBoxes().getUseBoxes();
        //gb.getLocals().getFirst().
                
        Scene.v().loadClassAndSupport("mydom.DomParserExample").getMethodByName("parseXmlFile").retrieveActiveBody();
        Scene.v().getSootClass("mydom.DomParserExample").getMethodByName("parseXmlFile").getActiveBody();
        DavaBody db = Dava.v().newBody(Grimp.v().newBody(b, "jb"));
        JimpleBody jb;
        //sm.
        List<ValueBox> lud = sm.getActiveBody().getUseAndDefBoxes();
        List<ValueBox> lub = sm.getActiveBody().getUseBoxes();
        int i=0;
    }
    
    public static void doTest2() {
        try {
//            Employee employee = new Employee("uiheroie", 56, 4, "permanent");
//            JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
//            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//            
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            
//            jaxbMarshaller.marshal(employee, System.out);            
//            jaxbMarshaller.marshal(employee, new File("C:/f/employees.xml"));
//            
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Employee employee2 = (Employee) jaxbUnmarshaller.unmarshal(new File("C:/f/employees.xml"));
//            System.out.println("\nreread emp");
//            System.out.println(employee2.toString());
            
            VulnerabilityDefinitionItems vmis = new VulnerabilityDefinitionItems();
            vmis.setVulnerabilityDefinitionItems(VulnerableXMLMethodDefinitions.getVulnerableMethodDefinitionList());
            
            JAXBContext jaxbContext = JAXBContext.newInstance(VulnerabilityDefinitionItems.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            jaxbMarshaller.marshal(vmis, System.out);            
            jaxbMarshaller.marshal(vmis, new File("C:/f/vmis.xml"));            
            
            SchemaOutputResolver sor = new SchemaOutputResolver() {

                @Override
                public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                    File file = new File("C:/f", suggestedFileName);
                    StreamResult result = new StreamResult(file);
                    result.setSystemId(file.toURI().toURL().toString());
                    return result;
                }
            };
            jaxbContext.generateSchema(sor);
            
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            VulnerabilityDefinitionItems vmis2 = (VulnerabilityDefinitionItems) jaxbUnmarshaller.unmarshal(new File("C:/f/vmis.xml"));
            System.out.println("\nreread emp");
            System.out.println(vmis2.toString());
            jaxbMarshaller.marshal(vmis2, System.out);  
            jaxbMarshaller.marshal(vmis2, new File("C:/f/vmis2.xml"));            
            System.exit(0);
        } catch (JAXBException ex) {
            Logger.getLogger(CallGraphObject.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(CallGraphObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, String> getBugFindParametersMap() {
        return bugFindParametersMap;
    }

    public void setBugFindParametersMap(Map<String, String> bugFindParametersMap) {
        this.bugFindParametersMap = bugFindParametersMap;
    }

    public static List<SootClass> getElevatedClasses() {
        return elevatedClasses;
    }
    
    public static void elevateClassToApplicationLevel(SootClass sc) {        
        if (!sc.isApplicationClass()) {
            sc.setApplicationClass();
            getElevatedClasses().add(sc);
        }
    }

    protected void printActualVunerabilitesFound(CallGraph cg, List<ActualVulnerabilityItem> actualVulnerabilities, String outputFormat, boolean printToFile) throws JAXBException, FileNotFoundException {
        OutputStream os = (printToFile) ? 
                    new FileOutputStream(bugFindParametersMap.get(OptionsParser.OUTPUT_FILE_OPT))
                    : System.out;
        
        if (outputFormat.toLowerCase().equals(BugFindConstants.XML_FORMAT)) {
            ActualVulnerabilityItems avis = new ActualVulnerabilityItems();
            avis.setActualVulnerabilityItems(ActualVulnerabilityItemForXML.toActualVulnerabilityItemForXMLs(actualVulnerabilities));            
            XMLUtils.writeXMLToStream(avis, os);
        }
        else {
            PrintStream ps = new PrintStream(os);
         
            if (actualVulnerabilities.size() > 0) {
                ps.println("\n" + actualVulnerabilities.size() + " variant(s) of xxe vulnerabilities found");
            }
            
            int n = 0;
            for (ActualVulnerabilityItem avi : actualVulnerabilities) {
                ++n;
                String classShortName = avi.getVulnerabilityDefinitionItem().getMethodDefinition().getClassName();

                if (classShortName.contains(".")) {
                    int index = classShortName.lastIndexOf(".");
                    classShortName = classShortName.substring(index + 1);
                }
                ps.println("XXE Var-" + n + " due to using " + classShortName
                        + " API. See detail: \n" + avi);
                ps.println("Reason: " + avi.getReason());

                ps.println("Exploitation route");
                printCallTraces(avi, cg, ps);
                ps.println("");
            }
            
            ps.flush();
            ps.close();
        }
        
    }
}
