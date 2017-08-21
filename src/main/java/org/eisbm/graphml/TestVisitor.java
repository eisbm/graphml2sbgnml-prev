package org.eisbm.graphml;

import java.util.HashMap;

public class TestVisitor implements GraphMLVisitor {
    public HashMap<String, Integer> types;
    public HashMap<String, Integer> edges;

    public TestVisitor() {
        this.edges = new HashMap<>();
        this.types = new HashMap<>();
    }

    @Override
    public void visit(GMLRoot root) {

    }

    @Override
    public void visit(GMLGraph graph) {

    }

    @Override
    public void visit(GMLNode node) {
        if(this.types.containsKey(node.getClass_())) {
            this.types.put(node.getClass_(), this.types.get(node.getClass_()) + 1);
        }
        else {
            this.types.put(node.getClass_(), 1);
        }
    }

    @Override
    public void visit(GMLEdge edge) {
        if(this.edges.containsKey(edge.getClass_())) {
            this.edges.put(edge.getClass_(), this.edges.get(edge.getClass_()) + 1);
        }
        else {
            this.edges.put(edge.getClass_(), 1);
        }
    }
}
