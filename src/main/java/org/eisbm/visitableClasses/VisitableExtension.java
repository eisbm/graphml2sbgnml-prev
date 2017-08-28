package org.eisbm.visitableClasses;

import org.sbgn.bindings.SBGNBase;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableExtension implements Visitable {
    SBGNBase.Extension extension;

    public VisitableExtension(SBGNBase.Extension extension) {
        this.extension = extension;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.extension != null) {
            visitor.visit(this.extension);
        }
    }
}
