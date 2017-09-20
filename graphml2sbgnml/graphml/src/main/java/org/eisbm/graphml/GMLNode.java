package org.eisbm.graphml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

public abstract class GMLNode extends GMLElement implements HierarchyVisitable, GMLNodeFeature {

    private GMLNodeFeature nodeFeature;

    private List<GMLLabelNode> labelNodes;

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
            Element shapeNode = (Element) xpath.evaluate("./data/ShapeNode", element, XPathConstants.NODE);
            Element genericNode = (Element) xpath.evaluate("./data/GenericNode", element, XPathConstants.NODE);
            if (genericNode != null) {
                this.nodeFeature = new GMLGenericNodeFeature(genericNode);
            }
            else if(shapeNode != null) {
                this.nodeFeature = new GMLShapeNodeFeature(shapeNode);
            }
            else {
                throw new IllegalStateException("Expected GenericNode or ShapeNode child element, but none found. For node: "+this.getId());
            }

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

                shapeNode = (Element) xpath.evaluate("./Resource[@id='"+iconData+"']//ShapeNode",
                        resourcesElement, XPathConstants.NODE);
                if(shapeNode == null) { // if sbgn palette, GenericNode instead of ShapeNode
                    shapeNode = (Element) xpath.evaluate("./Resource[@id='"+iconData+"']//GenericNode",
                            resourcesElement, XPathConstants.NODE);
                }
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
        return this.nodeFeature.getX();
    }

    public void setX(float x) {
        this.nodeFeature.setX(x);
    }

    public float getY() {
        return this.nodeFeature.getY();
    }

    public void setY(float y) {
        this.nodeFeature.setY(y);
    }

    public float getWidth() {
        return this.nodeFeature.getWidth();
    }

    public void setWidth(float width) {
        this.nodeFeature.setWidth(width);
    }

    public float getHeight() {
        return this.nodeFeature.getHeight();
    }

    public void setHeight(float height) {
        this.nodeFeature.setHeight(height);
    }

    public String getShapeType() {
        return this.nodeFeature.getShapeType();
    }

    public void setShapeType(String shapeType) {
        this.nodeFeature.setShapeType(shapeType);
    }

    public String getLabel() {
        return this.nodeFeature.getLabel();
    }

    public void setLabel(String label) {
        this.nodeFeature.setLabel(label);
    }

    public boolean isSBGNPalette() {
        return this.nodeFeature.isSBGNPalette();
    }

}
