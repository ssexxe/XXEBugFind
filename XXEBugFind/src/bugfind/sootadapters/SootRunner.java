/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import bugfind.utils.misc.FileUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import soot.CompilationDeathException;
import soot.G;
import soot.Main;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Timers;
import soot.Transform;
import soot.options.Options;
import soot.toolkits.astmetrics.ClassData;
import soot.util.Chain;
import soot.util.HashChain;

/**
 * This is a custom soot runner as the main soot runner is not suitable for what we want to do. For instance, 
 * the printing command-line debug/default printing by soot can be annoying.
 * @author Mikosh
 */
public class SootRunner extends Main {
    private static final Logger logger = Logger.getLogger(SootRunner.class.getName());
    //{logger.setUseParentHandlers(false);}
    
    // Note most of the fields and methods here are just duplicates. This is due to the difficulty in inherting 
    // the soot Main class as most of the fields and methods are private.
    // The altered method is main(...)
    
    
    private static SootRunner instance;
    
    private Date start;
    private Date finish;
    
    /**
     * Gets the instance of SootRunner for the application
     * @return the instance of SootRunner for the application
     */
    public static SootRunner getInstance() {
        if (instance == null) {
            instance = new SootRunner(null);
        }
        
        return instance;
    }
    
    private SootRunner(Singletons.Global g) {
        super(g);
    }
   
    /**
     * Runs soot. This method calls run(args, libpaths) but added is a try catch for OutOfMemoryError
     * @param args the soot arguments
     * @param libPaths the library paths for the application
     */
    public static void main(String[] args, String libPaths) {
        try {
            //Main.v().run(args); // usually this is what is called
            SootRunner.getInstance().run(args, libPaths);
        } catch( OutOfMemoryError e ) {
            G.v().out.println( "Soot has run out of the memory allocated to it by the Java VM." );
            G.v().out.println( "To allocate more memory to Soot, use the -Xmx switch to Java." );
            G.v().out.println( "For example (for 400MB): java -Xmx2000m ..." );
            throw e;
        }
    }
    
    /**
     * Runs soot
     * @param args the soot arguments
     * @param libPaths the library paths for the application
     */
    public void run(String[] args, String libPaths) {
        cmdLineArgs = args;

        start = new Date();

        try {
            Timers.v().totalTimer.start();

            processCmdLine(cmdLineArgs);
            
            autoSetOptions();

            G.v().out.println("Soot started on " + start);

            loadNecessaryClassesAndLibs(libPaths);

            /*
             * By this all the java to jimple has occured so we just check ast-metrics flag
             * 
             * If it is set......print the astMetrics.xml file and stop executing soot
             */
            if(Options.v().ast_metrics()){
        	   	try{
            		OutputStream streamOut = new FileOutputStream("../astMetrics.xml");
            		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
            		writerOut.println("<?xml version='1.0'?>");
            		writerOut.println("<ASTMetrics>");		 		
            		
            		Iterator<ClassData> it = G.v().ASTMetricsData.iterator();
            		while(it.hasNext()){
            			//each is a classData object
            			ClassData cData = it.next();
            			writerOut.println(cData.toString());
            		}

            		writerOut.println("</ASTMetrics>");
             		writerOut.flush();
            		streamOut.close();
            	} catch (IOException e) {
            		throw new CompilationDeathException("Cannot output file astMetrics",e);
            	}
                return;
            }
            
            
            PackManager.v().runPacks();
            //PackManager.v().writeOutput();

            Timers.v().totalTimer.end();

            // Print out time stats.				
            if (Options.v().time())
                Timers.v().printProfilingInformation();

        } catch (CompilationDeathException e) {
            Timers.v().totalTimer.end();
            if(e.getStatus()!=CompilationDeathException.COMPILATION_SUCCEEDED)
            	throw e;
            else 
            	return;
        }

        finish = new Date();

        G.v().out.println("Soot finished on " + finish);
        long runtime = finish.getTime() - start.getTime();
        G.v().out.println(
            "Soot has run for "
                + (runtime / 60000)
                + " min. "
                + ((runtime % 60000) / 1000)
                + " sec.");

    }

