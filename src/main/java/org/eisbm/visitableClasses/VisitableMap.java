package org.eisbm.visitableClasses;

import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Arcgroup;
import org.sbgn.bindings.Glyph;
import org.sbgn.bindings.Map;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableMap implements Visitable {
    Map map;

    public VisitableMap(Map map){
        this.map = map;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.map != null) {
            VisitableNotes vnotes = new VisitableNotes(this.map.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.map.getExtension());
            vextension.accept(visitor);

            VisitableBbox vbbox = new VisitableBbox(this.map.getBbox());
            vbbox.accept(visitor);

            for (Glyph glyph : this.map.getGlyph()) {
                VisitableGlyph vglyph = new VisitableGlyph(glyph);
                vglyph.accept(visitor);
            }

            for (Arc arc : this.map.getArc()) {
                VisitableArc varc = new VisitableArc(arc);
                varc.accept(visitor);
            }

            for (Arcgroup arcgroup : this.map.getArcgroup()) {
                VisitableArcgroup varcgroup = new VisitableArcgroup(arcgroup);
                varcgroup.accept(visitor);
            }

            visitor.visit(this.map);
        }
    }
}
