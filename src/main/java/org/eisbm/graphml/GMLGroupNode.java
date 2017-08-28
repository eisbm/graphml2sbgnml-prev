package org.eisbm.graphml;

import org.sbgn.bindings.Bbox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GMLGroupNode extends GMLNode {
    GMLGraph graph;

    public GMLGroupNode(String id, GMLRoot root) {
        super(id, root);
        this.setGraph(new GMLGraph(this.getId()+"_", root));
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
    public Element toXmlElement() {
        Element nodeE = super.toXmlElement();
        // here we need to add another attribute and the graph element specific to groups
        nodeE.setAttribute("yfiles.foldertype", "group");

        for(GMLProperty prop: this.dataList) {
            // override the specific node properties
            if(prop.getName().equals("nodegraphics")) {
                continue;
            }
            nodeE.appendChild(prop.toXmlElement());
        }

        Element dataElement = XMLElementFactory.getDataElement(this.getRoot().getNodeKeyNameMap().get("nodegraphics").getId());

        Element proxy = XMLElementFactory.getProxyAutoBoundsNodeElement();
        dataElement.appendChild(proxy);

        Element realizers = XMLElementFactory.getRealizersElement();
        proxy.appendChild(realizers);

        Element groupNode1 = XMLElementFactory.getGroupNodeElement();
        realizers.appendChild(groupNode1);

        groupNode1.appendChild(XMLElementFactory.getGeometryElement(this.getHeight(), this.getWidth(),
                this.getX(), this.getY()));
        groupNode1.appendChild(XMLElementFactory.getFillElement());
        groupNode1.appendChild(XMLElementFactory.getBorderStyleElement());
        groupNode1.appendChild(XMLElementFactory.getNodeLabelElement(this.getLabel()));
        groupNode1.appendChild(XMLElementFactory.getShapeElement(this.getShapeType()));
        // also add state insets and so on...

        nodeE.appendChild(dataElement);

        nodeE.appendChild(this.getGraph().toXmlElement());

        return nodeE;
    }
}
