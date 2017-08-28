package org.eisbm.visitor;

import org.sbgn.bindings.*;

public class TestVisitor implements SbgnmlVisitor {

    @Override
    public void visit(Arc arc) {
        System.out.println("visiting arc");
    }

    @Override
    public void visit(Arcgroup arcgroup) {

    }

    @Override
    public void visit(Bbox bbox) {
        System.out.println("visiting bbox");
    }

    @Override
    public void visit(Glyph.Callout callout) {

    }

    @Override
    public void visit(Glyph.Clone clone) {
        System.out.println("visiting clone");
    }

    @Override
    public void visit(Arc.End end) {
        System.out.println("visiting end "+end);
    }

    @Override
    public void visit(Glyph.Entity entity) {
        System.out.println("visiting entity");
    }

    @Override
    public void visit(SBGNBase.Extension extension) {
        System.out.println("visiting extension");
    }

    @Override
    public void visit(Glyph glyph) {
        System.out.println("visiting glyph");
    }

    @Override
    public void visit(Label label) {
        System.out.println("visiting label "+label.getText());
    }

    @Override
    public void visit(Map map) {
        System.out.println("visiting map");
    }

    @Override
    public void visit(Arc.Next next) {
        System.out.println("visiting next");
    }

    @Override
    public void visit(SBGNBase.Notes notes) {
        System.out.println("visiting notes");
    }

    @Override
    public void visit(Point point) {

    }

    @Override
    public void visit(Port port) {
        System.out.println("visiting port");
    }

    @Override
    public void visit(Sbgn sbgn) {
        System.out.println("visiting sbgn");
    }

    @Override
    public void visit(SBGNBase sbgnBase) {

    }

    @Override
    public void visit(Arc.Start start) {
        System.out.println("visiting start "+start);
    }

    @Override
    public void visit(Glyph.State state) {
        System.out.println("visiting state");
    }
}
