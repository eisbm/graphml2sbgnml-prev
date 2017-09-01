package org.eisbm.visitableClasses;

import org.sbgn.bindings.Glyph;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableState implements Visitable {
    Glyph.State state;

    public VisitableState(Glyph.State state){
        this.state = state;
    }

    public void accept(SbgnmlVisitor visitor) {
        if(this.state != null) {
            visitor.visit(this.state);
        }
    }
}
