package org.eisbm.visitableClasses;

import org.sbgn.bindings.Map;
import org.sbgn.bindings.Sbgn;
import org.eisbm.visitor.SbgnmlVisitor;

public class VisitableSbgn implements Visitable {
    Sbgn sbgn;

    public VisitableSbgn(Sbgn sbgn) {
        this.sbgn = sbgn;
    }

    @Override
    public void accept(SbgnmlVisitor visitor) {
        if(this.sbgn != null) {
            VisitableNotes vnotes = new VisitableNotes(this.sbgn.getNotes());
            vnotes.accept(visitor);

            VisitableExtension vextension = new VisitableExtension(this.sbgn.getExtension());
            vextension.accept(visitor);

            for (Map map : this.sbgn.getMap()) {
                VisitableMap vmap = new VisitableMap(map);
                vmap.accept(visitor);
            }

            visitor.visit(this.sbgn);
        }

    }
}
