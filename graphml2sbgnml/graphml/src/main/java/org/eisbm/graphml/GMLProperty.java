package org.eisbm.graphml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class GMLProperty implements XMLable{
    GMLPropertyDefinition definition;

    Object value;

    public GMLProperty(Element dataElement, GMLRoot root) {
        String id = dataElement.getAttribute("key");
        this.definition = (GMLPropertyDefinition) root.keyMap.get(id);
    }

    public static GMLProperty createProperty(Element dataElement, GMLRoot root) {
        NodeList groupList = dataElement.getElementsByTagName("y:ProxyAutoBoundsNode");
        NodeList shapenodeList = dataElement.getElementsByTagName("y:ShapeNode");
        NodeList shapeedgeList = dataElement.getElementsByTagName("y:PolyLineEdge");
        NodeList resourceList = dataElement.getElementsByTagName("y:Resources");
        if(groupList.getLength() > 0 || shapenodeList.getLength() > 0
                || shapeedgeList.getLength() > 0 || resourceList.getLength() > 0) {
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
