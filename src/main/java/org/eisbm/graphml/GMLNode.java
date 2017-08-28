package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public abstract class GMLNode extends GMLElement implements HierarchyVisitable {

    float x, y, width, height;

    String shapeType;
    String label;

    List<GMLLabelNode> labelNodes;

    public GMLNode(String id, GMLRoot root) {
        super(id, root);
        this.labelNodes = new ArrayList<>();
    }

    public GMLNode(Element element, GMLRoot root) {
        super(element, root);
        this.labelNodes = new ArrayList<>();

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

            this.shapeType = (String) xpath.evaluate(".//Shape[1]/@type", element, XPathConstants.STRING);

            this.label = (String) xpath.evaluate(".//NodeLabel[1]/text()[normalize-space(.) != '']", element,
                    XPathConstants.STRING);

            // possible additional labels
            // careful here, might go way too deep and get other elements with iconData inside
            NodeList nodeLabelList = (NodeList) xpath.evaluate("./data/ShapeNode/NodeLabel[@iconData]", element,
                   XPathConstants.NODESET);
            GMLComplexProperty rootResourceData = (GMLComplexProperty) root.dataNameMap.get("resources");
            Element resourcesElement = rootResourceData.getValue();
            for(int i=0; i < nodeLabelList.getLength(); i++){
                Element labelWithIconData = (Element) nodeLabelList.item(i);
                String iconData = labelWithIconData.getAttribute("iconData");
                float xdiff = Float.parseFloat(labelWithIconData.getAttribute("x"));
                float ydiff = Float.parseFloat(labelWithIconData.getAttribute("y"));

                Element shapeNode = (Element) xpath.evaluate("./Resource[@id='"+iconData+"']//ShapeNode",
                        resourcesElement, XPathConstants.NODE);
                GMLLabelNode unit = new GMLLabelNode(iconData, this, shapeNode, xdiff, ydiff);
                System.out.println("unit "+unit.getLabel()+" "+unit.getX()+" "+unit.getHeight()+" "+unit.getShapeType());

                this.labelNodes.add(unit);
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public static GMLNode createNode(Element element, GMLRoot root) {
        System.out.println("group ? "+element.getAttribute("yfiles.foldertype"));
        if(element.getAttribute("yfiles.foldertype").equals("group")) {
            return new GMLGroupNode(element, root);
        }
        else {
            return new GMLSimpleNode(element, root);
        }
    }

    public abstract String getClass_();

    @Override
    public Element toXmlElement() {
        Element nodeE = XMLElementFactory.getNodeElement(this.getId());

        /*for(GMLProperty prop: this.dataList) {
            nodeE.appendChild(prop.toXmlElement(nodeE, root));
        }*/

        return nodeE;
    }

    public List<GMLLabelNode> getLabelNodes() {
        return labelNodes;
    }

    public void setLabelNodes(List<GMLLabelNode> labelNodes) {
        this.labelNodes = labelNodes;
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

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
