package org.eisbm.graphml;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GMLAbstractElement is the top level class that all GML elements inherit from.
 * It is responsible for the shared behavior related to data elements (or properties).
 * Data can be attached to: graphml, graph, node, edge.
 */
public abstract class GMLAbstractElement {
    List<GMLProperty> dataList;
    Map<String, GMLProperty> dataMap;
    Map<String, GMLProperty> dataNameMap;

    /**
     * Default constructor
     */
    public GMLAbstractElement() {
        this.dataList = new ArrayList<>();
        this.dataMap = new HashMap<>();
        this.dataNameMap = new HashMap<>();
    }

    /**
     * Add a property by keeping indexes consistent.
     * @param prop a data element
     */
    protected void addData(GMLProperty prop) {
        this.dataList.add(prop);
        this.dataMap.put(prop.definition.id, prop);
        this.dataNameMap.put(prop.getName(), prop);
    }

    /**
     * Return the appropriate 'for' attribute for {@literal <key>} element
     * by checking the class of the calling object.
     * @return "node", "edge", "graph" or "graphml"
     */
    private String inferForAttribute() {
        String for_ = "";
        if(GMLNode.class.isAssignableFrom(this.getClass())) {
            for_ = "node";
        }
        else if(GMLEdge.class.isAssignableFrom(this.getClass())) {
            for_ = "edge";
        }
        else if(GMLGraph.class.isAssignableFrom(this.getClass())) {
            for_ = "graph";
        }
        else if(GMLRoot.class.isAssignableFrom(this.getClass())) {
            for_ = "graphml";
        }
        else {
            throw new IllegalArgumentException("The class: " + this.getClass()+" cannot be used to infer the 'for' " +
                    "attribute for <key> elements.");
        }
        return for_;
    }

    protected void addComplexDataFor(String name, Element element, GMLRoot root) {
        String for_ = this.inferForAttribute();
        String dataId = root.getOrCreatePropertyDefinition(name, for_).getId();
        Element dataElement = XMLElementFactory.getDataElement(dataId);
        dataElement.appendChild(element);
        GMLComplexProperty prop = new GMLComplexProperty(dataElement, root);
        this.addData(prop);
    }

    /**
     * Given the name associated to a property definition (a key element),
     * add an xml element as data for this property.
     * @param name the name associated with the property definition.
     *             Can be from the attr.name or yfiles.type attribute.
     * @param element {@literal <data>} xml element
     */
    public abstract void addComplexDataFor(String name, Element element);

    protected void addSimpleDataFor(String name, String data, GMLRoot root) {
        String for_ = this.inferForAttribute();
        String dataId = root.getOrCreatePropertyDefinition(name, for_).getId();
        Element dataElement = XMLElementFactory.getDataElement(dataId);
        dataElement.appendChild(XMLElementFactory.getCDATA(data));
        GMLSimpleProperty prop = new GMLSimpleProperty(dataElement, root);
        this.addData(prop);
    }

    /**
     * Given the name associated to a property definition (a key element),
     * add a string as data for this property.
     * @param name the name associated with the property definition.
     *             Can be from the attr.name or yfiles.type attribute.
     * @param data the string to add as content of the {@literal <data>} element
     */
    public abstract void addSimpleDataFor(String name, String data);

    public List<GMLProperty> getDataList() {
        return dataList;
    }

    public void setDataList(List<GMLProperty> dataList) {
        this.dataList = dataList;
    }

    public Map<String, GMLProperty> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, GMLProperty> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<String, GMLProperty> getDataNameMap() {
        return dataNameMap;
    }

    public void setDataNameMap(Map<String, GMLProperty> dataNameMap) {
        this.dataNameMap = dataNameMap;
    }
}
