package org.eisbm.visitableClasses;

import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Port;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableArc implements Visitable {
    Arc arc;

    public VisitableArc(Arc arc) {
        this.arc = arc;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.arc != null) {
            VisitableNotes vnotes = new VisitableNotes(this.arc.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.arc.getExtension());
            vextension.accept(visitor);

            for (Glyph glyph : this.arc.getGlyph()) {
                VisitableGlyph vglyph = new VisitableGlyph(glyph);
                vglyph.accept(visitor);
            }

            for (Port port : this.arc.getPort()) {
                VisitablePort vport = new VisitablePort(port);
                vport.accept(visitor);
            }

            VisitableStart vstart = new VisitableStart(this.arc.getStart());
            vstart.accept(visitor);

            for (Arc.Next next : this.arc.getNext()) {
                VisitableNext vnext = new VisitableNext(next);
                vnext.accept(visitor);
            }

            VisitableEnd vend = new VisitableEnd(this.arc.getEnd());
            vend.accept(visitor);

            visitor.visit(this.arc);
        }
    }
}
