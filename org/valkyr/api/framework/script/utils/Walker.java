package org.valkyr.api.framework.script.utils;

import org.osbot.rs07.api.LocalWalker;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2InterfaceChild;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.input.mouse.EntityDestination;
import org.osbot.rs07.input.mouse.InterfaceDestination;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.valkyr.api.framework.script.LoopScript;
import org.valkyr.api.framework.script.LoopScript;
import org.valkyr.api.framework.webwalker.web.WebNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Walker {

    private LoopScript script;

    private int ifaceIndex, total;

    public Walker(LoopScript script) {
        this.script = script;
    }

    private boolean walkTo(Position p) {
        if (p != null) {
            script.getAbhelper().activateRun();
            script.getAbhelper().waitInteractionDelay();
            if (p.isOnMiniMap(script.getBot())) {
                return script.getLocalWalker().walk(p);
            }
        }
        return false;
    }

    public boolean walkPath(ArrayList<WebNode> path, int dist) {
        WebNode next = null;
        while (script.myPosition().distance(path.get(path.size() - 1).construct()) > dist) {
            for (WebNode p : path) {
                if (!p.equals(script.myPosition())
                        && p.construct().isOnMiniMap(script.getBot())
                        && p.distance(script.myPosition()) < path.get(path.size() - 1).distance(script.myPosition())
                        && (next == null || p.distance(script.myPosition()) < next.distance(script.myPosition()))) {
                    next = p;
                }
            }
            if (next != null) {
                if (script.getWebWalker().getWeb().getObstacles().contains(next)) {
                    Entity obstacle = getEntityAt(next.construct());
                    if (obstacle != null) {
                        script.getMouse().click(new EntityDestination(script.getBot(), obstacle));
                    }
                } else {
                    //script.getMouse().click(new MiniMapTileDestination(script.getBot(), next.construct()));
                    script.getLocalWalker().walk(next.construct());
                }
                script.getAbhelper().waitInteractionDelay();
            } else return false;
        }
        return true;
    }

    public Entity getEntityAt(Position p) {
        for (RS2Object e : script.getObjects().getAll())
            if (e != null)
                if (e.getPosition().equals(p))
                    return e;
        return null;
    }

    private boolean selectMinigame(String teleportName) throws InterruptedException {
        List<RS2InterfaceChild> ifaces = script.getInterfaces().containingText(teleportName);
        if (!ifaces.isEmpty() && validateTeleportIface(ifaces, teleportName)) {
            RS2InterfaceChild child = loadTeleportChild(teleportName);
            if (child != null && ifaceIndex != -1 && total != -1) {
                Rectangle rect = child.getRectangle();
                rect.translate(0, (total - ifaceIndex - 1) * -child.getHeight());
                RectangleDestination dest = new RectangleDestination(script.getBot(), rect);
                return script.getMouse().click(dest);
            }
        } else {
            RS2InterfaceChild c = script.interfaces.getChild(76, 6);
            if (c != null && c.isVisible()) {
                InterfaceDestination dest = new InterfaceDestination(script.getBot(), c);
                script.getMouse().click(dest);
            }
        }
        return false;
    }

    private RS2InterfaceChild loadTeleportChild(String teleportName) {
        RS2InterfaceChild pFace = script.getInterfaces().getChild(76, 19);
        RS2InterfaceChild cFace = null;
        if (pFace != null && pFace.isVisible()) {
            RS2InterfaceChild[] children = pFace.getChildren();
            if (children.length > 0) {
                for (int i = children.length - 1; i >= 0; i--) {
                    RS2InterfaceChild c = children[i];
                    if (c.getMessage().contains(teleportName)) {
                        ifaceIndex = i;
                        total = children.length;
                        cFace = c;
                    }
                }
            }
        }
        return cFace;
    }

    private boolean validateTeleportIface(List<RS2InterfaceChild> children, String teleportName) {
        for (RS2InterfaceChild child : children) {
            if (child.getMessage().contains(teleportName)
                    && !child.getMessage().contains("currently talking")) {
                return true;
            }
        }
        return false;
    }
}