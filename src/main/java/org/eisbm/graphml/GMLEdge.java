package org.eisbm.graphml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GMLEdge extends GMLElement implements Visitable, HierarchyVisitable {
    String source;
    String target;
    List<Point2D.Float> points;
    public Point2D.Float startDiff;
    public Point2D.Float endDiff;
    String sourceArrowShape;
    String targetArrowShape;

    public GMLEdge(String id) {
        super(id);
    }

    public GMLEdge(Element element, GMLRoot root) {
        super(element, root);
        this.source = element.getAttribute("source");
        this.target = element.getAttribute("target");
        this.points = new ArrayList<>();
        System.out.println("property: "+this.dataNameMap.get("edgegraphics"));
        System.out.println("value: "+((GMLComplexProperty)this.dataNameMap.get("edgegraphics")).value);

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // get various attributes

        try {
            // get intermediate points if there
            NodeList list = (NodeList) xpath.evaluate(".//Point", element, XPathConstants.NODESET);
            for(int i=0; i < list.getLength(); i++) {
                Element point = (Element) list.item(i);
                Point2D.Float p = new Point2D.Float();
                p.x = Float.parseFloat(point.getAttribute("x"));
                p.y = Float.parseFloat(point.getAttribute("y"));
                this.points.add(p);
            }

            // get port diffs
            Element pathElement = (Element) xpath.evaluate(".//Path[1]", element, XPathConstants.NODE);
            Point2D.Float s = new Point2D.Float();
            s.x = Float.parseFloat(pathElement.getAttribute("sx"));
            s.y = Float.parseFloat(pathElement.getAttribute("sy"));
            this.startDiff = s;

            Point2D.Float end = new Point2D.Float();
            end.x = Float.parseFloat(pathElement.getAttribute("tx"));
            end.y = Float.parseFloat(pathElement.getAttribute("ty"));
            this.endDiff = end;

            // get arrow shapes
            Element arrowsElement = (Element) xpath.evaluate(".//Arrows[1]", element, XPathConstants.NODE);
            this.sourceArrowShape = arrowsElement.getAttribute("source");
            this.targetArrowShape = arrowsElement.getAttribute("target");


        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(GraphMLVisitor visitor) {
        visitor.visit(this);
    }

    public String getClass_() {
        String class_ = "unknown";
        if(this.dataNameMap.containsKey("class")) {
            class_ = ((GMLSimpleProperty)this.dataNameMap.get("class")).value;
        }
        else if(this.dataNameMap.containsKey("connection_type")) {
            String type = ((GMLSimpleProperty)this.dataNameMap.get("connection_type")).value;
            System.out.println("TYPE: "+type);
            switch(type) {
                case "Activation":
                    class_ = "positive influence";
                    break;
                case "Inhibition":
                    class_ = "negative influence";
                    break;
                default:
                    class_ = "unknown influence";
            }
        }
        return class_;
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }

    public Point2D.Float getStart() {
        return null;
    }

    public Point2D.Float getEnd() {
        return null;
    }

    public List<Point2D.Float> getPoints() {
        return this.points;
    }

    @Override
    public boolean accept(HierarchicalVisitor visitor) {
        return visitor.visitLeaf(this);
    }

    public boolean hasSingleArrow() {
        System.out.println(this.targetArrowShape+" "+this.sourceArrowShape);
        return (this.targetArrowShape.equals("none") || this.sourceArrowShape.equals("none"));
    }

    // returns whatever single value that is different from none for the edge
    // if both ends are none, returns none
    // works only for single arrow edges
    public String getArrowShape() {
        if(!this.targetArrowShape.equals("none"))
            return this.targetArrowShape;
        else
            return this.sourceArrowShape;
    }

    @Override
    public Element toXmlElement(Element parent, Document root) {
        Element edgeE = root.createElement("edge");
        edgeE.setAttribute("id", this.getId());
        edgeE.setAttribute("source", this.getSource());
        edgeE.setAttribute("target", this.getTarget());

        for(GMLProperty prop: this.dataList) {
            edgeE.appendChild(prop.toXmlElement(edgeE, root));
        }

        return edgeE;
    }
}
