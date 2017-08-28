package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

public class GMLRoot extends GMLAbstractElement implements HierarchyVisitable {
    List<GMLPropertyDefinition> keyList;
    Map<String, GMLPropertyDefinition> keyMap;
    Map<String, GMLPropertyDefinition> nodeKeyNameMap;
    Map<String, GMLPropertyDefinition> edgeKeyNameMap;
    Map<String, GMLPropertyDefinition> graphKeyNameMap;
    Map<String, GMLPropertyDefinition> graphmlKeyNameMap;

    public List<GMLGraph> graphList;
    Map<String, GMLGraph> graphMap;

    public Map<String, GMLNode> nodeMap; // stores all the nodes

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
    public static List<Map.Entry<String, String>> prefixes = Arrays.asList(
            new AbstractMap.SimpleEntry<String, String>("xmlns", "http://graphml.graphdrawing.org/xmlns"),
            new AbstractMap.SimpleEntry<String, String>("xmlns", "http://graphml.graphdrawing.org/xmlns"),
            new AbstractMap.SimpleEntry<String, String>("xmlns:java", "http://www.yworks.com/xml/yfiles-common/1.0/java"),
            new AbstractMap.SimpleEntry<String, String>("xmlns:sys", "http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0"),
            new AbstractMap.SimpleEntry<String, String>("xmlns:x", "http://www.yworks.com/xml/yfiles-common/markup/2.0"),
            new AbstractMap.SimpleEntry<String, String>("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"),
            new AbstractMap.SimpleEntry<String, String>("xmlns:y", "http://www.yworks.com/xml/graphml"),
            new AbstractMap.SimpleEntry<String, String>("xmlns:yed","http://www.yworks.com/xml/yed/3" ),
            new AbstractMap.SimpleEntry<String, String>("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd")
            );

    public GMLRoot() {
        super();
        this.keyList = new ArrayList<>();
        this.keyMap = new HashMap<>();
        this.nodeKeyNameMap = new HashMap<>();
        this.edgeKeyNameMap = new HashMap<>();
        this.graphKeyNameMap = new HashMap<>();
        this.graphmlKeyNameMap = new HashMap<>();

        this.graphList = new ArrayList<>();
        this.graphMap = new HashMap<>();

        this.nodeMap = new HashMap<>();
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
        Element graphml = XMLElementFactory.getDocument().createElement("graphml");
        // append all prefixes
        for(Map.Entry<String, String> p: prefixes){
            graphml.setAttribute(p.getKey(), p.getValue());
        }

        for(GMLPropertyDefinition propDef: this.keyList) {
            graphml.appendChild(propDef.toXmlElement());
        }

        for(GMLGraph graph: this.graphList) {
            graphml.appendChild(graph.toXmlElement());
        }

        for(GMLProperty prop: this.dataList) {
            graphml.appendChild(prop.toXmlElement());
        }

        System.out.println("rootDoc: "+rootDoc);

        rootDoc.appendChild(rootDoc.importNode(graphml, true));
        return rootDoc;
    }

    public void addPropertyDefinition(GMLPropertyDefinition def) {
        this.keyList.add(def);
        this.keyMap.put(def.getId(), def);
        if(def.getFor_().equals("node")) {
            this.nodeKeyNameMap.put(def.getName(), def);
        }
        else if(def.getFor_().equals("edge")) {
            this.edgeKeyNameMap.put(def.getName(), def);
        }
        else if(def.getFor_().equals("graph")) {
            this.graphKeyNameMap.put(def.getName(), def);
        }
        else if(def.getFor_().equals("graphml")) {
            this.graphmlKeyNameMap.put(def.getName(), def);
        }

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

    public Map<String, GMLPropertyDefinition> getNodeKeyNameMap() {
        return nodeKeyNameMap;
    }

    public Map<String, GMLPropertyDefinition> getEdgeKeyNameMap() {
        return edgeKeyNameMap;
    }

    public Map<String, GMLPropertyDefinition> getGraphKeyNameMap() {
        return graphKeyNameMap;
    }

    public Map<String, GMLPropertyDefinition> getGraphmlKeyNameMap() {
        return graphmlKeyNameMap;
    }

    public GMLPropertyDefinition getOrCreatePropertyDefinition(String name, String for_) {
        Map<String, GMLPropertyDefinition> concernedMap;
        if(for_.equals("node")) {
            concernedMap = this.getNodeKeyNameMap();
        }
        else if(for_.equals("edge")) {
            concernedMap = this.getEdgeKeyNameMap();
        }
        else if(for_.equals("graph")) {
            concernedMap = this.getGraphKeyNameMap();
        }
        else if(for_.equals("graphml")) {
            concernedMap = this.getGraphmlKeyNameMap();
        }
        else {
            throw new IllegalArgumentException(for_+" is supposed to be 'edge', 'node', 'graph' or 'graphml'");
        }

        if(concernedMap.containsKey(name)) {
            return concernedMap.get(name);
        }
        else {
            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("attr.name", name);
            attributes.put("attr.type", "string");

            // generate some random id
            String id = UUID.randomUUID().toString().substring(0, 4);
            while(this.getKeyMap().containsKey(id)) {
                id = UUID.randomUUID().toString().substring(0, 4);
            }
            // at this point we have a new available id

            GMLPropertyDefinition propDef = new GMLPropertyDefinition(id, for_, attributes);
            this.addPropertyDefinition(propDef);
            return propDef;
        }
    }

    @Override
    public void addComplexDataFor(String name, Element element) {
        this.addComplexDataFor(name, element, this);
    }

    @Override
    public void addSimpleDataFor(String name, String data) {
        this.addSimpleDataFor(name, data, this);
    }

}
