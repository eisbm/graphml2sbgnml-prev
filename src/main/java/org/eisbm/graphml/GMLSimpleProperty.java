package org.eisbm.graphml;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GMLSimpleProperty extends GMLProperty {
    String value;

    public GMLSimpleProperty(Element dataElement, GMLRoot root) {
        super(dataElement, root);

        Node child = dataElement.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            this.value = cd.getData();
        }
        else {
            this.value = dataElement.getNodeValue();
        }
        System.out.println("data: "+ this.definition.id+" "+this.value+" "+this.definition.getAttributeValue("attr.name"));
    }

    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public Element toXmlElement(Element parent, Document root) {
        Element data = root.createElement("data");
        data.setAttribute("key", this.definition.id);
        if(this.getValue() != null) {
            Node cdataContent = root.createCDATASection(this.getValue());
            System.out.println("cdata " + cdataContent + " " + cdataContent.getNodeValue());
            data.appendChild(cdataContent);
        }

        return data;
    }
}
