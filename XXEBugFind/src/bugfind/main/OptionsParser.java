/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.main;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the options parser object for XXEBugFind. This class parsers the command line arguments in to a
 * Map object. Use the static <code> OptionsParser.parse(args) </code> method which returns a Map object
 * @author Mikosh
 */
public class OptionsParser {
    // these are the commands (or keys that can be used to retrieve values from the Map object retunred)
    /**
     * This is the Key that represents the Vulnerable Method Definition option
     */
    public static String VMD_OPT = "-vmd";
    
    /**
     * This Key corresponds to the java application to be analysed
     */
    public static String DIR_OPT = "-d";
    
    /**
     * This key corresponds to the supporting libraries of the java application to be analysed
     */
    public static String LIB_OPT = "-l";
    
    /**
     * this key specifies the format of the output. 
     * <p><b>NOTE:</b> The values that can be returned for the map corresponding to this key are
     * XML and TEXT
     */
    public static String OUTPUT_FORMAT_OPT = "-f";
    
    /**
     * this key specifies the file to output findings to.
     */
    public static String OUTPUT_FILE_OPT = "-o";
    
    /**
     * This key corresponds to the ruleset option.
     */
    public static String RULESET_OPT = "-rs";
    
    /**
     * This key corresponds to the rt.jar location. Users can use this key the rt.jar location if the application
     * fails to find it
     */
    public static String RT_LIB_LOC_OPT = "-rtloc";
    
    /**
     * This key corresponds to the soot library location
     */
    public static String SOOT_LIB_LOC_OPT = "-sootloc";
    
    /**
     * This key corresponds to the help option
     */
    public static String HELP_OPT = "--help";

    private OptionsParser() {
    }
    
    /**
     * Parses the command line arguments and returns a Map
     * @param args the command line arguments to be parsed
     * @return a Map corresponding to the command line arguments parsed
     */
    public static Map<String, String> parse(String[] args) {
        if (args == null || args.length <1) {
            throw new IllegalArgumentException("Commandline arguments cannot be empty. Use --help to see how to use tool");
        }
        
        Map<String, String> map = new HashMap();
        
        for (int i=0; i<args.length; i+=2) {
            if (!isCommandStr(args[i])) {
                throw new IllegalArgumentException(args[i] + " is an invalid command. Use --help to see how to use tool");
            }
            if (map.containsKey(args[i])) {
                throw new IllegalArgumentException("Providing double values for " + args[i] + ". Previous value given was "
                        + map.get(args[i]));
            }
            // 
            if (i+1 >= args.length) {
                throw new IllegalArgumentException("Already reached end of arguments. Expected a value for command " + args[i]);
            }
            
            map.put(args[i], args[i+1]);
        }       
        
        return map;
    }
    
    private static boolean isCommandStr(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        } else if (str.toLowerCase().equals(DIR_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(VMD_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(LIB_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(OUTPUT_FORMAT_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(OUTPUT_FILE_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(RULESET_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(HELP_OPT.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Checks if the help option has been specified in the given arguments
     * @param args the string args to check
     * @return true if the help option has been specified in the given arguments or false if otherwise
     */
    public static boolean containsHelpOption(String[] args) {
        if (args == null || args.length == 0) {
            return false;
        }
        
        for (String str : args) {
            if (str.toLowerCase().equals(HELP_OPT.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
}
