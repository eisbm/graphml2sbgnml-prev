package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLable {
    public Element toXmlElement(Element parent, Document root);
}
