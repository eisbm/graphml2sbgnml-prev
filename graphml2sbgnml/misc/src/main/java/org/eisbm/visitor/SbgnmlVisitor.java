package org.eisbm.visitor;

import org.sbgn.bindings.*;

public interface SbgnmlVisitor {
    void visit(Arc arc);
    void visit(Arcgroup arcgroup);
    void visit(Bbox bbox);
    void visit(Glyph.Callout callout);
    void visit(Glyph.Clone clone);
    void visit(Arc.End end);
    void visit(Glyph.Entity entity);
    void visit(SBGNBase.Extension extension);
    void visit(Glyph glyph);
    void visit(Label label);
    void visit(Map map);
    void visit(Arc.Next next);
    void visit(SBGNBase.Notes notes);
    void visit(Point point);
    void visit(Port port);
    void visit(Sbgn sbgn);
    void visit(SBGNBase sbgnBase);
    void visit(Arc.Start start);
    void visit(Glyph.State state);
}
