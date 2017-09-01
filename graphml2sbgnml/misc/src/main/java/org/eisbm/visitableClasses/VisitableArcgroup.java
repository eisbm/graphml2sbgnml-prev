package org.eisbm.visitableClasses;

import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Arcgroup;
import org.sbgn.bindings.Glyph;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableArcgroup implements Visitable {
    Arcgroup arcgroup;

    public VisitableArcgroup(Arcgroup arcgroup) {
        this.arcgroup = arcgroup;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.arcgroup != null) {
            VisitableNotes vnotes = new VisitableNotes(this.arcgroup.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.arcgroup.getExtension());
            vextension.accept(visitor);

            for (Glyph glyph : this.arcgroup.getGlyph()) {
                VisitableGlyph vglyph = new VisitableGlyph(glyph);
                vglyph.accept(visitor);
            }

            for (Arc arc : this.arcgroup.getArc()) {
                VisitableArc varc = new VisitableArc(arc);
                varc.accept(visitor);
            }

            visitor.visit(this.arcgroup);
        }
    }
}
