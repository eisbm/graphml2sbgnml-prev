package org.eisbm.visitableClasses;

import org.sbgn.bindings.Point;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitablePoint implements Visitable {
    Point point;

    public VisitablePoint(Point point) {
        this.point = point;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.point != null) {
            VisitableNotes vnotes = new VisitableNotes(this.point.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.point.getExtension());
            vextension.accept(visitor);

            visitor.visit(this.point);
        }
    }
}
