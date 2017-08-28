package org.eisbm.visitableClasses;

import org.sbgn.bindings.SBGNBase;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableNotes implements Visitable {
    SBGNBase.Notes notes;

    public VisitableNotes(SBGNBase.Notes notes){
        this.notes = notes;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.notes != null) {
            visitor.visit(this.notes);
        }
    }
}
