package org.eisbm.graphml;

import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class GMLCoreNodeFeature {

    private float x, y, width, height;

    private String label;

    public GMLCoreNodeFeature(Element element) {

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // get various attributes

        try {
            // always take the first Geometry element. GroupNodes have 2, but the 2nd one isn't relevant
            Element geometryElement = (Element) xpath.evaluate(".//Geometry[1]", element, XPathConstants.NODE);

            this.x = Float.parseFloat(geometryElement.getAttribute("x"));
            this.y = Float.parseFloat(geometryElement.getAttribute("y"));
            this.width = Float.parseFloat(geometryElement.getAttribute("width"));
            this.height = Float.parseFloat(geometryElement.getAttribute("height"));

            this.label = (String) xpath.evaluate(".//NodeLabel[1]/text()[normalize-space(.) != '']", element,
                    XPathConstants.STRING);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
