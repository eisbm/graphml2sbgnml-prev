package org.eisbm.graphml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GMLElement implements XMLable {
    String id;
    List<GMLProperty> dataList;
    public Map<String, GMLProperty> dataMap;
    public Map<String, GMLProperty> dataNameMap;

    public GMLElement(String id) {
        this.id = id;
        this.dataList = new ArrayList<>();
        this.dataMap = new HashMap<>();
        this.dataNameMap = new HashMap<>();
    }

    public GMLElement(Element element, GMLRoot root) {
        this.id = element.getAttribute("id");
        this.dataList = new ArrayList<>();
        this.dataMap = new HashMap<>();
        this.dataNameMap = new HashMap<>();

        NodeList nList = element.getChildNodes();
        for(int i=0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            if(n.getNodeName() == "data") {
                Element dataElement = (Element) n;
                GMLProperty prop = GMLProperty.createProperty(dataElement, root);
                this.addData(prop);
            }
        }

    }

    public String getId() {
        return id;
    }

    public static String xmlize(String s){
        return s.replaceAll(":", "_");
    }

    public String getXMLValidId() {
         return xmlize(this.getId());
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void addData(GMLProperty prop) {
        this.dataList.add(prop);
        this.dataMap.put(prop.definition.id, prop);
        this.dataNameMap.put(prop.getName(), prop);
    }

}
