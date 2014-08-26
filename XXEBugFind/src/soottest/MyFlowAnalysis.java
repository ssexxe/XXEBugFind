/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package soottest;

import java.util.List;
import java.util.Map;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowAnalysis;

/**
 *
 * @author Mikosh
 */
public class MyFlowAnalysis extends FlowAnalysis<Unit, Map<Unit, List>>{

    public MyFlowAnalysis(DirectedGraph<Unit> graph) {
        super(graph);
    }

    @Override
    protected void flowThrough(Map<Unit, List> in, Unit d, Map<Unit, List> out) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<Unit, List> newInitialFlow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Map<Unit, List> entryInitialFlow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean isForward() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void merge(Map<Unit, List> in1, Map<Unit, List> in2, Map<Unit, List> out) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void copy(Map<Unit, List> source, Map<Unit, List> dest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doAnalysis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
