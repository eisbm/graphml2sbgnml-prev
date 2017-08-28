package org.eisbm.visitableClasses;

import org.sbgn.bindings.Label;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableLabel implements Visitable {
    Label label;

    public VisitableLabel(Label label){
        this.label = label;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.label != null) {
            VisitableNotes vnotes = new VisitableNotes(this.label.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.label.getExtension());
            vextension.accept(visitor);

            VisitableBbox vbbox = new VisitableBbox(this.label.getBbox());
            vbbox.accept(visitor);

            visitor.visit(this.label);
        }
    }
}
