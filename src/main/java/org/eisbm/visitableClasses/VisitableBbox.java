package org.eisbm.visitableClasses;

import org.sbgn.bindings.Bbox;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableBbox implements Visitable {
    Bbox bbox;

    public VisitableBbox(Bbox bbox){
        this.bbox = bbox;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.bbox != null) {
            VisitableNotes vnotes = new VisitableNotes(this.bbox.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.bbox.getExtension());
            vextension.accept(visitor);

            visitor.visit(this.bbox);
        }
    }
}
