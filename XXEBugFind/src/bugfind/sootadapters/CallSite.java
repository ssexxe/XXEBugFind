/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import javax.xml.parsers.DocumentBuilder;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.LineNumberTag;

/**
 *
 * @author Mikosh
 */
public class CallSite implements Comparable<CallSite> {
    protected static final String LINE_NUMBER_TAG = "LineNumberTag";
    protected Edge edge;
    

    public CallSite(Edge edge) {
        this.edge = edge;        
    }

    public int getLineLocation() {DocumentBuilder db;
        //edge.tgt().getDeclaringClass().getActiveBody().getUnitBoxes(true).get().getUnit().getTags()
        LineNumberTag lineTag = (LineNumberTag) edge.srcUnit().getTag(LINE_NUMBER_TAG);
        return lineTag.getLineNumber();
    }
    
    public SootMethod getSourceMethod() {
        return (SootMethod) edge.getSrc();
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }      

    @Override
    public String toString() {
        return edge.toString() + " at line: " + getLineLocation(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(CallSite o) {
        if (!this.getSourceMethod().getSignature().equals(o.getSourceMethod().getSignature())) {
            throw new RuntimeException("Cannot perform comparison because " + this + " AND " + o + " do not have the same "
                    + "source method. (ie they are not called within the same method)");            
        }
        int line1 = this.getLineLocation();
        int line2 = o.getLineLocation();
        if (line1 < line2) {
            return -1;
        }
        else if (line2 < line1) {
            return 1;
        }
        else {// ie they are on the same line. now determine which one is called first
            throw new RuntimeException("Both call sites occur in the same line. This is not supported yet");
        }
    }
    
    
}
