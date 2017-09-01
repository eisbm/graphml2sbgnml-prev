package org.eisbm.visitableClasses;

import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Point;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableEnd implements Visitable {
    Arc.End end;

    public VisitableEnd(Arc.End end) {
        this.end = end;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.end != null) {
            for(Point point: this.end.getPoint()) {
                VisitablePoint vpoint = new VisitablePoint(point);
                vpoint.accept(visitor);
            }

            visitor.visit(this.end);
        }
    }
}
