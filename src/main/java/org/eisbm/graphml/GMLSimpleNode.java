package org.eisbm.graphml;

import org.sbgn.bindings.Bbox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GMLSimpleNode extends GMLNode {

    public GMLSimpleNode(String id, GMLRoot root) {
        super(id,root);
    }

    public GMLSimpleNode(Element element, GMLRoot root) {
        super(element, root);

        Element yedData = ((GMLComplexProperty) this.dataNameMap.get("nodegraphics")).value;
        System.out.println("-----------------> "+yedData);
        System.out.println(this.x+" "+this.shapeType);

        /*XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // get various attributes

        try {
            Object result = xpath.evaluate("//node[@id='"+this.id+"']//Geometry/@x", yedData);
            System.out.println("result "+Float.parseFloat((String)result));
            System.exit(1);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public String getClass_() {
        String class_ = "unknown";
        if(this.dataNameMap.containsKey("class")) {
            class_ = ((GMLSimpleProperty)this.dataNameMap.get("class")).value;
        }
        else if(this.dataNameMap.containsKey("type")) {
            String type = ((GMLSimpleProperty)this.dataNameMap.get("type")).value;
            System.out.println("TYPE: "+type);
            class_ = type;
        }
        return class_;
    }

    @Override
    public void accept(GraphMLVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean accept(HierarchicalVisitor visitor) {
        return visitor.visitLeaf(this);
    }

    @Override
    public Element toXmlElement() {
        Element nodeE = super.toXmlElement();

        for(GMLProperty prop: this.dataList) {
            // override the specific node properties
            if(prop.getName().equals("nodegraphics")) {
                continue;
            }
            nodeE.appendChild(prop.toXmlElement());
        }

        Element dataElement = XMLElementFactory.getDataElement(this.getRoot().getNodeKeyNameMap().get("nodegraphics").getId());

        Element shapeNode = XMLElementFactory.getShapeNodeElement();
        dataElement.appendChild(shapeNode);

        shapeNode.appendChild(XMLElementFactory.getGeometryElement(
                this.getHeight(), this.getWidth(), this.getX(), this.getY()));
        shapeNode.appendChild(XMLElementFactory.getFillElement());
        shapeNode.appendChild(XMLElementFactory.getBorderStyleElement());
        //shapeNode.appendChild(XMLElementFactory.getNodeLabelElement(this.getLabel()));
        shapeNode.appendChild(
                new XMLElementFactory.NodeLabelBuilder()
                        .setLabel(this.getLabel())
                        .build()
        );
        shapeNode.appendChild(XMLElementFactory.getShapeElement(this.getShapeType()));

        nodeE.appendChild(dataElement);

        for(GMLLabelNode labelNode: this.getLabelNodes()) {
            labelNode.injectIntoParentXML(nodeE);
        }


        return nodeE;
    }
}