    private void printVersion() {
        G.v().out.println("Soot version " + versionString);

        G.v().out.println(
            "Copyright (C) 1997-2010 Raja Vallee-Rai and others.");
        G.v().out.println("All rights reserved.");
        G.v().out.println("");
        G.v().out.println(
            "Contributions are copyright (C) 1997-2010 by their respective contributors.");
        G.v().out.println("See the file 'credits' for a list of contributors.");
        G.v().out.println("See individual source files for details.");
        G.v().out.println("");
        G.v().out.println(
            "Soot comes with ABSOLUTELY NO WARRANTY.  Soot is free software,");
        G.v().out.println(
            "and you are welcome to redistribute it under certain conditions.");
        G.v().out.println(
            "See the accompanying file 'COPYING-LESSER.txt' for details.");
        G.v().out.println();
        G.v().out.println("Visit the Soot website:");
        G.v().out.println("  http://www.sable.mcgill.ca/soot/");
        G.v().out.println();
        G.v().out.println("For a list of command line options, enter:");
        G.v().out.println("  java soot.Main --help");
    }

    private void processCmdLine(String[] args) {

        if (!Options.v().parse(args))
            throw new CompilationDeathException(
                CompilationDeathException.COMPILATION_ABORTED,
                "Option parse error");

        if( PackManager.v().onlyStandardPacks() ) {
            for (Pack pack : PackManager.v().allPacks()) {
                Options.v().warnForeignPhase(pack.getPhaseName());
                for( Iterator<Transform> trIt = pack.iterator(); trIt.hasNext(); ) {
                    final Transform tr = trIt.next();
                    Options.v().warnForeignPhase(tr.getPhaseName());
                }
            }
        }
        Options.v().warnNonexistentPhase();

        if (Options.v().help()) {
            G.v().out.println(Options.v().getUsage());
            throw new CompilationDeathException(CompilationDeathException.COMPILATION_SUCCEEDED);
        }

        if (Options.v().phase_list()) {
            G.v().out.println(Options.v().getPhaseList());
            throw new CompilationDeathException(CompilationDeathException.COMPILATION_SUCCEEDED);
        }

        if(!Options.v().phase_help().isEmpty()) {
            for( Iterator<String> phaseIt = Options.v().phase_help().iterator(); phaseIt.hasNext(); ) {
                final String phase = phaseIt.next();
                G.v().out.println(Options.v().getPhaseHelp(phase));
            }
            throw new CompilationDeathException(CompilationDeathException.COMPILATION_SUCCEEDED);
        }

        if ((!Options.v().unfriendly_mode() && args.length == 0) || Options.v().version()) {
            printVersion();
            throw new CompilationDeathException(CompilationDeathException.COMPILATION_SUCCEEDED);
        }

        postCmdLineCheck();
    }

    private void postCmdLineCheck() {
        if (Options.v().classes().isEmpty()
        && Options.v().process_dir().isEmpty()) {
            throw new CompilationDeathException(
                CompilationDeathException.COMPILATION_ABORTED,
                "No input classes specified!");
        }
    }
    
    private static void loadNecessaryXMLParsingImplClasses() {//Options.v().classes()
        String s = null;
        try {
            s = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl";
            Scene.v().loadClassAndSupport(s).setLibraryClass();//Scene.v().loadClassAndSupport("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactojryImpl")
            s = "com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.org.apache.xerces.internal.parsers.SAXParser";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.org.apache.xerces.internal.parsers.jaxp.SAXParserImpl.JAXPSAXParser";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.xml.internal.stream.XMLInputFactory";            
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
            s = "com.sun.xml.internal.stream.XMLEventReaderImpl";
            Scene.v().loadClassAndSupport(s).setLibraryClass();
        } catch (Exception ex) {
            Logger.getLogger(SootRunner.class.getName()).log(Level.SEVERE, "Cannot find some jdk XML parsing classes in the jdk. i.e. '" + s 
                    + "'. It might have been changed or moved. This will lead to the tool missing possible XXE vulnerabilities. "
                    + "Consider using the -l option via the command-line to add XML lib "
                    + "dependables eg -l \"pathToXercesXMLlib/Xerces.jar\"", ex);
        }
    } 
    
