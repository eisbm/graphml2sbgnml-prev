package org.eisbm.graphml;

import java.util.Stack;

public class TestRecursiveVisitor implements GraphMLVisitor {
    Stack<GMLNode> stack;

    public TestRecursiveVisitor() {
        this.stack = new Stack<>();
    }

    @Override
    public void visit(GMLRoot root) {

    }

    @Override
    public void visit(GMLGraph graph) {
        System.out.println("graph "+this.stack.size());
        //this.stack.pop();
    }

    @Override
    public void visit(GMLNode node) {
        if(node instanceof GMLGroupNode) {
            //stack.push(node);
            System.out.println("group node");
        }
        System.out.println("node "+this.stack.size());
    }

    @Override
    public void visit(GMLEdge edge) {

    }
}
