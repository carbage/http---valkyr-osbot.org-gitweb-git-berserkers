package org.valkyr.api.framework.utils;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.util.DynamicCircularPathFinder;
import org.osbot.rs07.input.mouse.MiniMapTileDestination;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.valkyr.api.framework.script.LoopScript;
import org.valkyr.api.framework.webwalker.web.WebNode;

import java.util.ArrayList;
import java.util.LinkedList;

public class Walker {

    private final LoopScript script;

    public Walker(LoopScript script) {
        this.script = script;
    }

    public boolean walkPath(ArrayList<WebNode> path, int dist, int skipDist) {
        WebNode next = nextTile(path, 16);
        WebNode lastNode = path.get(path.size() - 1);
        if (next != null && noTeleportNeeded(lastNode) && noObstacleBlocking(path, next, lastNode, dist))
            if (!script.myPlayer().isMoving() || next.distance(script.myPosition()) < skipDist) {
                boolean b = clickMiniMapPosition(next.construct());
                script.getAbhelper().waitInteractionDelay();
                return b;
            }
        return lastNode != null && lastNode.getZ() == script.myPosition().getZ()
                && script.map.distance(lastNode.construct()) < dist;
    }

    public boolean noTeleportNeeded(WebNode last) {
        Item i = null;
        if (script.getTeleport() != null && last.distance(script.getTeleport().getDestination()) < last.distance(script.myPosition())) {
            if (script.getTeleport().getName().contains("tab")) {
                i = script.getInventory().getItem(script.getTeleport().getName().replace(" tab", ""));
                if (i != null)
                    i.interact("Break");
            }
        }
        return i == null;
    }

    public boolean noObstacleBlocking(ArrayList<WebNode> path, WebNode next, WebNode last, int skipDist) {
        WebNode nextObject = getNextObjectIn(path);
        if (script.getWebWalker().getWeb().getObstacles().contains(next))
            nextObject = next;
        RS2Object entity = getClosestEntity(nextObject);
        String option = "";
        if (entity != null && isBlocking(nextObject, next) && entity.getPosition().isOnMiniMap(script.getBot())) {
            for (String s : entity.getActions())
                if (s != null) {
                    option = s;
                    break;
                }
            if (entity.getName().equals("Staircase") || entity.getName().equals("Stairs") || entity.getName().equals("Ladder"))
                if ((last.getZ() != script.myPosition().getZ() || next.getZ() != script.myPosition().getZ())) {
                    if (last.getZ() > script.myPosition().getZ() || next.getZ() > script.myPosition().getZ())
                        option = "Climb-up";
                    else if (last.getZ() < script.myPosition().getZ() || next.getZ() < script.myPosition().getZ())
                        option = "Climb-down";
                } else return true;
            if (option.equals("") || option.equals("Close"))
                return true;
            if (!script.myPlayer().isMoving()) {
                if (script.myPosition().distance(entity.getPosition()) > skipDist)
                    clickMiniMapPosition(entity.getPosition());
                else {
                    if (entity.isVisible())
                        entity.interact(option);
                    else script.getCamera().toEntity(entity);
                    script.getAbhelper().waitInteractionDelay();
                }
            }
            return false;
        }
        return true;
    }

    public boolean isBlocking(WebNode obstacle, WebNode next) {
        LinkedList<Position> path = new DynamicCircularPathFinder(script.getBot()).construct(next.construct());
        if (path != null) {
            for (Position p : path)
                if (!next.equals(obstacle) && obstacle.equals(p))
                    return true;
        } else return true;
        return false;
    }

    private RS2Object getClosestEntity(WebNode n) {
        RS2Object closest = null;
        for (RS2Object e : script.getObjects().getAll())
            if (e != null && n != null)
                if (e.exists() && e.getName().equals(n.getName()))
                    if (closest == null || e.getPosition().distance(n.construct()) < closest.getPosition().distance(n.construct()))
                        closest = e;
        return closest;
    }

    public WebNode getNextObjectIn(ArrayList<WebNode> path) {
        for (WebNode node : path) {
            if (node.getZ() == script.myPosition().getZ()) {
                if (script.getWebWalker().getWeb().getObstacles().contains(node))
                    return node;
            }
        }
        return null;
    }

    public WebNode nextTile(ArrayList<WebNode> path, int skipDist) {
        /*WebNode next = null;
        for (WebNode node : path) {
            if (next == null || (node.distance(script.myPosition()) < next.distance(script.myPosition()) && node.distance(script.myPosition()) > skipDist))
                if (!node.equals(script.myPosition()))
                    next = node;
        }
        return next;*/
        int dist = -1, closest = -1;
        for (int i = path.size() - 1; i >= 0; i--) {
            WebNode tile = path.get(i);
            int d = script.map.distance(tile.construct());
            if (d < dist || dist == -1) {
                dist = d;
                closest = i;
            }
        }

        int feasibleTileIndex = -1;

        for (int i = closest; i < path.size(); i++) {
            if (script.map.distance(path.get(i).construct()) <= skipDist) {
                if (!path.get(i).equals(script.myPosition()))
                    feasibleTileIndex = i;
            } else {
                break;
            }
        }

        return (feasibleTileIndex == -1) ? null : path.get(feasibleTileIndex);
    }


    private boolean clickMiniMapPosition(Position position) {
        return script.mouse.click(new RectangleDestination(script.getBot(), new MiniMapTileDestination(script.getBot(), position).getBoundingBox()));
    }
}
