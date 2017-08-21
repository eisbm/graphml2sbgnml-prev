package org.eisbm.graphml;

public interface Visitable {
    void accept(GraphMLVisitor visitor);
}
