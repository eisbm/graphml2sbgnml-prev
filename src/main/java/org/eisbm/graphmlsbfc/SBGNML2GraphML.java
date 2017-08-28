package org.eisbm.graphmlsbfc;

import com.sun.webkit.dom.CDATASectionImpl;
import org.eisbm.graphml.*;
import org.sbfc.converter.GeneralConverter;
import org.sbfc.converter.exceptions.ConversionException;
import org.sbfc.converter.exceptions.ReadModelException;
import org.sbfc.converter.models.GeneralModel;
import org.sbfc.converter.models.SBGNModel;
import org.sbgn.bindings.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SBGNML2GraphML extends GeneralConverter {
    static Logger LOG = LoggerFactory.getLogger(SBGNML2GraphML.class);

    public SBGNML2GraphML() {

    }

    public GMLRoot toGraphml(Sbgn sbgn) {
        GMLRoot root = new GMLRoot();

        // add default property definitions
        for(GMLPropertyDefinition propDef: getDefaultKeys()) {
            root.addPropertyDefinition(propDef);
        }
        // add the default resource data section
        root.addComplexDataFor("resources", XMLElementFactory.getResourcesElement());

        // add main graph element
        Map mainMap = sbgn.getMap().get(0);
        GMLGraph mainGraph = new GMLGraph(mainMap.getId(), root);
        root.addGraph(mainGraph);

        // add some data to the main graph
        mainGraph.addSimpleDataFor("language", mainMap.getLanguage());

        for(Glyph glyph: mainMap.getGlyph()) {
            GMLNode node = convert(glyph, root, false);
            //mainGraph.addNode(node);
        }

        for(Arc arc: mainMap.getArc()) {
            GMLEdge edge = convert(arc, root);
        }





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

    public static GMLNode convert(Glyph glyph, GMLRoot root, boolean doExternallyAssignParent) {
        System.out.println("convert glyph "+glyph);
        GMLNode node;
        String glyphClass = glyph.getClazz();
        // group nodes case
        if(glyphClass.equals("compartment") ||
                glyphClass.equals("complex") ||
                glyphClass.equals("complex multimer")){
            node =new GMLGroupNode(glyph.getId(), root);

            // in the case of complexes, we need to traverse subunits now
            if(glyphClass.equals("complex") || glyphClass.equals("complex multimer")){
                for(Glyph subunit: glyph.getGlyph()) {
                    GMLNode subNode = convert(subunit, root, true);
                    ((GMLGroupNode) node).getGraph().addNode(subNode, root);
                }
            }

            Bbox bbox = glyph.getBbox();
            node.setHeight(bbox.getH());
            node.setWidth(bbox.getW());
            node.setX(bbox.getX());
            node.setY(bbox.getY());

            node.setLabel(glyph.getLabel().getText());

            node.setShapeType("rectangle");

        }
        // other cases (normal leaf nodes)
        else {
            node = new GMLSimpleNode(glyph.getId(), root);
            System.out.println("simple node nodegraphics");

            Bbox bbox = glyph.getBbox();
            node.setHeight(bbox.getH());
            node.setWidth(bbox.getW());
            node.setX(bbox.getX());
            node.setY(bbox.getY());

            node.setLabel(glyph.getLabel().getText());

            node.setShapeType("rectangle");

        }

        // assign the created node to correct parent
        if(!doExternallyAssignParent) {
            if (glyph.getCompartmentRef() == null) {
                System.out.println("add node to root " + node);
                root.graphList.get(0).addNode(node, root);
            } else {
                System.out.println("add node to other node " + glyph.getCompartmentRef());
                String compartmentId = ((Glyph) glyph.getCompartmentRef()).getId();
                GMLGroupNode groupNode = (GMLGroupNode) root.nodeMap.get(compartmentId);
                groupNode.getGraph().addNode(node, root);
            }
        }

        // go through the node's unit of info/state var
        for(Glyph auxUnit: glyph.getGlyph()) {
            String auxUnitClass = auxUnit.getClazz();
            if(auxUnitClass.equals("unit of information")
                    || auxUnitClass.equals("state variable")) {
                System.out.println("found auxunit: "+ auxUnit.getClazz());
                GMLLabelNode labelNode = new GMLLabelNode(auxUnit.getId(), node);
                node.getLabelNodes().add(labelNode);

                String label;
                String shapeType;
                if(auxUnitClass.equals("unit of information")){
                    label = auxUnit.getLabel().getText();
                    shapeType = "rectangle";
                }
                else {
                    label = auxUnit.getState().getVariable()+"@"+auxUnit.getState().getVariable();
                    shapeType = "roundrectangle";
                }
                labelNode.setLabel(label);
                labelNode.setShapeType(shapeType);

                labelNode.setX(auxUnit.getBbox().getX());
                labelNode.setY(auxUnit.getBbox().getY());
                labelNode.setWidth(auxUnit.getBbox().getW());
                labelNode.setHeight(auxUnit.getBbox().getH());
            }
        }

        // add all possible additional information as data
        node.addSimpleDataFor("class", glyph.getClazz());



        return node;

    }

    public static GMLEdge convert(Arc arc, GMLRoot root) {
        System.out.println("convert edge");
        GMLEdge edge = new GMLEdge(arc.getId(), root);

        edge.setSource(((Glyph) arc.getSource()).getId());
        edge.setTarget(((Glyph) arc.getTarget()).getId());

        Element polyLine = XMLElementFactory.getPolyLineEdgeElement();

        polyLine.appendChild(XMLElementFactory.getPathElement());
        polyLine.appendChild(XMLElementFactory.getLineStyleElement());

        polyLine.appendChild(XMLElementFactory.getArrowsElement("none", "none"));

        polyLine.appendChild(XMLElementFactory.getBendStyleElement());


        edge.addComplexDataFor("edgegraphics", polyLine);

        root.graphList.get(0).addEdge(edge);

        // add all possible additional information as data
        edge.addSimpleDataFor("class", arc.getClazz());

        return edge;
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
