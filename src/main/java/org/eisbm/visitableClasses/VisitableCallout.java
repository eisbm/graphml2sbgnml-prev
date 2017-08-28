package org.eisbm.visitableClasses;

import org.sbgn.bindings.Glyph;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableCallout implements Visitable {
    Glyph.Callout callout;

    public VisitableCallout(Glyph.Callout callout) {
        this.callout = callout;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.callout != null) {
            VisitablePoint vpoint = new VisitablePoint(this.callout.getPoint());
            vpoint.accept(visitor);

            visitor.visit(this.callout);
        }
    }
}
