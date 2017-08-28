package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.*;
import org.eisbm.graphmlsbfc.configuration.Configuration;
import org.sbfc.converter.GeneralConverter;
import org.sbfc.converter.exceptions.ConversionException;
import org.sbfc.converter.exceptions.ReadModelException;
import org.sbfc.converter.models.GeneralModel;
import org.sbfc.converter.models.SBGNModel;
import org.sbgn.GlyphClazz;
import org.sbgn.Language;
import org.sbgn.bindings.*;

import java.awt.geom.Point2D;
import java.io.IOException;

public class GraphML2SBGNML extends GeneralConverter {

    private String configFileName = "src/main/resources/default.yml";

    public GraphML2SBGNML() {
        super();
    }

    public GraphML2SBGNML(String configFileName) {
        super();
        this.configFileName = configFileName;
    }

    public Sbgn toSbgn(GMLRoot graphml) {
        System.out.println("converting");
        // guesser will help assign missing information to different elements
        Guesser guesser = new DefaultGuesser(graphml);
        Configuration conf = null;
        try {
            conf = Configuration.readFromFile(this.configFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InfoLookup lookup = new InfoLookup(conf, null);

        // init start of sbgn hierarchy
        Sbgn sbgn = new Sbgn();
        Map map = new Map();
        map.setId(graphml.graphList.get(0).getXMLValidId());

        // assign language if possible
        Language language = lookup.getMapLanguage(graphml);
        map.setLanguage(language.toString());


        ConvertionVisitor visitor = new ConvertionVisitor(graphml, lookup);
        graphml.accept(visitor);
        map.getGlyph().addAll(visitor.glyphs);
        map.getArc().addAll(visitor.arcs);

        for(Glyph g: map.getGlyph()){
            System.out.println(g.getId()+" "+g.getCompartmentRef()+" "+g.getClazz());
        }

        sbgn.getMap().add(map);

        return sbgn;
    }


    @Override
    public GeneralModel convert(GeneralModel generalModel) throws ConversionException, ReadModelException {
        GraphMLModel graphmlModel = (GraphMLModel) generalModel;

        return new SBGNModel(this.toSbgn(graphmlModel.graphml));
    }

    @Override
    public String getResultExtension() {
        return "sbgnml";
    }

    @Override
    public String getName() {
        return "graphml2sbgnml";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getHtmlDescription() {
        return null;
    }

    public static Glyph convert(GMLNode node, InfoLookup lookup) {
        Glyph glyph = new Glyph();
        glyph.setId(node.getXMLValidId());

        Bbox bbox = new Bbox();
        bbox.setX(node.getX());
        bbox.setY(node.getY());
        bbox.setH(node.getHeight());
        bbox.setW(node.getWidth());
        glyph.setBbox(bbox);

        if(node.getLabel() != null) {
            Label label = new Label();
            label.setText(node.getLabel());
            glyph.setLabel(label);
        }

        // class isn't obvious, it has to be mapped from configuration
        // or guessed
        GlyphClazz clazz = lookup.getGlyphClass(node);
        glyph.setClazz(clazz.toString());

        // we need to nest the subunits correctly
        if(clazz == GlyphClazz.COMPLEX || clazz == GlyphClazz.COMPLEX_MULTIMER) {
            GMLGraph subgraph = ((GMLGroupNode) node).getGraph();
            // here subgraph can only have nodes, arcs to subunits are illegal

            for(GMLNode subunits: subgraph.getNodeList()) {
                glyph.getGlyph().add(convert(subunits, lookup));
            }
        }

        // auxiliary units section
        if(!node.getLabelNodes().isEmpty()) {
            for(GMLLabelNode labelNode: node.getLabelNodes()) {
                String shape = labelNode.getShapeType();
                switch(shape) {
                    case "roundrectangle":
                        Glyph stateVar = new Glyph();
                        stateVar.setClazz("state variable");
                        stateVar.setId(glyph.getId()+"_"+GMLElement.xmlize(labelNode.getId()));

                        Bbox stateVarBox = new Bbox();
                        stateVarBox.setX(labelNode.getX());
                        stateVarBox.setY(labelNode.getY());
                        stateVarBox.setH(labelNode.getHeight());
                        stateVarBox.setW(labelNode.getWidth());
                        stateVar.setBbox(stateVarBox);

                        Glyph.State state = new Glyph.State();
                        state.setValue(labelNode.getLabel());
                        stateVar.setState(state);

                        glyph.getGlyph().add(stateVar);
                        break;
                    case "rectangle":
                        Glyph unitOfInfo = new Glyph();
                        unitOfInfo.setClazz("unit of information");
                        unitOfInfo.setId(glyph.getId()+"_"+GMLElement.xmlize(labelNode.getId()));

                        Bbox unitBox = new Bbox();
                        unitBox.setX(labelNode.getX());
                        unitBox.setY(labelNode.getY());
                        unitBox.setH(labelNode.getHeight());
                        unitBox.setW(labelNode.getWidth());
                        unitOfInfo.setBbox(unitBox);

                        Label label = new Label();
                        label.setText(labelNode.getLabel());
                        unitOfInfo.setLabel(label);

                        glyph.getGlyph().add(unitOfInfo);
                        break;

                }
            }
        }


        return glyph;
    }

    public static Arc convert(GMLEdge  edge, GMLRoot root, InfoLookup lookup) {
        Arc arc = new Arc();
        arc.setId(edge.getXMLValidId());
        arc.setClazz(edge.getClass_());

        Glyph source = new Glyph();
        source.setId(GMLElement.xmlize(edge.getSource()));
        arc.setSource(source);

        Glyph target = new Glyph();
        target.setId(GMLElement.xmlize(edge.getTarget()));
        arc.setTarget(target);

        GMLNode sourceNode = root.nodeMap.get(edge.getSource());
        GMLNode targetNode = root.nodeMap.get(edge.getTarget());

        System.out.println("voncert edge "+sourceNode+" "+targetNode+" "+sourceNode.getX());

        Arc.Start arcStart = new Arc.Start();
        float startX = sourceNode.getX() + edge.startDiff.x;
        float startY = sourceNode.getY() + edge.startDiff.y;
        arcStart.setX(startX);
        arcStart.setY(startY);
        arc.setStart(arcStart);

        Arc.End arcEnd = new Arc.End();
        float endX = targetNode.getX() + edge.endDiff.x;
        float endY = targetNode.getY() + edge.endDiff.y;
        arcEnd.setX(endX);
        arcEnd.setY(endY);
        arc.setEnd(arcEnd);

        for(Point2D.Float p : edge.getPoints()) {
            Arc.Next next = new Arc.Next();
            next.setX(p.x);
            next.setY(p.y);
            arc.getNext().add(next);
        }

        arc.setClazz(lookup.getArcClass(edge).toString());

        return arc;
    }
}
