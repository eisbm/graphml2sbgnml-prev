package org.eisbm.graphml;

public interface HierarchicalVisitor {
    boolean visitEnter(GMLRoot root);
    boolean visitExit(GMLRoot root);

    boolean visitEnter(GMLGraph graph);
    boolean visitExit(GMLGraph graph);

    boolean visitEnter(GMLNode node);
    boolean visitExit(GMLNode node);
    boolean visitLeaf(GMLNode node);

    boolean visitLeaf(GMLEdge edge);
}
