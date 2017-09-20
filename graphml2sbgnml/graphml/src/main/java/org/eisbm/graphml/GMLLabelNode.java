package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class GMLLabelNode implements GMLNodeFeature {
    private GMLNode parent;
    private String id;

    private GMLNodeFeature nodeFeature;


    public GMLLabelNode(String id, GMLNode parentNode) {
        this.parent = parentNode;
        this.id = id;
    }

    public GMLLabelNode(String id, GMLNode parentNode, Element element, float xdiff, float ydiff) {
        this(id, parentNode);


        // duplicated code from GMLNode constructor
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // get various attributes

        //try {
            if (element.getTagName().equals("y:GenericNode")) {
                this.nodeFeature = new GMLGenericNodeFeature(element);
            }
            else if(element.getTagName().equals("y:ShapeNode")) {
                this.nodeFeature = new GMLShapeNodeFeature(element);
            }
            else {
                throw new IllegalStateException("Expected GenericNode or ShapeNode element, but none found. For node: "+this.getId());
            }

            // where should the xdiff be taken from ? this nodelabel, or the one in the parent ?
            /*this.x = parentNode.getX() + Float.parseFloat((String) xpath.evaluate(".//NodeLabel[1]/@x",
                    shapeNodeElement, XPathConstants.STRING));
            this.y = parentNode.getY() + Float.parseFloat((String) xpath.evaluate(".//NodeLabel[1]/@y",
                    shapeNodeElement, XPathConstants.STRING));*/
            this.nodeFeature.setX(parentNode.getX() + xdiff);
            this.nodeFeature.setY(parentNode.getY() + ydiff);

        /*} catch (XPathExpressionException e) {
            e.printStackTrace();
        }*/


        // adjust the coordinates using x and ydiff that are from the nodeLabel element
        // contained in the parent node
        //this.x = parentNode.getX() + xdiff;
        //this.y = parentNode.getY() + ydiff;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GMLNode getParent() {
        return parent;
    }

    public void setParent(GMLNode parent) {
        this.parent = parent;
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

    @Override
    public boolean isSBGNPalette() {
        return this.nodeFeature.isSBGNPalette();
    }

    public void injectIntoParentXML(Element parentXML) {

        // inject root data part aka Resource

        Element rootResourcesData = (Element) this.getParent().getRoot().getDataNameMap().get("resources").getValue();
        System.out.println(rootResourcesData+" "+rootResourcesData.getChildNodes().getLength());

        Element resource = XMLElementFactory.getResourceElement(this.getId());
        rootResourcesData.appendChild(resource);

        Element nodeRealizer = XMLElementFactory.getNodeRealizerIcon();
        resource.appendChild(nodeRealizer);

        Element shapeNode = XMLElementFactory.getShapeNodeElement();
        nodeRealizer.appendChild(shapeNode);

        shapeNode.appendChild(XMLElementFactory.getGeometryElement(
                this.getHeight(), this.getWidth(), 0f, 0f));
        shapeNode.appendChild(XMLElementFactory.getFillElement());
        shapeNode.appendChild(XMLElementFactory.getBorderStyleElement());
        Element nodeLabel1 = new XMLElementFactory.NodeLabelBuilder()
                .setLabel(this.getLabel())
                .setCoordinates(
                        this.getX() - this.getParent().getX(),
                        this.getY() - this.getParent().getY())
                .setDimensions(this.getHeight(), this.getWidth())
                .hasSmartModel()
                .setModelName("custom")
                .setIconTextGap(4)
                .build();

        shapeNode.appendChild(nodeLabel1);

        shapeNode.appendChild(XMLElementFactory.getShapeElement(this.getShapeType()));

        // inject additional nodeLabel into parent's nodegraphics data

        Element parentElement;
        if(this.getParent() instanceof GMLSimpleNode) {
            parentElement = (Element) parentXML.getElementsByTagName("y:ShapeNode").item(0);
        }
        else {
            parentElement = (Element) parentXML.getElementsByTagName("y:GroupNode").item(0);
        }

        System.out.println("UNIT COORD COMPUTATION "+(this.getY() + (this.getHeight() / 2)));
        System.out.println("UNIT COORD COMPUTATION "+(this.getParent().getY() + (this.getParent().getHeight() / 2)));
        System.out.println("UNIT COORD COMPUTATION "+( this.parent.getHeight()));

        float thisMiddleX = this.getX() + (this.getWidth() / 2);
        float thisMiddleY = this.getY() + (this.getHeight() / 2);
        float parentMiddleX = this.getParent().getX() + (this.getParent().getWidth() / 2);
        float parentMiddleY = this.getParent().getY() + (this.getParent().getHeight() / 2);

        System.out.println("UNIT COORD COMPUTATION "+ thisMiddleY + " "+parentMiddleY);
        System.out.println((thisMiddleY - parentMiddleY) / this.parent.getHeight());

        Element nodeLabel = new XMLElementFactory.NodeLabelBuilder()
                .setIconData(this.getId())
                .setCoordinates(this.getX() - this.getParent().getX(), this.getY() - this.getParent().getY())
                .setDimensions(this.getHeight(), this.getWidth())
                .hasSmartModel()
                .setNodeRatio(
                        (thisMiddleX - parentMiddleX) / this.getParent().getWidth(),
                        (thisMiddleY - parentMiddleY) / this.getParent().getHeight()
                )
                .hasText(false)
                .setModelName("custom")
                .setIconTextGap(0)
                .build();

        parentElement.appendChild(nodeLabel);
    }
}
