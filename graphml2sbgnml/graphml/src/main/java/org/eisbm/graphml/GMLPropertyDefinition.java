package org.eisbm.graphml;

import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class GMLPropertyDefinition implements XMLable{
    String id; // required
    String for_; // required
    Map<String, String> attributes;

    public GMLPropertyDefinition() {}

    public GMLPropertyDefinition(String id, String for_, Map<String, String> attributes) {
        this.id = id;
        this.for_ = for_;
        this.attributes = attributes;
    }

    public GMLPropertyDefinition(Element keyXMLElement){
        this.attributes = new HashMap<>();
        for(int i=0; i < keyXMLElement.getAttributes().getLength(); i++) {
            String attrName = keyXMLElement.getAttributes().item(i).getNodeName();
            String attrValue = keyXMLElement.getAttributes().item(i).getNodeValue();
            switch (attrName){
                case "id":
                    this.id = attrValue;
                    break;
                case "for":
                    this.for_ = attrValue;
                    break;
                default:
                    this.attributes.put(attrName, attrValue);
            }
        }
    }

    public String getAttributeValue(String attr) {
        return this.attributes.get(attr);
    }

    public boolean isSimpleType() {
        return this.getAttributeValue("attr.name") != null &&
                this.getAttributeValue("attr.type") != null;
    }

    public boolean isYFileType() {
        return this.getAttributeValue("yfiles.type") != null;
    }

    @Override
    public Element toXmlElement() {
        Element keyE = XMLElementFactory.getKeyElement(this.getId(), this.getFor_());

        for(String s: this.getAttributes().keySet()){
            keyE.setAttribute(s, this.getAttributes().get(s));
        }

        return keyE;
    }

    public String getName() {
        if(this.attributes.containsKey("attr.name")){
            return this.getAttributeValue("attr.name");
        }
        else if(this.attributes.containsKey("yfiles.type")){
            return this.getAttributeValue("yfiles.type");
        }
        else {
            return this.id;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFor_() {
        return for_;
    }

    public void setFor_(String for_) {
        this.for_ = for_;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
