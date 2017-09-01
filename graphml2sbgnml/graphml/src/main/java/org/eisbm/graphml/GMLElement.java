package org.eisbm.graphml;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GMLElement extends GMLAbstractElement implements XMLable {
    String id;
    GMLRoot root;

    public GMLElement(String id, GMLRoot root) {
        super();
        this.id = id;
        this.root = root;
    }

    public GMLElement(Element element, GMLRoot root) {
        super();
        this.id = element.getAttribute("id");
        this.root = root;

        NodeList nList = element.getChildNodes();
        for(int i=0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            if(n.getNodeName().equals("data")) {
                Element dataElement = (Element) n;
                GMLProperty prop = GMLProperty.createProperty(dataElement, root);
                this.addData(prop);
            }
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GMLRoot getRoot() {
        return root;
    }

    public void setRoot(GMLRoot root) {
        this.root = root;
    }


    public static String xmlize(String s){
        return s.replaceAll(":", "_");
    }

    public String getXMLValidId() {
         return xmlize(this.getId());
    }



    @Override
    public void addComplexDataFor(String name, Element element) {
        this.addComplexDataFor(name, element, this.root);
    }

    @Override
    public void addSimpleDataFor(String name, String data) {
        this.addSimpleDataFor(name, data, this.root);
    }

}
