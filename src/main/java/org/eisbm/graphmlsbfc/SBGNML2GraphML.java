package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.*;
import org.sbfc.converter.GeneralConverter;
import org.sbfc.converter.exceptions.ConversionException;
import org.sbfc.converter.exceptions.ReadModelException;
import org.sbfc.converter.models.GeneralModel;
import org.sbfc.converter.models.SBGNModel;
import org.sbgn.bindings.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SBGNML2GraphML extends GeneralConverter {
    static Logger LOG = LoggerFactory.getLogger(SBGNML2GraphML.class);

    static Document dummyDoc;
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

    public SBGNML2GraphML() {

    }

    public GMLRoot toGraphml(Sbgn sbgn) {
        GMLRoot root = new GMLRoot();

        // add default property definitions
        for(GMLPropertyDefinition propDef: getDefaultKeys()) {
            root.addPropertyDefinition(propDef);
        }

        // add main graph element
        Map mainMap = sbgn.getMap().get(0);
        GMLGraph mainGraph = new GMLGraph(mainMap.getId());

        for(Glyph glyph: mainMap.getGlyph()) {
            GMLNode node = convert(glyph, root);
            mainGraph.addNode(node);
        }

        /*for(Arc arc: mainMap.getArc()) {
            GMLEdge edge = convert(arc, root);
            mainGraph.addEdge(edge);
        }*/



        root.addGraph(mainGraph);

        return root;
    }

    @Override
    public GeneralModel convert(GeneralModel generalModel) throws ConversionException, ReadModelException {
        SBGNModel sbgnmlModel = (SBGNModel) generalModel;

        // TODO: change it to make it correct; Impossible for now as sbgn object cannot be accessed from sbgnModel
        return new GraphMLModel(this.toGraphml(new Sbgn()));
    }

    @Override
    public String getResultExtension() {
        return "graphml";
    }

    @Override
    public String getName() {
        return "sbgnml2graphml";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getHtmlDescription() {
        return null;
    }

    public static GMLNode convert(Glyph glyph, GMLRoot root) {
        System.out.println("convert glyph "+glyph);
        GMLNode node;
        if(glyph.getClazz().equals("compartment") ||
                glyph.getClazz().equals("complex") ||
                glyph.getClazz().equals("complex multimer")){
            node =new GMLGroupNode(glyph.getId());
        }
        else {
            node = new GMLSimpleNode(glyph.getId());
            System.out.println("simple node nodegraphics");

            Element data = dummyDoc.createElement("data");
            data.setAttribute("key", "d3");

            Element shapeNode = dummyDoc.createElement("y:ShapeNode");
            shapeNode.appendChild(getGeometryElement(glyph.getBbox()));
            shapeNode.appendChild(getFillElement());
            shapeNode.appendChild(getBorderStyleElement());
            shapeNode.appendChild(getNodeLabelElement(glyph.getLabel().getText()));
            shapeNode.appendChild(getShapeElement(glyph.getClazz()));
            data.appendChild(shapeNode);

            //GMLComplexProperty prop = new GMLComplexProperty();
            node.addData(new GMLComplexProperty(data, root));


        }



        return node;

    }

    public static Element getGeometryElement(Bbox bbox) {
        Element geomE = dummyDoc.createElement("y:Geometry");
        geomE.setAttribute("height", Float.toString(bbox.getH()));
        geomE.setAttribute("width", Float.toString(bbox.getW()));
        geomE.setAttribute("x", Float.toString(bbox.getX()));
        geomE.setAttribute("y", Float.toString(bbox.getY()));
        return geomE;
    }

    public static Element getFillElement() {
        Element fillE = dummyDoc.createElement("y:Fill");
        fillE.setAttribute("color", "#FFFFFF");
        fillE.setAttribute("transparent", "false");
        return fillE;
    }

    public static Element getBorderStyleElement() {
        Element borderStyleE = dummyDoc.createElement("y:BorderStyle");
        borderStyleE.setAttribute("color", "#000000");
        borderStyleE.setAttribute("raised", "false");
        borderStyleE.setAttribute("type", "line");
        borderStyleE.setAttribute("width", "1.0");
        return borderStyleE;
    }

    public static Element getNodeLabelElement(String label) {
        Element nodeLabelE = dummyDoc.createElement("y:NodeLabel");
        /*nodeLabelE.setAttribute("alignment", "center");
        nodeLabelE.setAttribute("autoSizePolicy", "content");
        nodeLabelE.setAttribute("fontFamily", "Dialog");
        nodeLabelE.setAttribute("fontSize", "12");
        nodeLabelE.setAttribute("fontStyle", "plain");
        nodeLabelE.setAttribute("hasBackgroundColor", "false");
        nodeLabelE.setAttribute("hasLineColor", "false");*/
        nodeLabelE.setTextContent(label);

        return nodeLabelE;
    }

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

    public static GMLEdge convert(Arc arc, GMLRoot root) {
        return new GMLEdge("idtest");
    }

    // default set of key definitions that should always be present in the graphml
    public static List<GMLPropertyDefinition> getDefaultKeys() {
        List<GMLPropertyDefinition> propDefList = new ArrayList<>();

        // port property definitions
        // are they useful ??
        HashMap<String, String> propAttributes1 = new HashMap<>();
        propAttributes1.put("yfiles.type", "portgraphics");
        GMLPropertyDefinition def1 = new GMLPropertyDefinition("d0", "port", propAttributes1);
        propDefList.add(def1);

        HashMap<String, String> propAttributes2 = new HashMap<>();
        propAttributes2.put("yfiles.type", "portgeometry");
        GMLPropertyDefinition def2 = new GMLPropertyDefinition("d1", "port", propAttributes2);
        propDefList.add(def2);

        HashMap<String, String> propAttributes3 = new HashMap<>();
        propAttributes3.put("yfiles.type", "portuserdata");
        GMLPropertyDefinition def3 = new GMLPropertyDefinition("d2", "port", propAttributes3);
        propDefList.add(def3);

        HashMap<String, String> propAttributes4 = new HashMap<>();
        propAttributes4.put("yfiles.type", "nodegraphics");
        GMLPropertyDefinition def4 = new GMLPropertyDefinition("d3", "node", propAttributes4);
        propDefList.add(def4);

        HashMap<String, String> propAttributes5 = new HashMap<>();
        propAttributes5.put("yfiles.type", "edgegraphics");
        GMLPropertyDefinition def5 = new GMLPropertyDefinition("d4", "edge", propAttributes5);
        propDefList.add(def5);

        HashMap<String, String> propAttributes6 = new HashMap<>();
        propAttributes6.put("yfiles.type", "resources");
        GMLPropertyDefinition def6 = new GMLPropertyDefinition("d5", "graphml", propAttributes6);
        propDefList.add(def6);

        return propDefList;
    }

    public static java.util.Map<String, List<Glyph>> getCompartmentHierarchy (org.sbgn.bindings.Map map) {
        java.util.Map<String, List<Glyph>> compartmentMap = new HashMap<>();
        for(Glyph glyph: map.getGlyph()) {
            if(glyph.getClazz().equals("compartment")) {
                compartmentMap.put(glyph.getId(), new ArrayList<>());
            }
            else {
                if(glyph.getCompartmentRef() != null) {
                    String idRef = ((Glyph)glyph.getCompartmentRef()).getId();
                    if(compartmentMap.containsKey(idRef)) {
                        List<Glyph> glyphs = compartmentMap.get(idRef);
                        glyphs.add(glyph);
                    }
                    else {
                        LOG.warn("idRef: "+idRef+" was found in glyph with id: "+glyph.getId()+" but doesn't belong" +
                                " to any comaprtment glyph");
                    }

                }
            }
        }
        return compartmentMap;
    }
}
