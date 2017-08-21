package org.eisbm.graphml;

import com.sun.webkit.dom.ElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GMLGraph extends GMLElement implements Visitable, HierarchyVisitable, XMLable{
    List<GMLNode> nodeList;
    Map<String, GMLNode> nodeMap;
    List<GMLEdge> edgeList;
    Map<String, GMLEdge> edgeMap;

    public GMLGraph(String id) {
        super(id);
        this.nodeList = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        this.edgeList = new ArrayList<>();
        this.edgeMap = new HashMap<>();
    }

    public GMLGraph(Element graphElement, GMLRoot root) {
        super(graphElement, root);
        this.nodeList = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        this.edgeList = new ArrayList<>();
        this.edgeMap = new HashMap<>();

        NodeList nList = graphElement.getChildNodes();
        for(int i=0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            if(n.getNodeName().equals("node")) {
                Element nodeElement = (Element) n;
                GMLNode gNode = GMLNode.createNode(nodeElement, root);
                this.addNode(gNode);
                root.nodeMap.put(gNode.id, gNode);
            }
            else if(n.getNodeName().equals("edge")) {
                Element edgeElement = (Element) n;
                GMLEdge gEdge = new GMLEdge(edgeElement, root);
                this.addEdge(gEdge);
            }
        }

        System.out.println("after parse graph "+this.id);
    }

    @Override
    public void accept(GraphMLVisitor visitor) {
        visitor.visit(this);
        for(GMLNode node: this.nodeList) {
            node.accept(visitor);
        }
        for(GMLEdge edge: this.edgeList) {
            edge.accept(visitor);
        }
    }

    @Override
    public boolean accept(HierarchicalVisitor visitor) {
        if(visitor.visitEnter(this)) {
            for(GMLNode node: this.nodeList) {
                if(node.accept(visitor)){
                    break;
                }
            }
            for(GMLEdge edge: this.edgeList) {
                if(edge.accept(visitor)){
                    break;
                }
            }
        }
        return visitor.visitExit(this);
    }

    public List<GMLNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<GMLNode> nodeList) {
        this.nodeList = nodeList;
    }

    public Map<String, GMLNode> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<String, GMLNode> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public List<GMLEdge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<GMLEdge> edgeList) {
        this.edgeList = edgeList;
    }

    public Map<String, GMLEdge> getEdgeMap() {
        return edgeMap;
    }

    public void setEdgeMap(Map<String, GMLEdge> edgeMap) {
        this.edgeMap = edgeMap;
    }

    @Override
    public Element toXmlElement(Element parent, Document root) {
        Element graphE = root.createElement("graph");
        graphE.setAttribute("id", this.getId());
        graphE.setAttribute("edgedefault", "directed");

        for(GMLProperty prop: this.dataList) {
            graphE.appendChild(prop.toXmlElement(graphE, root));
        }

        for(GMLNode node: this.nodeList) {
            graphE.appendChild(node.toXmlElement(graphE, root));
        }

        for(GMLEdge edge: this.edgeList) {
            graphE.appendChild(edge.toXmlElement(graphE, root));
        }

        return graphE;
    }

    public void addNode(GMLNode node) {
        this.nodeList.add(node);
        this.nodeMap.put(node.id, node);
    }

    public void addEdge(GMLEdge edge) {
        this.edgeList.add(edge);
        this.edgeMap.put(edge.id, edge);
    }
}
