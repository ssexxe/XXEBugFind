/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.main;

import bugfind.sootadapters.CallGraphObject;
import bugfind.utils.misc.BugFindConstants;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This is the main class for XXE BugFind. Entry into the application is via BugFindMain.main(...)
 * @author Mikosh
 */
public class BugFindMain {

    /**
     * This is the main entry point into the application
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        preprocessHelpOption(args);
        setUpLogging();
        
        //args = DiscardedMeths.mockargs();
        
        Map<String, String> inputMap = OptionsParser.parse(args);
  
        CallGraphObject cgo = CallGraphObject.getInstance(inputMap);
        try {
            cgo.runAnalysis();
        } catch (FileSystemException ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private static void setUpLogging() {       
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();
        try {
            logManager.readConfiguration(BugFindMain.class
                    .getResourceAsStream("/bugfind/utils/misc/logging.properties"));            
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(BugFindMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Gets the help message string for XXEFindBug
     * @return  the help message string for XXEFindBug
     */
    public static String getHelpMessage() {       
        StringBuilder sb = new StringBuilder();
        String L_SP1 = "    ", L_SP2 = "     ", L_SP3 = "               ";
        
        sb.append("_______________________________________________________________________________").append("\n");
        sb.append("XXEBugFind Version ").append(BugFindConstants.APP_VERSION).append(" Sponsors: ")
                .append("UCL & Google UK").append(" Copyright (C) 2014").append("\n\n");
        sb.append("This is a tool for detecting XML External Entity (XXE) vulnerabilities in java applications.").append("\n");
        sb.append("\nHelp Mode entered ").append(OptionsParser.HELP_OPT).append("\n");
        sb.append("Usage: -d [-l <value>] [-f <value>] [-o <value>] [-rs <value>] [-rtloc <value>]").append("\n");
        sb.append("Basic commands are shown below:\n\n");
        sb.append(L_SP1).append("-d").append("\n");
        sb.append(L_SP3).append("Use this to specify the jar file or files you want to analyse").append("\n");
        sb.append(L_SP3).append("You can specify multiple files separated by a semicolon. It is").append("\n");
        sb.append(L_SP3).append("best to always enclose the paths in quotations to prevent").append("\n");
        sb.append(L_SP3).append("error when parsing your commmands. See sample usage below.").append("\n");
        
        sb.append(L_SP1).append("-l").append("\n");
        sb.append(L_SP3).append("Specifies one one more libraries your java application is").append("\n");
        sb.append(L_SP3).append("dependent on. Not all have to be included. The ones that are").append("\n");
        sb.append(L_SP3).append("necessary are any custom XML parser libraries used if any e.g. ").append("\n");
        sb.append(L_SP3).append("jdom-2.0.5.jar for JDOM. See sample usage at the bottom").append("\n");
        
        sb.append(L_SP1).append("-f").append("\n");
        sb.append(L_SP3).append("Specifies the format of the output. Currently supported formats").append("\n");
        sb.append(L_SP3).append("are xml or text (in lowercase) e.g. -f xml").append("\n");        
        
        sb.append(L_SP1).append("-o").append("\n");
        sb.append(L_SP3).append("Specifies the output location. Use this option if you want to ").append("\n");
        sb.append(L_SP3).append("save the output in a file. It's best to enclose in quotation").append("\n");
        sb.append(L_SP3).append("marks e.g. -o \"/users/sse/outputFile\"").append("\n");
        
        sb.append(L_SP1).append("-rs").append("\n");
        sb.append(L_SP3).append("Specifies the location of a custom ruleset to be used by the").append("\n");
        sb.append(L_SP3).append("tool. The rule-set file should be an XML file containing all").append("\n");
        sb.append(L_SP3).append("the vulnerabilities to be checked for. This situation may be").append("\n");
        sb.append(L_SP3).append("suitable for vulnerabilities not covered by our tool. ").append("\n");
        sb.append(L_SP3).append("Example -rs \"/users/sse/MyRuleset.xml\"").append("\n");
        
        sb.append(L_SP1).append("-rtloc").append("\n");
        sb.append(L_SP3).append("Specifies the directory containing rt.jar which is usually ").append("\n");
        sb.append(L_SP3).append("found in the java installation directory. This command is only").append("\n");
        sb.append(L_SP3).append("needed when the tool is unable to find this file as it is").append("\n");
        sb.append(L_SP3).append("requiredd to run the tool. E.g. -rtloc \"/u/path-to-rt-folder\"").append("\n");
        sb.append("\n");
        sb.append(L_SP1).append("Sample Usage:\n");
        sb.append(L_SP2).append("java -jar XXEBugFind.jar -d \"myjar1.jar; myjar2.jar\" -l ").append("\n");
        sb.append(L_SP2).append("\"libloc/jdom-2.jar;dom4jlib/dom4j-2.jar\" -f text -o output.txt").append("\n");
        sb.append("For more information check the XXEBugFind manual");
        return sb.toString();
    }
    
    /**
     * Check if the user entered the --help command
     * @param args the command line arguments to check
     */
    public static void preprocessHelpOption(String[] args) {
        if (OptionsParser.containsHelpOption(args)) {
            System.out.println(getHelpMessage());
            System.exit(0);
        }
    }
}
