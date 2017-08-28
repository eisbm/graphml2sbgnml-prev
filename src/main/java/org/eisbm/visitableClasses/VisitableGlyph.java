package org.eisbm.visitableClasses;

import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Port;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableGlyph implements Visitable {
    Glyph glyph;

    public VisitableGlyph(Glyph glyph){
        this.glyph = glyph;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.glyph != null) {
            VisitableNotes vnotes = new VisitableNotes(this.glyph.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.glyph.getExtension());
            vextension.accept(visitor);

            VisitableLabel vlabel = new VisitableLabel(this.glyph.getLabel());
            vlabel.accept(visitor);

            VisitableState vstate = new VisitableState(this.glyph.getState());
            vstate.accept(visitor);

            VisitableClone vclone = new VisitableClone(this.glyph.getClone());
            vclone.accept(visitor);

            VisitableCallout vcallout = new VisitableCallout(this.glyph.getCallout());
            vcallout.accept(visitor);

            VisitableEntity ventity = new VisitableEntity(this.glyph.getEntity());
            ventity.accept(visitor);

            VisitableBbox vbbox = new VisitableBbox(this.glyph.getBbox());
            vbbox.accept(visitor);

            for (Glyph subglyph : this.glyph.getGlyph()) {
                VisitableGlyph vglyph = new VisitableGlyph(subglyph);
                vglyph.accept(visitor);
            }

            for (Port port : this.glyph.getPort()) {
                VisitablePort vport = new VisitablePort(port);
                vport.accept(visitor);
            }

            visitor.visit(this.glyph);
        }
    }
}
