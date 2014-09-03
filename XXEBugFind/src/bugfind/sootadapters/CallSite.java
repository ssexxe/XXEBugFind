/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bugfind.sootadapters;

import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.LineNumberTag;

/**
 * This indicates a call site for a method. It takes an Edge object.
 * @author Mikosh
 */
public class CallSite implements Comparable<CallSite> {
    /**
     * A line number tag
     */
    protected static final String LINE_NUMBER_TAG = "LineNumberTag";
    
    /**
     * the edge to be set
     */
    protected Edge edge;
    

    /**
     * Creates a CallSite when given the edge
     * @param edge the soot edge to create from
     */
    public CallSite(Edge edge) {
        this.edge = edge;        
    }

    /**
     * Returns the line location of this call site in the java file it occurs in otherwise returns -1
     * if the line location could not be obtained
     * @return the line location of this call site or -1 if it cannot be gotten
     */
    public int getLineLocation() {        
        LineNumberTag lineTag = (LineNumberTag) edge.srcUnit().getTag(LINE_NUMBER_TAG);
        if (lineTag == null) return -1;
        else return lineTag.getLineNumber();
    }
    
    /**
     * Gets the method where this call site occurs in
     * @return the method that contains this call site
     */
    public SootMethod getSourceMethod() {
        return edge.src();
    }

    /**
     * Gets the edge
     * @return 
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * Sets the edge
     * @param edge 
     */
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
            boolean isBefore = SimpleIntraDataFlowAnalysis.isBefore(this.getSourceMethod(), this.getEdge().srcStmt(), o.getEdge().srcStmt());
            return (isBefore) ? -1 : 1;//throw new RuntimeException("Both call sites occur in the same line. This is not supported yet" + this.toString() + " against " + o.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;        
        else if (this == obj) return true;
        
        else {
            if (obj.getClass() != this.getClass()) return false;
            
            return edge.equals(((CallSite)obj).edge); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public int hashCode() {
        return edge.hashCode();
    }

    
    
    
}
