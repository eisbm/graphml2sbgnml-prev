package org.eisbm.visitableClasses;

import org.sbgn.bindings.Arc;
import org.sbgn.bindings.Point;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableNext implements Visitable {
    Arc.Next next;

    public VisitableNext(Arc.Next end) {
        this.next = next;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.next != null) {
            for (Point point : this.next.getPoint()) {
                VisitablePoint vpoint = new VisitablePoint(point);
                vpoint.accept(visitor);
            }

            visitor.visit(this.next);
        }
    }
}
