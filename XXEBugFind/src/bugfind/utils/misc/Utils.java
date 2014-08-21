/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.utils.misc;

/**
 *
 * @author Mikosh
 */
public class Utils {
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
