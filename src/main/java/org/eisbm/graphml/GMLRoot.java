package org.eisbm.graphml;

import com.sun.tools.javac.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

public class GMLRoot implements Visitable, HierarchyVisitable {
    List<GMLPropertyDefinition> keyList;
    Map<String, GMLPropertyDefinition> keyMap;

    public List<GMLGraph> graphList;
    Map<String, GMLGraph> graphMap;

    public Map<String, GMLNode> nodeMap; // stores all the nodes

    List<GMLProperty> dataList;
    public Map<String, GMLProperty> dataMap;
    public Map<String, GMLProperty> dataNameMap;

    // list of prefixes to define in the graphml root element
    /*
    xmlns="http://graphml.graphdrawing.org/xmlns"
    xmlns:java="http://www.yworks.com/xml/yfiles-common/1.0/java"
    xmlns:sys="http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0"
    xmlns:x="http://www.yworks.com/xml/yfiles-common/markup/2.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:y="http://www.yworks.com/xml/graphml"
    xmlns:yed="http://www.yworks.com/xml/yed/3"
    xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd"
    */
    public static List<Pair<String, String>> prefixes = Arrays.asList(
            Pair.of("xmlns", "http://graphml.graphdrawing.org/xmlns"),
            Pair.of("xmlns:java", "http://www.yworks.com/xml/yfiles-common/1.0/java"),
            Pair.of("xmlns:sys", "http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0"),
            Pair.of("xmlns:x", "http://www.yworks.com/xml/yfiles-common/markup/2.0"),
            Pair.of("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"),
            Pair.of("xmlns:y", "http://www.yworks.com/xml/graphml"),
            Pair.of("xmlns:yed","http://www.yworks.com/xml/yed/3" ),
            Pair.of("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd")
            );

    public GMLRoot() {
        this.keyList = new ArrayList<>();
        this.keyMap = new HashMap<>();

        this.graphList = new ArrayList<>();
        this.graphMap = new HashMap<>();

        this.nodeMap = new HashMap<>();

        this.dataList = new ArrayList<>();
        this.dataMap = new HashMap<>();
        this.dataNameMap = new HashMap<>();
    }

    public GMLRoot(Element rootElement) {
        this();

        NodeList keyNList = rootElement.getElementsByTagName("key");
        for (int i = 0; i < keyNList.getLength(); i++) {
            Node keyNode = keyNList.item(i);
            Element keyElement = (Element) keyNode;
            GMLPropertyDefinition pdef = new GMLPropertyDefinition(keyElement);
            this.addPropertyDefinition(pdef);
        }
        System.out.println(this.keyList);
        System.out.println(this.keyMap);

        //////// data

        NodeList nList = rootElement.getChildNodes();
        for(int i=0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            if(n.getNodeName().equals("data")) {
                Element dataElement = (Element) n;
                GMLProperty prop = GMLProperty.createProperty(dataElement, this);
                this.addProperty(prop);
            }
        }

        NodeList graphNList = rootElement.getElementsByTagName("graph");
        for (int i = 0; i < graphNList.getLength(); i++) {
            Node graphNode = graphNList.item(i);
            if(graphNode.getParentNode() != rootElement) { // keep only first level elements
                continue;
            }
            Element graphElement = (Element) graphNode;
            GMLGraph gmlGraph = new GMLGraph(graphElement, this);
            this.addGraph(gmlGraph);
        }
        System.out.println(this.graphList);
        System.out.println(this.graphMap);
        System.out.println(this.nodeMap);


    }

    @Override
    public void accept(GraphMLVisitor visitor) {
        visitor.visit(this);
        for(GMLGraph graph: this.graphList) {
            graph.accept(visitor);
        }
    }

    @Override
    public boolean accept(HierarchicalVisitor visitor) {
        if(visitor.visitEnter(this)) {
            for(GMLGraph graph: this.graphList) {
                if(graph.accept(visitor)){
                    break;
                }
            }
        }
        return visitor.visitExit(this);
    }

    public Document toXmlDoc() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document rootDoc = db.newDocument();
        Element graphml = rootDoc.createElement("graphml");
        // append all prefixes
        for(Pair<String, String> p: prefixes){
            graphml.setAttribute(p.fst, p.snd);
        }

        for(GMLPropertyDefinition propDef: this.keyList) {
            graphml.appendChild(propDef.toXmlElement(graphml, rootDoc));
        }

        for(GMLGraph graph: this.graphList) {
            graphml.appendChild(graph.toXmlElement(graphml, rootDoc));
        }

        for(GMLProperty prop: this.dataList) {
            graphml.appendChild(prop.toXmlElement(graphml, rootDoc));
        }

        System.out.println("rootDoc: "+rootDoc);

        rootDoc.appendChild(graphml);
        return rootDoc;
    }

    public void addPropertyDefinition(GMLPropertyDefinition def) {
        this.keyList.add(def);
        this.keyMap.put(def.getId(), def);
    }

    public void addGraph(GMLGraph graph) {
        this.graphList.add(graph);
        this.graphMap.put(graph.id, graph);
    }

    public void addProperty(GMLProperty prop) {
        this.dataList.add(prop);
        this.dataMap.put(prop.definition.id, prop);
        this.dataNameMap.put(prop.getName(), prop);
    }

    public Map<String, GMLPropertyDefinition> getKeyMap() {
        return keyMap;
    }
}
