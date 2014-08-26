/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.main;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mikosh
 */
public class OptionsParser {
    // these are the commands
    public static String VMD_OPT = "-vmd";
    public static String DIR_OPT = "-d";
    public static String LIB_OPT = "-l";
    public static String FORMAT_OPT = "-f";
    public static String RULESET_OPT = "-rs";
    public static String RT_LIB_LOC_OPT = "-rtloc";
    public static String SOOT_LIB_LOC_OPT = "-sootloc";

    private OptionsParser() {
    }
    
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
        } else if (str.toLowerCase().equals(FORMAT_OPT.toLowerCase())) {
            return true;
        } else if (str.toLowerCase().equals(RULESET_OPT.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
}
