package org.eisbm.visitableClasses;

import org.sbgn.bindings.Port;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitablePort implements Visitable {
    Port port;

    public VisitablePort(Port port) {
        this.port = port;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.port != null) {
            VisitableNotes vnotes = new VisitableNotes(this.port.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.port.getExtension());
            vextension.accept(visitor);

            visitor.visit(this.port);
        }
    }
}
