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
import bugfind.xxe.XXEVulnerabilityDetector;
import bugfind.xxe.xmlobjects.ActualVulnerabilityItemForXML;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import soot.G;
import soot.Local;
import soot.PackManager;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;

/**
 * This is the callgraph object that encapsulates the soot CallGraph and provides utility methods
 * such as getCallTraces and so on which cannot be gotten directly from soot
 * @author Mikosh
 */
public class CallGraphObject {
    private static final Logger logger = Logger.getLogger(CallGraphObject.class.getName());
    //{logger.setUseParentHandlers(false);}
    
    /**
     * The instance of the call graph object used by this class
     */
    protected static CallGraphObject callGraphObject;
    
    /**
     * Holds all the parameters and their values entered by the user via the command-line
     */
    protected Map<String, String> bugFindParametersMap;
    
    /**
     * Holds a list of vulnerable method definitions used by this class
     */
    protected List<MethodDefinition> vulMethodDefinitionList;
    
    /**
     * This holds a list of all elevated classes.i.e, classes that were elevated from library or javalibrary level
     * to application level. It's sometimes necessary to elevate classes so that they can be returned in a call trace
     */
    private static List<SootClass> elevatedClasses = new ArrayList<>();

    /**
     * Gets the call graph instance of this class
     * @return the call graph instance of this class 
     */
    public static CallGraphObject getInstance() {
        if (callGraphObject == null) {
            callGraphObject = new CallGraphObject();
        }
        
        return callGraphObject;
    }
    
    /**
     * Gets the call graph instance of this class having the specified bug find parameters
     * @return the call graph instance of this class having the specified bug find parameters
     * @param bugFindParametersMap the bugfind parameters to set
     */
    public static CallGraphObject getInstance(Map<String, String> bugFindParametersMap) {
        CallGraphObject cgo = getInstance();        
        cgo.setBugFindParametersMap(bugFindParametersMap);
        
        return cgo;
    }
    
    /**
     * Made private  to prevent instatiation of this class directly
     */
    private CallGraphObject() {}
    
    /**
     * Constructs a new call graph object when given the  parameters
     * @param bugFindParametersMap the parameters to set
     */
    protected CallGraphObject(Map<String, String> bugFindParametersMap) {
        this.bugFindParametersMap = bugFindParametersMap;
    }   
    
