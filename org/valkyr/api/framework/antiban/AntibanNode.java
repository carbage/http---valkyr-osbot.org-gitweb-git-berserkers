package org.valkyr.api.framework.antiban;

import org.valkyr.api.framework.script.LoopScript;
import org.valkyr.api.framework.script.Node;
import org.valkyr.api.framework.script.utils.Timer;

import java.util.Random;

public class AntibanNode extends Node {

    private static final int INTERVAL = AntibanUtils.INTERVAL * 50;
    private Random rng;
    private Timer antibanTimer;

    public AntibanNode(LoopScript script) {
        super(script);
        rng = new Random(System.currentTimeMillis());
        antibanTimer = new Timer(rng.nextInt(INTERVAL));
        antibanTimer.setEndIn(rng.nextInt(INTERVAL));
    }

    @Override
    public int process() {
        switch (rng.nextInt(100)) {
            case 0:
                script.getAbhelper().combatCheck();
                break;
            case 1:
                script.getAbhelper().questCheck();
                break;
            case 2:
                script.getAbhelper().checkXp();
                break;
            case 3:
                script.getAbhelper().friendsCheck();
                break;
            case 4:
                script.getAbhelper().equipmentCheck();
                break;
            case 5:
                script.getAbhelper().moveCamera();
                break;
            case 6:
                script.getAbhelper().mouseMovement();
                break;
            case 7:
                script.getAbhelper().rightClick();
                break;
            case 8:
                script.getAbhelper().examineObject();
                break;
            case 9:
                script.getAbhelper().pickUpMouse();
                break;
        }
        antibanTimer.setEndIn(rng.nextInt(INTERVAL));
        return 100;
    }

    @Override
    public boolean validate() {
        return !antibanTimer.isRunning();
    }
}
