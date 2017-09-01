package org.eisbm.graphml;

public interface HierarchyVisitable {
    boolean accept(HierarchicalVisitor visitor);
}
