package org.eisbm.graphml;

import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;

public class GMLGenericNodeFeature implements GMLNodeFeature {


    private GMLCoreNodeFeature coreFeature;
    private String shapeType;

    public GMLGenericNodeFeature(Element element) {
        this.coreFeature = new GMLCoreNodeFeature(element);

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // get various attributes
        try {
            this.shapeType = (String) xpath.evaluate(".//@configuration", element, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }


    public static HashMap<String, String> paletteClassToSBGNClassMap;
    static {
        paletteClassToSBGNClassMap = new HashMap<>();
        paletteClassToSBGNClassMap.put("UnspecifiedEntity", "unspecified entity");
        paletteClassToSBGNClassMap.put("SimpleChemical", "simple chemical");
        paletteClassToSBGNClassMap.put("Macromolecule", "macromolecule");
        paletteClassToSBGNClassMap.put("NucleicAcidFeature", "nucleic acid feature");
        //paletteClassToSBGNClassMap.put("UnspecifiedEntity", "");
    }

    @Override
    public boolean isSBGNPalette() {
        return true;
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
        String[] tmp = this.shapeType.split("\\.");
        String lastPart = tmp[tmp.length - 1];
        System.out.println("convert palette to class "+lastPart+" "+paletteClassToSBGNClassMap.get(lastPart));
        return paletteClassToSBGNClassMap.get(lastPart);
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
}
