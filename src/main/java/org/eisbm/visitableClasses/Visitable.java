package org.eisbm.visitableClasses;

import org.eisbm.visitor.SbgnmlVisitor;

public interface Visitable {
    void accept(SbgnmlVisitor visitor);
}
