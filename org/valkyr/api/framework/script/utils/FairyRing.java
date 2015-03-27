package org.valkyr.api.framework.script.utils;

import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;

public class FairyRing {

    public static final String[] LETTERS = {"a", "d", "c", "b", "i", "l", "k", "j", "p", "s", "r", "q"};

    private MethodProvider ctx;
    public FairyRing(MethodProvider ctx) {
        this.ctx = ctx;
    }

    public boolean setRing(String combination) {
        if (ctx.widgets.isVisible(398)) {
            for (int i = 0; i < 3; i++) {
                long start = System.currentTimeMillis();
                int value = getValue(""+combination.charAt(i));
                while (getRowConfig(i, ctx) != value && System.currentTimeMillis() - start <= 4000) {
                    RS2Widget w = ctx.widgets.get(398, 19 + (i * 2));
                    if (w != null) {
                        if (w.interact("Rotate clockwise")) {
                            try {
                                MethodProvider.sleep(MethodProvider.random(1000, 1250));
                            } catch (InterruptedException e) {}
                        }
                    }
                }
            }
            if (isValid(combination, ctx)) {
                return clickTeleport(ctx);
            }
            return false;
        }
        return false;
    }

    private boolean clickTeleport(MethodProvider ctx) {
        RS2Widget w = ctx.widgets.get(398, 26);
        if (w != null && w.isVisible()) {
            return w.interact("Confirm");
        }
        return false;
    }

    public boolean isInterfaceOpen(MethodProvider ctx) {
        return ctx.widgets.isVisible(398);
    }

    public boolean isValid(String combination, MethodProvider ctx) {
        for (int i = 0; i < 3; i++) {
            int value = getValue(""+combination.charAt(i));
            if (getRowConfig(i, ctx) != value)
                return false;
        }
        return true;
    }

    public int getValue(String letter) {
        for (int i = 0; i < LETTERS.length; i++) {
            if (letter.toLowerCase().equals(LETTERS[i])) {
                return i % 4;
            }
        }
        return -1;
    }

    public int getRowConfig(int row, MethodProvider ctx) {
        switch(row) {
            case 0:
                return ctx.configs.get(816) & 0x3;
            case 1:
                return (ctx.configs.get(816) >> 2) & 0x3;
            case 2:
                return (ctx.configs.get(816) >> 4) & 0x3;
        }
        return 0;
    }

}

