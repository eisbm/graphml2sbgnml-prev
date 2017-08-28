package org.eisbm.graphml;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLElementFactory {

    public static Document dummyDoc;
    static {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        dummyDoc = db.newDocument();
    }

    public static Document getDocument() {
        return dummyDoc;
    }

    public static Element getDataElement(String key) {
        Element data = dummyDoc.createElement("data");
        data.setAttribute("key", key);
        return data;
    }

    public static Element getNodeElement(String id) {
        Element data = dummyDoc.createElement("node");
        data.setAttribute("id", id);
        return data;
    }

    public static Element getGraphElement(String id) {
        Element graph = dummyDoc.createElement("graph");
        graph.setAttribute("id", id);
        graph.setAttribute("edgedefault", "directed");
        return graph;
    }

    public static Element getEdgeElement(String id, String source, String target) {
        Element edge = dummyDoc.createElement("edge");
        edge.setAttribute("id", id);
        edge.setAttribute("source", source);
        edge.setAttribute("target", target);
        return edge;
    }

    public static Element getKeyElement(String id, String for_) {
        Element key = dummyDoc.createElement("key");
        key.setAttribute("id", id);
        key.setAttribute("for", for_);
        return key;
    }

    public static Element getShapeNodeElement() {
        return dummyDoc.createElement("y:ShapeNode");
    }

    public static Element getGeometryElement(float h, float w, float x, float y) {
        Element geomE = dummyDoc.createElement("y:Geometry");
        geomE.setAttribute("height", Float.toString(h));
        geomE.setAttribute("width", Float.toString(w));
        geomE.setAttribute("x", Float.toString(x));
        geomE.setAttribute("y", Float.toString(y));
        return geomE;
    }

    public static Element getFillElement() {
        return getFillElement("#FFFFFF", false);
    }

    public static Element getFillElement(String color, boolean transparent) {
        Element fillE = dummyDoc.createElement("y:Fill");
        fillE.setAttribute("color", color);
        fillE.setAttribute("transparent", String.valueOf(transparent));
        return fillE;
    }

    public static Element getBorderStyleElement() {
        return getBorderStyleElement("#000000", false, "line", 1.0f);
    }

    public static Element getBorderStyleElement(String color, boolean raised, String type, float width) {
        Element borderStyleE = dummyDoc.createElement("y:BorderStyle");
        borderStyleE.setAttribute("color", color);
        borderStyleE.setAttribute("raised", String.valueOf(raised));
        borderStyleE.setAttribute("type", type);
        borderStyleE.setAttribute("width", Float.toString(width));
        return borderStyleE;
    }

    // ----- NodeLabel block ----- //

    public static class NodeLabelBuilder {
        protected Element nodeLabel;

        public NodeLabelBuilder() {
            this.nodeLabel = XMLElementFactory.getNodeLabelElement();

            // defaults
            this.nodeLabel.setAttribute("alignment", "center");
            this.nodeLabel.setAttribute("autoSizePolicy", "content");
            this.nodeLabel.setAttribute("fontFamily", "Dialog");
            this.nodeLabel.setAttribute("fontSize", String.valueOf(12));
            this.nodeLabel.setAttribute("fontStyle", "plain");
            this.nodeLabel.setAttribute("horizontalTextPosition", "center");
            this.nodeLabel.setAttribute("verticalTextPosition", "bottom");
            this.nodeLabel.setAttribute("visible", String.valueOf(true));
            this.nodeLabel.setAttribute("hasBackgroundColor", String.valueOf(false));
            this.nodeLabel.setAttribute("hasLineColor", String.valueOf(false));

        }

        public Element build() {
            return this.nodeLabel;
        }

        public NodeLabelBuilder setLabel(String label) {
            this.nodeLabel.setTextContent(label);
            return this;
        }

        public NodeLabelBuilder setDimensions(float h, float w) {
            this.nodeLabel.setAttribute("height", String.valueOf(h));
            this.nodeLabel.setAttribute("width", String.valueOf(w));
            return this;
        }

        public NodeLabelBuilder setCoordinates(float x, float y) {
            this.nodeLabel.setAttribute("x", String.valueOf(x));
            this.nodeLabel.setAttribute("y", String.valueOf(y));
            return this;
        }

        public NodeLabelBuilder setIconData(String id) {
            this.nodeLabel.setAttribute("iconData", id);
            return this;
        }

        public NodeLabelBuilder setAlignment(String alignment) {
            this.nodeLabel.setAttribute("alignment", alignment);
            return this;
        }

        public NodeLabelBuilder setAutoSizePolicy(String autoSizePolicy) {
            this.nodeLabel.setAttribute("autoSizePolicy", autoSizePolicy);
            return this;
        }

        public NodeLabelBuilder setFont(String family, int size, String type) {
            this.nodeLabel.setAttribute("fontFamily", family);
            this.nodeLabel.setAttribute("fontSize", String.valueOf(size));
            this.nodeLabel.setAttribute("fontStyle", type);
            return this;
        }

        public NodeLabelBuilder setTextPosition(String horizontal, String vertical) {
            this.nodeLabel.setAttribute("horizontalTextPosition", horizontal);
            this.nodeLabel.setAttribute("verticalTextPosition", vertical);
            return this;
        }

        public NodeLabelBuilder setModelName(String name) {
            this.nodeLabel.setAttribute("modelName", name);
            return this;
        }

        public NodeLabelBuilder setVisible(boolean visible) {
            this.nodeLabel.setAttribute("visible", String.valueOf(visible));
            return this;
        }

        public NodeLabelBuilder setIconTextGap(int textGap) {
            this.nodeLabel.setAttribute("iconTextGap", String.valueOf(textGap));
            return this;
        }

        public NodeLabelBuilder hasBackgroundColor(boolean hasBackgroundColor) {
            this.nodeLabel.setAttribute("hasBackgroundColor", String.valueOf(hasBackgroundColor));
            return this;
        }

        public NodeLabelBuilder hasLineColor(boolean hasLineColor) {
            this.nodeLabel.setAttribute("hasLineColor", String.valueOf(hasLineColor));
            return this;
        }

        public NodeLabelBuilder hasText(boolean hasText) {
            this.nodeLabel.setAttribute("hasText", String.valueOf(hasText));
            return this;
        }


        public SmartNodeLabelBuilder hasSmartModel() {
            return new SmartNodeLabelBuilder(this);
        }
    }

    public static class SmartNodeLabelBuilder extends NodeLabelBuilder {
        protected SmartNodeLabelBuilder(NodeLabelBuilder parent) {
            this.nodeLabel = parent.nodeLabel;

            Element labelModel = dummyDoc.createElement("y:LabelModel");
            this.nodeLabel.appendChild(labelModel);

            Element smartNodeLabelModel = dummyDoc.createElement("y:SmartNodeLabelModel");
            smartNodeLabelModel.setAttribute("distance", "4.0");
            labelModel.appendChild(smartNodeLabelModel);

            Element modelParam = dummyDoc.createElement("y:ModelParameter");
            this.nodeLabel.appendChild(modelParam);

            Element smartParam = dummyDoc.createElement("y:SmartNodeLabelModelParameter");
            modelParam.appendChild(smartParam);
            smartParam.setAttribute("labelRatioX", "0.0");
            smartParam.setAttribute("labelRatioY", "0.0");
            smartParam.setAttribute("nodeRatioX", "0.0");
            smartParam.setAttribute("nodeRatioY", "0.0");
            smartParam.setAttribute("offsetX", "0.0");
            smartParam.setAttribute("offsetY", "0.0");
            smartParam.setAttribute("upX", "0.0");
            smartParam.setAttribute("upY", "-1.0");

        }

        public SmartNodeLabelBuilder setDistance(float d) {
            Element smart = (Element) this.nodeLabel.getElementsByTagName("y:SmartNodeLabelModel").item(0);
            smart.setAttribute("distance", String.valueOf(d));
            return this;
        }

        public SmartNodeLabelBuilder setLabelRatio(float x, float y) {
            Element smart = (Element) this.nodeLabel.getElementsByTagName("y:SmartNodeLabelModelParameter").item(0);
            smart.setAttribute("labelRatioX", String.valueOf(x));
            smart.setAttribute("labelRatioY", String.valueOf(y));
            return this;
        }

        public SmartNodeLabelBuilder setNodeRatio(float x, float y) {
            Element smart = (Element) this.nodeLabel.getElementsByTagName("y:SmartNodeLabelModelParameter").item(0);
            smart.setAttribute("nodeRatioX", String.valueOf(x));
            smart.setAttribute("nodeRatioY", String.valueOf(y));
            return this;
        }

        public SmartNodeLabelBuilder setOffset(float x, float y) {
            Element smart = (Element) this.nodeLabel.getElementsByTagName("y:SmartNodeLabelModelParameter").item(0);
            smart.setAttribute("offsetX", String.valueOf(x));
            smart.setAttribute("offsetY", String.valueOf(y));
            return this;
        }

        public SmartNodeLabelBuilder setUp(float x, float y) {
            Element smart = (Element) this.nodeLabel.getElementsByTagName("y:SmartNodeLabelModelParameter").item(0);
            smart.setAttribute("upX", String.valueOf(x));
            smart.setAttribute("upY", String.valueOf(y));
            return this;
        }
    }

    public static Element getNodeLabelElement() {
        return dummyDoc.createElement("y:NodeLabel");
    }

    public static Element getNodeLabelElement(String label) {
        Element nodeLabelE = dummyDoc.createElement("y:NodeLabel");

        /*nodeLabelE.setAttribute("alignment", options.getAlignment());
        nodeLabelE.setAttribute("autoSizePolicy", options.getAutoSizePolicy());
        nodeLabelE.setAttribute("fontFamily", options.getFontFamily());
        nodeLabelE.setAttribute("fontSize", String.valueOf(options.getFontSize()));
        nodeLabelE.setAttribute("fontStyle", options.getFontStyle());
        nodeLabelE.setAttribute("hasBackgroundColor", String.valueOf(options.hasBackgroundColor()));
        nodeLabelE.setAttribute("hasLineColor", String.valueOf(options.hasLineColor()));
        nodeLabelE.setAttribute("hasText", String.valueOf(options.hasLineColor()));
        nodeLabelE.setAttribute("horizontalTextPosition", options.getHorizontalTextPosition());
        nodeLabelE.setAttribute("iconTextGap", String.valueOf(options.getIconTextGap()));
        nodeLabelE.setAttribute("modelName", options.getModelName());
        nodeLabelE.setAttribute("textColor", options.getTextColor());
        nodeLabelE.setAttribute("verticalTextPosition", options.getVerticalTextPosition());
        nodeLabelE.setAttribute("visible", String.valueOf(options.isVisible()));*/

        nodeLabelE.setTextContent(label);

        return nodeLabelE;
    }

    public static Element getNodeLabelElement(String label, float h, float w, float x, float y) {
        Element nodeLabel = getNodeLabelElement(label);
        nodeLabel.setAttribute("height", String.valueOf(h));
        nodeLabel.setAttribute("width", String.valueOf(w));
        nodeLabel.setAttribute("x", String.valueOf(x));
        nodeLabel.setAttribute("y", String.valueOf(y));
        return nodeLabel;
    }

    // ----- END NodeLabel block ----- //

    public static Element getShapeElement(String clazz) {
        Element shapeE = dummyDoc.createElement("y:Shape");
        String shapeType = "";
        switch(clazz) {
            case "macromolecule":
            case "macromolecule multimer":
                shapeType = "roundrectangle";
                break;
            default:
                shapeType = "rectangle";
        }


        shapeE.setAttribute("type", shapeType);
        return shapeE;
    }

    public static Element getProxyAutoBoundsNodeElement() {
        return dummyDoc.createElement("y:ProxyAutoBoundsNode");
    }

    public static Element getRealizersElement() {
        Element e = dummyDoc.createElement("y:Realizers");
        e.setAttribute("active", "0");
        return e;
    }

    public static Element getGroupNodeElement() {
        return dummyDoc.createElement("y:GroupNode");
    }

    public static Element getPolyLineEdgeElement() {
        return dummyDoc.createElement("y:PolyLineEdge");
    }

    public static Element getPathElement() {
        return getPathElement(0f, 0f, 0f, 0f);
    }

    public static Element getPathElement(float sx, float sy, float tx, float ty) {
        Element e = dummyDoc.createElement("y:Path");
        e.setAttribute("sx", String.valueOf(sx));
        e.setAttribute("sy", String.valueOf(sy));
        e.setAttribute("tx", String.valueOf(tx));
        e.setAttribute("ty", String.valueOf(ty));
        return e;
    }

    public static Element getPointElement(float x, float y) {
        Element e = dummyDoc.createElement("y:Point");
        e.setAttribute("x", String.valueOf(x));
        e.setAttribute("y", String.valueOf(y));
        return e;
    }

    public static Element getLineStyleElement() {
        return getLineStyleElement("#000000", "line", 1.0f);
    }

    public static Element getLineStyleElement(String color, String type, float width) {
        Element e = dummyDoc.createElement("y:LineStyle");
        e.setAttribute("color", color);
        e.setAttribute("type", type);
        e.setAttribute("width", String.valueOf(width));
        return e;
    }

    public static Element getArrowsElement(String source, String target) {
        Element e = dummyDoc.createElement("y:Arrows");
        e.setAttribute("source", source);
        e.setAttribute("target", target);
        return e;
    }

    public static Element getBendStyleElement () {
        return getBendStyleElement(false);
    }

    public static Element getBendStyleElement(boolean smoothed) {
        Element e = dummyDoc.createElement("y:BendStyle");
        e.setAttribute("smoothed", String.valueOf(smoothed));
        return e;
    }

    public static CDATASection getCDATA(String data) {
        return dummyDoc.createCDATASection(data);
    }

    public static Element getResourcesElement() {
        return dummyDoc.createElement("y:Resources");
    }

    public static Element getResourceElement(String id) {
        Element resource = dummyDoc.createElement("y:Resource");
        resource.setAttribute("id", id);
        return resource;
    }

    public static Element getNodeRealizerIcon() {
        return dummyDoc.createElement("yed:NodeRealizerIcon");
    }


}
