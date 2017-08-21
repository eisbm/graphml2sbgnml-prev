package org.eisbm.graphml;

public interface GraphMLVisitor {
    void visit(GMLRoot root);
    void visit(GMLGraph graph);
    void visit(GMLNode node);
    void visit(GMLEdge edge);
}
