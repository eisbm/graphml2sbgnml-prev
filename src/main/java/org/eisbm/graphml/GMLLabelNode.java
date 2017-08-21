package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GMLLabelNode extends GMLNode {

    public GMLLabelNode(Element resourceElement, GMLNode parentNode, GMLRoot root, float xdiff, float ydiff) {
        super(resourceElement, root);

        // correction for x and y wrongly parsed from the Node constructor
        this.x = parentNode.getX() + xdiff;
        this.y = parentNode.getY() + ydiff;
    }

    @Override
    public void accept(GraphMLVisitor visitor) {

    }

    @Override
    public boolean accept(HierarchicalVisitor visitor) {
        return false;
    }

    @Override
    public String getClass_() {
        return null;
    }
}
