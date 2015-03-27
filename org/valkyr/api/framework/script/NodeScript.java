package org.valkyr.api.framework.script;

import org.valkyr.api.framework.gui.Gui;
import org.valkyr.api.framework.script.utils.Timer;

import java.util.LinkedList;

/**
 * @Author Josef
 */
public abstract class NodeScript extends LoopScript {

    public static final int INTERVAL = 1000;
    private Timer timer = new Timer(0);
    private LinkedList<Node> nodes = new LinkedList<Node>();

    @Override
    public int process() {
        for (Node n : nodes)
            if (n.validate())
                return n.process();
        return 0;
    }

    @Override
    public void onExit() {
        nodes.clear();
        Gui.get().removeTab(this);
        submitStats();
        finish();
    }

    @Override
    public void finish() {
    }

    public void join(Node... nodes) {
        for (Node n : nodes)
            if (!this.nodes.contains(n) && n != null) {
                this.nodes.add(n);
            }
    }

    public Node getNode(Class c) {
        for (Node n : nodes)
            if (n.getClass() == c)
                return n;
        return null;
    }
}
