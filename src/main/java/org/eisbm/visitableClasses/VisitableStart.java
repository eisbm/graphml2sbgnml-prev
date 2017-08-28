package org.eisbm.visitableClasses;

import org.sbgn.bindings.Arc;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableStart implements Visitable {
    Arc.Start start;

    public VisitableStart(Arc.Start start) {
        this.start = start;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.start != null) {
            visitor.visit(this.start);
        }
    }
}
