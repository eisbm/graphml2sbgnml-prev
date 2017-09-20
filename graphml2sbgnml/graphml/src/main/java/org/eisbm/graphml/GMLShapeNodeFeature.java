package org.eisbm.graphml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;

public class GMLShapeNodeFeature implements GMLNodeFeature {

    private GMLCoreNodeFeature coreFeature;
    private String shapeType;

    public GMLShapeNodeFeature(Element element) {
        this.coreFeature = new GMLCoreNodeFeature(element);

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // get various attributes
        try {
            this.shapeType = (String) xpath.evaluate(".//Shape[1]/@type", element, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public float getX() {
        return this.coreFeature.getX();
    }

    @Override
    public void setX(float x) {
        this.coreFeature.setX(x);
    }

    @Override
    public float getY() {
        return this.coreFeature.getY();
    }

    @Override
    public void setY(float y) {
        this.coreFeature.setY(y);
    }

    @Override
    public float getWidth() {
        return this.coreFeature.getWidth();
    }

    @Override
    public void setWidth(float width) {
        this.coreFeature.setWidth(width);
    }

    @Override
    public float getHeight() {
        return this.coreFeature.getHeight();
    }

    @Override
    public void setHeight(float height) {
        this.coreFeature.setHeight(height);
    }

    @Override
    public String getShapeType() {
        return this.shapeType;
    }

    @Override
    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    @Override
    public String getLabel() {
        return this.coreFeature.getLabel();
    }

    @Override
    public void setLabel(String label) {
        this.coreFeature.setLabel(label);
    }

    @Override
    public boolean isSBGNPalette() {
        return false;
    }
}