    /**
     * Runs an analysis over the given application. It constructs a call graph for the whole application for use by the
     * XXE Vulnerability detector
     * @throws FileNotFoundException
     * @throws FileSystemException
     * @throws Exception 
     */
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
            dirLocation = (sb.lastIndexOf(pathSep) == sb.length() - 1) ? sb.substring(0, sb.length() - 1) : sb.toString();
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
                libLocation = (sb.lastIndexOf(pathSep) == sb.length() - 1) ? sb.substring(0, sb.length() - 1) : sb.toString();
            } 
            else {
                libLocs = new String[]{libLocation};
            }
        }

        // do checkings for output type and output file
        if (outputFormat == null && bugFindParametersMap.containsKey(OptionsParser.OUTPUT_FORMAT_OPT)) {
            throw new Exception("There is no specified value for " + OptionsParser.OUTPUT_FORMAT_OPT + " option");
        }

        if (outputFormat != null) {
            if (!outputFormat.trim().toLowerCase().equals(BugFindConstants.TEXT_FORMAT)
                    && !outputFormat.trim().toLowerCase().equals(BugFindConstants.XML_FORMAT)) {
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

        // get rt directory location // rt.jar and other java libs are necessary to run soot        
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
                        + "for call graph generation. Use the "
                        + OptionsParser.RT_LIB_LOC_OPT + " option to specify the directory containing rt.jar");
            }

            rtDirectory = guessedRTdir;
        }

        String javaNecessaryJarsLoc = Utils.join(fileUtil.getAllFilesWithExtension(rtDirectory, ".jar"), pathSep);
        String cpOptionString = (!libLocation.trim().isEmpty()) ? dirLocation + pathSep + libLocation + pathSep + javaNecessaryJarsLoc
                : dirLocation + pathSep + javaNecessaryJarsLoc;

        List<String> argsList = new ArrayList<String>();

        // add all the necessary aguments to start the call graph generation
        argsList.addAll(Arrays.asList(new String[]{
            "-w",            
            "-p",
            "cg",
            "all-reachable:true",
            "-p",
            "jb",
            "use-original-names:false",
            "-cp",
            cpOptionString}));
        for (String s : dirLocs) {
            if (!s.trim().isEmpty()) {
                argsList.add("-process-dir");
                argsList.add(s.trim());
                logger.log(Level.INFO, "process-dir: {0}", s.trim());                
            }
        }
        logger.log(Level.INFO, "class-path:\n{0}", cpOptionString);
        // get get vulnerable method definition list
        String vulmethodDefs = bugFindParametersMap.get(OptionsParser.VMD_OPT);
        //if (vulmethodDefs == null || vulmethodDefs.trim().isEmpty()) {
        //    throw new Exception("There is no specified value for " + OptionsParser.VMD_OPT + " option");
        //}
        this.vulMethodDefinitionList = getVulnerableMethodDefinitions(vulmethodDefs);
        
        // add our custom scene transformer to deal with the call graph finds
        addBugFindSootTransformer();

        String[] args = argsList.toArray(new String[0]);

        Options.v().set_keep_line_number(true);
        Options.v().set_include_all(true);
        Options.v().set_allow_phantom_refs(true);        
        PhaseOptions.v().setPhaseOption("tag.ln", "on");        
        //PhaseOptions.v().setPhaseOption("cg", "enabled:false");        
        PhaseOptions.v().setPhaseOption("cg", "all-reachable:true");
        //PhaseOptions.v().setPhaseOption("jb", "use-original-names:false");//PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
        //PhaseOptions.v().setPhaseOption("cg.paddle", "enabled:true");//Options.v().parse(args);
        
        //redirect all soots outputs to soot.txt as it annoyingly affects the output of our program.
        G.v().out = new PrintStream(new File("soot.txt")); 
        
        
        logger.log(Level.INFO, "Started...");
        // start our custom soot runnet
        SootRunner.main(args, libLocation);//        soot.Main.main(args);
    }

    /**
     * Adds bugfind's transformer to be called by soot.
     * This is necessary for our analysis
     */
    protected void addBugFindSootTransformer() {
        final CallGraphObject cgo = this;
        
        // add our custom scene transformer to deal with the call graph finds
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.BugFindTrans", new SceneTransformer() {
            
            @Override
            protected void internalTransform(String phaseName, Map options) {
                //CHATransformer.v().transform();
                CallGraph cg = Scene.v().getCallGraph();
                
                // create our xxe vulnerability detector and find vulnerabilities
                XXEVulnerabilityDetector xvd = new XXEVulnerabilityDetector(cgo);
                List<ActualVulnerabilityItem> actualVulnerabilities = xvd.findVulnerabilities();
                
                // get the output format
                String outputFormat = (bugFindParametersMap.get(OptionsParser.OUTPUT_FORMAT_OPT) == null)
                        ? BugFindConstants.TEXT_FORMAT : bugFindParametersMap.get(OptionsParser.OUTPUT_FORMAT_OPT);
                logger.log(Level.INFO, "Finished run");
                
                try {
                    String outputFile = bugFindParametersMap.get(OptionsParser.OUTPUT_FILE_OPT);
                    printActualVunerabilitesFound(cg, actualVulnerabilities, outputFormat, (outputFile != null));
                    
                    if (outputFile != null) {
                        logger.log(Level.INFO, "Finished writing to file " + outputFile);
                    }
                } catch (JAXBException ex) {
                    Logger.getLogger(CallGraphObject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CallGraphObject.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (actualVulnerabilities == null || actualVulnerabilities.isEmpty()) {
                    logger.log(Level.INFO, "No exploitable XXE vulnerabilities found");
                }
                
                logger.log(Level.INFO, "About to terminate...");
            }

        }));
    }
        
        
    /**
     * Gets a list of calling ancestors in a single list. Does not allow for tree-like call
     * @param cg the callgraph to use
     * @param src the method whose calling ancestors is to be obtained
     * @return list of calling ancestors in a single list 
     */
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
                //consider only applications classes (to scale up at the cost of soundness)
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

    /**
     * Get all the callsites for a particular soot method. 
     * Note: An approximation is done to return only application class
     * @param cg the callgraph to use
     * @param method the method whose call sites are to be obtained
     * @return all the callsites for a particular soot method 
     */
    public List<CallSite> getCallSites(CallGraph cg, SootMethod method) {
        List<CallSite> listCS = new ArrayList<>();
        // get edges into the method and return the list
        Iterator<Edge> ite = cg.edgesInto(method);
        while (ite.hasNext()) {
            Edge edge = ite.next();
            if (edge.src().getDeclaringClass().isApplicationClass()) {
                listCS.add(new CallSite(edge));
            }
        }
                
        // this is necessary to rectify the problem of missing edges for Interfaces and abstact classes types
        // The reason is that soot CHA will return edges only for the concrete class implementations.
        // Hence we get the concrete classes and use it to track the base class
        if (listCS.isEmpty() && method.getDeclaringClass().isAbstract() || method.getDeclaringClass().isInterface()) {            
            if (method.getDeclaringClass().isInterface()) {
                // get all implemeters of the interface
                List<SootClass> subclasses = Scene.v().getActiveHierarchy().getImplementersOf(method.getDeclaringClass());
                
                // get all subclasses
                for (SootClass subclass : subclasses) {
                    SootMethod sm = MethodDefinition.getOverrideSootMethod(subclass, method);
                    if (sm != null) {
                        // get the edges                        
                        Iterator<Edge> iteT = cg.edgesInto(sm);
                        while (iteT.hasNext()) {
                            Edge edge = iteT.next();
                            // ensure that only application classes are considered
                            if (edge.src().getDeclaringClass().isApplicationClass()
                                    && !isElevatedClass(edge.src().getDeclaringClass())) {
                                Local l = (Local) SimpleIntraDataFlowAnalysis.getInvokedLocal(edge.srcStmt());

                                if (l.getType().toString().equals(method.getDeclaringClass().getName())) {
                                    // add missing edge for interface type
                                    Edge missingEdge = new Edge(edge.src(), edge.srcStmt(), method);
                                    
                                    Scene.v().getCallGraph().addEdge(missingEdge);
                                    
                                    CallSite cs = new CallSite(missingEdge);
                                    
                                    // now add callsite to calltrace list returned
                                    if (!listCS.contains(cs)) {
                                        listCS.add(cs);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if (method.getDeclaringClass().isAbstract()) {
                // the steps are similar for the interface if clause just above this one
                List<SootClass> subclasses = Scene.v().getActiveHierarchy().getSubclassesOf(method.getDeclaringClass());
                
                for (SootClass subclass : subclasses) {
                    SootMethod sm = MethodDefinition.getOverrideSootMethod(subclass, method);
                    if (sm != null) {
                        Iterator<Edge> iteT = cg.edgesInto(sm);
                        while (iteT.hasNext()) {
                            Edge edge = iteT.next();
                            
                            if (edge.src().getDeclaringClass().isApplicationClass()
                                    && !isElevatedClass(edge.src().getDeclaringClass())) {
                                Local l = (Local) SimpleIntraDataFlowAnalysis.getInvokedLocal(edge.srcStmt());

                                if (l.getType().toString().equals(method.getDeclaringClass().getName())) {
                                    // add missing edge for interface type
                                    Edge missingEdge = new Edge(edge.src(), edge.srcStmt(), method);
                                    
                                    Scene.v().getCallGraph().addEdge(missingEdge);
                                    
                                    CallSite cs = new CallSite(missingEdge);
                                    
                                    // now add callsite to calltrace list returned
                                    if (!listCS.contains(cs)) {
                                        listCS.add(cs);
                                    }
                                }
                            }
                        }
                    }
                }
                
            }
        }
        
        return listCS;
    }
    
    /**
     * Gets a list of all callsites of the callee method in the caller method's body
     * @param cg the callgraph to use
     * @param callee the method being called
     * @param caller the caller
     * @return a list of all callsites of the callee method in the caller method's body 
     */
    public List<CallSite> getCallSitesInMethod(CallGraph cg, SootMethod callee, SootMethod caller) {
        List<CallSite> listCS = getCallSites(cg, callee);
        Iterator<CallSite> ite = listCS.iterator();
        
        while (ite.hasNext()) {
            if (!ite.next().getEdge().src().getSignature().equals(caller.getSignature())) {
                ite.remove();
            }
        }
        
        return listCS;
    }
    
    /**
     * Get edges corresponding to all the methods that were called in the specified method
     * @param cg the callgraph that contains the method
     * @param method the specified method
     * @return edges corresponding to all the methods that were called in the specified method 
     */
    public List<CallSite> getMethodCallEdgesOutOfMethod(CallGraph cg, SootMethod method) {
        List<CallSite> listCS = new ArrayList<>();
        
        Iterator<Edge> ite = cg.edgesOutOf(method);
        while (ite.hasNext()) {
            listCS.add(new CallSite(ite.next()));
        }
        
        return listCS;
    }
    
    /**
     * Gets the call traces for a specified method using backward trace analysis. It has been approximated to
     * include only soot's application classes (ie classes in the main jar file being analysed; jars specifed
     * using the -d option). Other classes such as library, javalibrary classes are not included to prevent 
     * bloating of the call trace. Check Sable Soot online for more info
     * @param cg the callgraph that contains the method
     * @param meth the method whose call stack trace is to be traced
     * @param atraceList a list contain a single-flow call trace
     * @param mainList the main list containing branches of the calltraces if need
     * @return  the call traces for a specified method
     */
    protected List<List<SootMethod>> getCallStackTraces(CallGraph cg, SootMethod meth, List<SootMethod> atraceList, List<List<SootMethod>> mainList) {
        // limit the max paths. to prevent infinite call traces.
        if (mainList.size() > 3000) return mainList;
        
        List<SootMethod> currCallersList = new ArrayList<>();
        if (!mainList.contains(atraceList)) {
            mainList.add(atraceList);
        }
        
        // first get all the edges into the method
        Iterator<Edge> ite = cg.edgesInto(meth);
        
        // first check if it has a caller (ie. ite.hasNext should return true). Also try to optimize call trace to
        // include only application classes
        if (ite.hasNext() && (!atraceList.isEmpty() && !atraceList.get(atraceList.size() - 1).equals(meth)) 
                && (meth.getDeclaringClass().isApplicationClass()) || atraceList.isEmpty()) {
            atraceList.add(meth);
        }
                
        // now loop through all the occurrence edges and add them. Make sure to add only application classes as
        // library classes (ie xml libs) will only make the call trace bloated and not understandable
        while (ite.hasNext()) {
            Edge currEdge = ite.next();// the tostring of this gives me what i want
            if (currEdge.src().getDeclaringClass().isApplicationClass() && !atraceList.contains(currEdge.src())
                    && !isElevatedClass(currEdge.src().getDeclaringClass())) {//NSOT
                currCallersList.add(currEdge.src());       //((JAssignStmt)currEdge.srcUnit()).leftBox.getValue().getType()
            }
        }
        
        // duplicate traces if needed to get the tree like structure of the traces
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
    
    /**
     * Duplicates a list. Use specifically for the getCallStackTraces(...) method
     * @param <T> the type of the object contained in the list
     * @param list the list to be duplicated
     * @return the duplicated of the list
     */
    protected <T> List<T> duplicateList(List<T> list) {
        List<T> l = new ArrayList<>();
        for(T t : list) {
            l.add(t);
        }
        
        return l;
    }
   
    /**
     * Gets vulnerable method definitions from the conforming string as a List. For instance when the following
     * command is given via the command-line (-vmd stands for vulnerable method definitons)
     * <code>
     * -vmd "com.mypack.VulnerableMethod1(java.lang.String, int);com.parsers.MyVulparser(int, boolean)"
     * <code>
     * will be split into 2 objects contained int the list returned.
     * Note theo use ';' as the separator of the two methods
     * @param methodDefListAsString the string containing the method defs
     * @return a list contiaining vulnerable method definitions
     * @throws Exception 
     */
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

    /**
     * Displays the call traces for a particular method and writes it to the specified stream.
     * Note: the stream is not closed after the write and hence is the responsibility of the developer to close it
     * @param cg the callgraph to use
     * @param md the method defintion to use
     * @param ps the print stream to write to
     */
    protected void displayCallTracesForMethod(CallGraph cg, MethodDefinition md, PrintStream ps) {
        SootClass msc = Scene.v().getSootClass(md.getClassName());
        // elevate the class if needed or else it wont return edges
        if (msc.isJavaLibraryClass() || msc.isLibraryClass()) {
            msc = Scene.v().loadClassAndSupport(msc.getName());
            CallGraphObject.elevateClassToApplicationLevel(msc);
        }
        
        SootMethod meth = MethodDefinition.getSootMethod(msc, md);
        
        // work around for interfacce method as Soot's CHA doesn't return eges for this
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
        
        // work around for abstract method as Soot's CHA doesn't return eges for this
        else if (msc.isAbstract()) {
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
        }
    }
    
    /**
     * Print all call traces for vulnerable points found in the actual vulnerability item passed to it
     * Note the print stream is not closed after the printing.
     * @param avi the actual vulnerability item
     * @param cg the call graph to use
     * @param ps the print stream to use
     */
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
    
    /**
     * Print stack traces. Used for displayCallTraces and printCall
     * @param list the list containg the call traces
     * @param ps the printstream to use
     */
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
    }

    /**
     * Gets the stack traces as a string list
     * @param list
     * @return 
     */
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
    
    /**
     * Gets the call traces a string list
     * @param listCS the list containing the call sites
     * @return the call traces a string list 
     */
    public static List<String> getCallTracesAsStringList(List<CallSite> listCS) {
        CallGraph cg = Scene.v().getCallGraph();
        CallGraphObject cgo = CallGraphObject.getInstance();
        List<List<SootMethod>> ltrunk = new ArrayList<>();
        List<SootMethod> atrace = new ArrayList<>();
        
        // for each call site
        for (CallSite cs : listCS) {

            ltrunk = cgo.getCallStackTraces(cg, cs.getEdge().src(), atrace, ltrunk);
            for (List<SootMethod> l : ltrunk) {
                l.add(0, cs.getEdge().tgt());
            }
        }
        
        return cgo.getStackTracesAsStringList(ltrunk);
    }
    
    /**
     * Get call traces for a call site as a string list
     * @param cs the call site to use
     * @return call traces for a call site as a string list 
     */
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
    
    /**
     * Get the find bug parameters map object
     * @return the find bug parameters map object
     */
    public Map<String, String> getBugFindParametersMap() {
        return bugFindParametersMap;
    }

    /**
     * Sets the find bugs parameters map object
     * @param bugFindParametersMap the map to use
     */
    public void setBugFindParametersMap(Map<String, String> bugFindParametersMap) {
        this.bugFindParametersMap = bugFindParametersMap;
    }

    /**
     * Gets the classes that were elevated from library level to application level. The elevation of
     * some classes is necessary in order to solve the missing edge problem in soot 
     * @return the classes that were elevated from library to application level
     */
    public static List<SootClass> getElevatedClasses() {
        return elevatedClasses;
    }
    
    /**
     * Returns whether a class was elevated from a library or java-library level to application level
     * @param sc the soot class to use
     * @return whether a class was elevated from a library or java-library level to application level 
     */
    public static boolean isElevatedClass(SootClass sc) {
        return getElevatedClasses().contains(sc);
    }
    
    /**
     *  Elevates a class from a library or java-library level to application level
     * @param sc the soot class to elevate
     */
    public static void elevateClassToApplicationLevel(SootClass sc) {        
        if (!sc.isApplicationClass()) {
            sc.setApplicationClass();
            getElevatedClasses().add(sc);
        }
    }

    /**
     * Used to print actual vulnerabilites found
     * @param cg the call graph to use
     * @param actualVulnerabilities the actual vulnerabilities found
     * @param outputFormat the output format
     * @param printToFile whether to print to file or not
     * @throws JAXBException
     * @throws FileNotFoundException 
     */
    protected void printActualVunerabilitesFound(CallGraph cg, List<ActualVulnerabilityItem> actualVulnerabilities, 
            String outputFormat, boolean printToFile) throws JAXBException, FileNotFoundException {
        
        logger.log(Level.FINE, "Compiling report");
        
        if (printToFile) {
            logger.log(Level.FINE, "Writing to file {0}", bugFindParametersMap.get(OptionsParser.OUTPUT_FILE_OPT));
        }
        else {
            logger.log(Level.FINE, "Writing to console");
        }
        
        OutputStream os = (printToFile) ? 
                    new FileOutputStream(bugFindParametersMap.get(OptionsParser.OUTPUT_FILE_OPT))
                    : System.out;
        
        logger.log(Level.FINE, "Output format {0}", outputFormat);
        
        if (outputFormat.toLowerCase().equals(BugFindConstants.XML_FORMAT)) {
            ActualVulnerabilityItems avis = new ActualVulnerabilityItems();
            avis.setActualVulnerabilityItems(ActualVulnerabilityItemForXML.toActualVulnerabilityItemForXMLs(actualVulnerabilities));            
            XMLUtils.writeXMLToStream(avis, os);
        }
        else {
            try (PrintStream ps = new PrintStream(os)) {
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
                    ps.println("XXE Variant-" + n + " due to using " + classShortName
                            + " API. See detail: \n" + avi);
                    ps.println("Reason: " + avi.getReason());
                    
                    ps.println("Exploitation route(s)");
                    printCallTraces(avi, cg, ps);
                    ps.println("");
                }
                
                ps.flush();
            }
        }
        
    }
}
