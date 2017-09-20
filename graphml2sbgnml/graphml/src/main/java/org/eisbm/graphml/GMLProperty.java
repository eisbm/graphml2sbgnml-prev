package org.eisbm.graphml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class GMLProperty implements XMLable{
    GMLPropertyDefinition definition;

    Object value;

    public GMLProperty(Element dataElement, GMLRoot root) {
        String id = dataElement.getAttribute("key");
        this.definition = (GMLPropertyDefinition) root.keyMap.get(id);
    }

    public static GMLProperty createProperty(Element dataElement, GMLRoot root) {

        boolean hasChild = false;
        NodeList children = dataElement.getChildNodes();
        for (int i = 0;i < children.getLength();i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
                hasChild = true;
        }

        if(hasChild) {
            return new GMLComplexProperty(dataElement, root);
        }
        else {
            return new GMLSimpleProperty(dataElement, root);
        }
    }

    public String getName() {
        return this.getDefinition().getName();
    }

    public GMLPropertyDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(GMLPropertyDefinition definition) {
        this.definition = definition;
    }

    public abstract Object getValue();

    public abstract void setValue(Object value);
}
