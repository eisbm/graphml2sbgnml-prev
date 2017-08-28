package org.eisbm.graphmlsbfc;

import org.eisbm.graphml.*;
import org.sbgn.GlyphClazz;
import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Map;
import org.sbgn.bindings.Sbgn;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ConvertionVisitor implements HierarchicalVisitor {

    Sbgn sbgn;
    Map map;
    Stack<GMLGroupNode> stack;
    List<Glyph> glyphs;
    List<Arc> arcs;
    GMLRoot root;
    InfoLookup lookup;

    public ConvertionVisitor(GMLRoot root, InfoLookup lookup){
        this.stack = new Stack<>();
        this.glyphs = new ArrayList<>();
        this.arcs = new ArrayList<>();
        this.root = root;
        this.lookup = lookup;
    }

    @Override
    public boolean visitEnter(GMLRoot root) {
        System.out.println("visit enter root");
        this.sbgn = new Sbgn();
        this.map = new Map();
        this.sbgn.getMap().add(map);

        return true;
    }

    @Override
    public boolean visitExit(GMLRoot root) {
        System.out.println("visit exit root");
        return false;
    }

    @Override
    public boolean visitEnter(GMLGraph graph) {
        System.out.println("visit enter graph");
        return true;
    }

    @Override
    public boolean visitExit(GMLGraph graph) {
        System.out.println("visit exit graph");
        return false;
    }

    @Override
    public boolean visitEnter(GMLNode node) {
        System.out.println("visit enter node");
        Glyph g = GraphML2SBGNML.convert(node, this.lookup);

        if(this.stack.size() > 0){ // we are in some compartment
            Glyph ref = new Glyph();
            ref.setId(this.stack.peek().getId());
            g.setCompartmentRef(ref); // doesn't accept id, need glyph with Id...
        }

        // get deeper and add to stack if compartment
        if(node instanceof GMLGroupNode && g.getClazz().equals(GlyphClazz.COMPARTMENT.toString())) {
            this.stack.push((GMLGroupNode)node);
        }

        this.glyphs.add(g);
        // we don't want to continue for nested complex subunits
        // as they are processed once in the complex convert
        if(g.getClazz().equals(GlyphClazz.COMPLEX.toString())
                || g.getClazz().equals(GlyphClazz.COMPLEX_MULTIMER.toString())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean visitExit(GMLNode node) {
        System.out.println("visit exit node");
        this.stack.pop();
        return false;
    }

    @Override
    public boolean visitLeaf(GMLNode node) {
        System.out.println("visit leaf node");
        Glyph g = GraphML2SBGNML.convert(node, this.lookup);

        if(this.stack.size() > 0){ // we are in some compartment
            Glyph ref = new Glyph();
            ref.setId(this.stack.peek().getXMLValidId());
            g.setCompartmentRef(ref);
        }
        this.glyphs.add(g);
        return false;
    }

    @Override
    public boolean visitLeaf(GMLEdge edge) {
        System.out.println("visit leaf edge");
        Arc a = GraphML2SBGNML.convert(edge, this.root, this.lookup);

        this.arcs.add(a);
        return false;
    }
}