    private void loadNecessaryClass(String name) {
        SootClass c;
        c = Scene.v().loadClassAndSupport(name);
        c.setApplicationClass();
    }
    
    private void prepareClasses() {
        // Remove/add all classes from packageInclusionMask as per -i option
        Chain<SootClass> processedClasses = new HashChain<SootClass>();
        while(true) {
            Chain<SootClass> unprocessedClasses = new HashChain<SootClass>(Scene.v().getClasses());
            unprocessedClasses.removeAll(processedClasses);
            if( unprocessedClasses.isEmpty() ) break;
            processedClasses.addAll(unprocessedClasses);
            for (SootClass s : unprocessedClasses) {
                if( s.isPhantom() ) continue;
                if(Options.v().app()) {
                    s.setApplicationClass();
                }
                if (Options.v().classes().contains(s.getName())) {
                    s.setApplicationClass();
                    continue;
                }
//                for( Iterator<String> pkgIt = excludedPackages.iterator(); pkgIt.hasNext(); ) {
//                    final String pkg = (String) pkgIt.next();
//                    if (s.isApplicationClass()
//                    && (s.getPackageName()+".").startsWith(pkg)) {
//                            s.setLibraryClass();
//                    }
//                }
                for( Iterator<String> pkgIt = Options.v().include().iterator(); pkgIt.hasNext(); ) {
                    final String pkg = (String) pkgIt.next();
                    if ((s.getPackageName()+".").startsWith(pkg))
                        s.setApplicationClass();
                }
                if(s.isApplicationClass()) {
                    // make sure we have the support
                    Scene.v().loadClassAndSupport(s.getName());
                }
            }
        }
    }
    
    private void loadNecessaryClassesAndLibs(String libPaths) {
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
        
        createCustomEntryPointsIfNecessary();//System.out.println("ENTRY POINTS" + Scene.v().getEntryPoints());
        
        // wen need to load the lib paths as 
        List<String> libPathList = FileUtil.extractPaths(libPaths, File.pathSeparator);
        for( Iterator<String> pathLibLoc = libPathList.iterator(); pathLibLoc.hasNext(); ) {

            final String path = (String) pathLibLoc.next();
            logger.log(Level.INFO, "adding path: {0}", path);
            
            for (String cl : SourceLocator.v().getClassesUnder(path)) {
                //if (cl.toLowerCase().contains("jaxen")) continue;//System.out.println("cl: " + cl);
                try {                   
                    Scene.v().loadClassAndSupport(cl).setLibraryClass();    
                    //Scene.v().loadClass(cl, SootClass.HIERARCHY).setLibraryClass();               //Scene.v().tryLoadClass(cl, SootClass.HIERARCHY).setLibraryClass();//loadClassAndSupport(cl).setLibraryClass();
                } catch(Exception ex) {
                    logger.log(Level.SEVERE, "Error! resolving for " + cl + " err: ", ex);                    
                    //Scene.v().tryLoadClass(cl, SootClass.DANGLING).setLibraryClass();
                }
            }
        }
        
        loadNecessaryXMLParsingImplClasses();
        

        prepareClasses();
        Scene.v().setDoneResolving();
    }
    
    /**
     * This is necessary for applications that do not have a main class
     */
    private static void createCustomEntryPointsIfNecessary() {
        if (!Scene.v().hasMainClass()) {
            List<SootClass> allClasses = new ArrayList<>(Scene.v().getApplicationClasses());
            ArrayList<SootMethod> entrypoints = new ArrayList<SootMethod>();
            
            for (SootClass klass : allClasses) {                
                // adding all non-abstract method as entrypoint
                for (SootMethod m : klass.getMethods()) {
                    if (!m.isAbstract()) {
                        entrypoints.add(m);
                    }
                }
            }
            
            Scene.v().setEntryPoints(entrypoints);
            
            logger.log(Level.INFO, "Created custom entry point main class");            
        }
    }
}
