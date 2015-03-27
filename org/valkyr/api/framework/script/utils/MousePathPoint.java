package org.valkyr.api.framework.script.utils;

import java.awt.*;

// Thanks Swizzbeat!

public class MousePathPoint extends Point {

    private long finishTime;
    private double lastingTime;
    private int alpha = 255;

    public MousePathPoint(int x, int y, int lastingTime) {
        super(x, y);
        this.lastingTime = lastingTime;
        finishTime = System.currentTimeMillis() + lastingTime;
    }

    //added by Swizzbeat
    public int getAlpha() {
        int newAlpha = ((int) ((finishTime - System.currentTimeMillis()) / (lastingTime / alpha)));
        if (newAlpha > 255)
            newAlpha = 255;
        if (newAlpha < 0)
            newAlpha = 0;
        return newAlpha;
    }

    public boolean isUp() {
        return System.currentTimeMillis() >= finishTime;
    }

}