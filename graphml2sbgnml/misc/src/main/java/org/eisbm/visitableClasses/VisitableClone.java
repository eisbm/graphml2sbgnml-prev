package org.eisbm.visitableClasses;

import org.sbgn.bindings.Glyph;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableClone implements Visitable {
    Glyph.Clone clone;

    public VisitableClone(Glyph.Clone clone) {
        this.clone = clone;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.clone != null) {
            VisitableLabel vlabel = new VisitableLabel(this.clone.getLabel());
            vlabel.accept(visitor);

            visitor.visit(this.clone);
        }
    }
}
