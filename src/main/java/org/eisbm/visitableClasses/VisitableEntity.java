package org.eisbm.visitableClasses;

import org.sbgn.bindings.Glyph;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableEntity implements Visitable {
    Glyph.Entity entity;

    public VisitableEntity(Glyph.Entity entity) {
        this.entity = entity;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.entity != null) {
            visitor.visit(this.entity);
        }
    }
}
