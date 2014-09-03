/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

/**
 * Provides some common utils
 * @author Mikosh
 */
public class Utils {
    /**
     * Joins the string array into a string using the specified joinTerm
     * @param strArray the string array to use
     * @param joinTerm the join term to use
     * @return a string comprising of all the elements of the array joined
     */
    public static String join(String[] strArray, String joinTerm) {
        StringBuilder sb = new StringBuilder();
        
        for (String str : strArray) {
            sb.append(str).append(joinTerm);
        }
        
        // test for bug
        if (sb.length() > 0 || joinTerm.length() > 0) {
            sb.delete(sb.lastIndexOf(joinTerm), sb.length());
        }
        
        return sb.toString();
    }
}
