package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GMLGroupNode extends GMLNode {
    GMLGraph graph;

    public GMLGroupNode(String id) {
        super(id);
        this.setGraph(new GMLGraph(this.getId()+"_"));
    }

    public GMLGroupNode(Element element, GMLRoot root) {
        super(element, root);

        NodeList graphNList = element.getElementsByTagName("graph");
        Node graphNode = graphNList.item(0); // there can be 1 or 0 graph
        if(graphNList.getLength() > 0 && graphNode.getParentNode() == element) { // keep only first level elements
            Element graphElement = (Element) graphNode;
            GMLGraph gmlGraph = new GMLGraph(graphElement, root);
            this.graph = gmlGraph;
        }
    }

    @Override
    public String getClass_() {
        return "compartment";
    }

    @Override
    public void accept(GraphMLVisitor visitor) {
        visitor.visit(this);
        this.graph.accept(visitor);
    }

    @Override
    public boolean accept(HierarchicalVisitor visitor) {
        if(visitor.visitEnter(this)) {
            this.graph.accept(visitor);
            return visitor.visitExit(this);
        }
        return false;
    }

    public GMLGraph getGraph() {
        return graph;
    }

    public void setGraph(GMLGraph graph) {
        this.graph = graph;
    }

    @Override
    public Element toXmlElement(Element parent, Document root) {
        Element nodeE = super.toXmlElement(parent, root);
        // here we need to add another attribute and the graph element specific to groups
        nodeE.setAttribute("yfiles.foldertype", "group");

        nodeE.appendChild(this.getGraph().toXmlElement(nodeE, root));

        return nodeE;
    }
}
