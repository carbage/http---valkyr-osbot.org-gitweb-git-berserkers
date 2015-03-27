package org.valkyr.api.framework.webwalker.web;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;

import java.util.ArrayList;
import java.util.Collections;

public class Pathfinder {
    private Script script;
    private Web web;

    public Pathfinder(Script script, Web web) {
        this.script = script;
        this.web = web;
    }

    public ArrayList<WebNode> findAStarPathTo(WebNode finish) {
        long startTime = System.currentTimeMillis();
        WebNode start = getClosest(web.getAllNodes(), script.myPosition());
        if (start == null || finish == null)
            return null;
        if (start.equals(finish))
            return new ArrayList<>();
        ArrayList<WebNode> open = new ArrayList<>();
        ArrayList<WebNode> closed = new ArrayList<>();
        //script.log("Finding path to " + finish.toString());
        start.setDistance(calcManhattanDistance(start, finish));
        start.setPrevious(null);
        open.add(start);
        while (!open.isEmpty()) {
            WebNode u = open.get(0);
            open.remove(0);
            closed.add(u);
            if (u.equals(finish)) {
                ArrayList<WebNode> path = new ArrayList<>();
                while (u != null) {
                    path.add(u);
                    u = u.getPrevious();
                }
                //script.log("Found path to " + finish.toString() + "(" + path.size() + " nodes) in " + (System.currentTimeMillis() - startTime) + "ms");
                Collections.reverse(path);
                return path;
            }
            for (WebNode neighbor : getAdjacent(u)) {
                int g = (int) (getGScore(start, u) + calcManhattanDistance(neighbor, u));
                if (closed.contains(neighbor))
                    continue;
                if (!open.contains(neighbor) || g < getGScore(start, neighbor)) {
                    neighbor.setDistance((int) (getGScore(start, neighbor) + calcManhattanDistance(neighbor, finish)));
                    neighbor.setPrevious(u);
                    if (!open.contains(neighbor))
                        open.add(neighbor);
                }
            }
        }
        script.log("Could not find path to " + finish.toString());
        return null;
    }

    public WebNode getClosest(ArrayList<WebNode> set, Position p) {
        WebNode v = null;
        for (WebNode vtx : set)
            if (p != null && vtx != null && vtx.getZ() == p.getZ())
                if (v == null || vtx.distance(p) < v.distance(p)) {
                    v = vtx;
                }
        return v;
    }

    public ArrayList<WebNode> getAdjacent(WebNode node) {
        return node.getEdges();
    }

    public double getGScore(WebNode start, WebNode pos) {
        if (pos != null && start != null) {
            double dx = start.getX() - pos.getX();
            double dy = start.getY() - pos.getY();
            return Math.sqrt((dx * dx) + (dy * dy));
        }
        return Double.MAX_VALUE;
    }

    private int calcManhattanDistance(WebNode current, WebNode target) {
        int dx = Math.abs(target.getX() - current.getX());
        int dy = Math.abs(target.getY() - current.getY());
        return dx + dy;
    }
}
