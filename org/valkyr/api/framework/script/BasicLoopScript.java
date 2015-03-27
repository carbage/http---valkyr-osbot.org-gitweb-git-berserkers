package org.valkyr.api.framework.script;

import org.valkyr.api.framework.script.utils.Timer;

/**
 * @Author Josef
 */
public abstract class BasicLoopScript extends LoopScript {

    private static final int INTERVAL = 1000;
    private Timer timer = new Timer(0);

    @Override
    public int onLoop() {
        return process();
    }

    public abstract int process();

    @Override
    public void onStart() {
        start();
    }

    public abstract boolean start();

    @Override
    public void onExit() {
        finish();
    }

    public abstract void finish();

    public int getIndex() {
        return getBot().getId();
    }

    public String getRunTime() {
        return timer.toElapsedString();
    }
}
