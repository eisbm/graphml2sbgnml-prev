package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GMLComplexProperty extends GMLProperty {
    Element value;

    public GMLComplexProperty(Element dataElement, GMLRoot root) {
        super(dataElement, root);

        NodeList nList = dataElement.getChildNodes();
        for(int i=0; i < nList.getLength(); i++) {
            Node n = nList.item(i);
            System.out.println(n.getNodeName());
            if(n.getNodeName().equals("y:ProxyAutoBoundsNode") ||
                    n.getNodeName().equals("y:ShapeNode") ||
                    n.getNodeName().equals("y:PolyLineEdge") ||
                    n.getNodeName().equals("y:Resources")) {
                this.value = (Element) n;
                break;
            }
        }
    }

    public Element getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Element) value;
    }

    @Override
    public Element toXmlElement() {
        Element data = XMLElementFactory.getDataElement(this.getDefinition().getId());
        // here just appending an element as is gives an error, wrong document owner.
        // We need to import the node.
        data.appendChild(this.getValue());
        return data;
    }
}
