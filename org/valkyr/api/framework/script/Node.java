package org.valkyr.api.framework.script;

/**
 * @Author Josef
 */
public abstract class Node {
    public LoopScript script;

    public Node(LoopScript script) {
        this.script = script;
    }

    public abstract int process();

    public abstract boolean validate();
}
