package org.valkyr.api.framework.webwalker.web;

import org.osbot.rs07.api.Map;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;

import java.io.*;
import java.util.ArrayList;


public class Web {

    public static Web instance;

    public static final int DISTANCE = 4;
    private ArrayList<WebNode> nodes = new ArrayList<>();
    private ArrayList<WebNode> banks = new ArrayList<>();
    private ArrayList<WebNode> obstacles = new ArrayList<>();
    private ArrayList<WebNode> resources = new ArrayList<>();
    private ArrayList<WebNode> npcs = new ArrayList<>();
    private ArrayList<WebNode> grounditems = new ArrayList<>();

    public ArrayList<WebNode> getNodes() {
        return nodes;
    }

    public ArrayList<WebNode> getAllNodes() {
        ArrayList<WebNode> vertices = new ArrayList<>();
        vertices.addAll(nodes);
        vertices.addAll(banks);
        vertices.addAll(obstacles);
        vertices.addAll(resources);
        vertices.addAll(npcs);
        vertices.addAll(grounditems);
        return vertices;
    }

    public ArrayList<WebNode> getBanks() {
        return banks;
    }

    public ArrayList<WebNode> getObstacles() {
        return obstacles;
    }

    public ArrayList<WebNode> getResources() {
        return resources;
    }

    public ArrayList<WebNode> getNpcs() {
        return npcs;
    }

    public ArrayList<WebNode> getGroundItems() {
        return grounditems;
    }

    public Web() {
    }

    public WebNode addNode(WebNode v, ArrayList<WebNode> list) {
        if (list == null || v == null)
            return null;
        if (!list.contains(v))
            list.add(v);
        return v;
    }

    public void addEdge(WebNode v1, WebNode v2) {
        if (v1 == null || v2 == null || v1.equals(v2))
            return;
        if (!v1.getEdges().contains(v2))
            v1.addEdge(v2);
        if (!v2.getEdges().contains(v1))
            v2.addEdge(v1);
    }

    public WebNode add(Position p, Entity e, Map map) {
        int[][] flags = map.getRegion().getClippingPlanes()[p.getZ()].getTileFlags();
        int flag = flags[p.getX() - map.getBaseX()][p.getY() - map.getBaseY()];
        int dist = Web.DISTANCE;
        if (e != null) {
            return addNew(new WebNode(p, e.getName(), e.getActions()));
        }
        WebNode closest = closestNode(getNodes(), p);
        if (closest == null || p.distance(closest.construct()) > dist)
            if (flag <= 160)
                return addNew(new WebNode(p, "", new String[]{}));
        return null;
    }

    public WebNode addNew(WebNode n) {
        ArrayList<WebNode> targetSet = nodes;
        if (n.getName().length() > 0) {
            if (n.hasAction("Bank", "Deposit"))
                targetSet = banks;
            if (n.hasAction("Open", "Close", "Use", "Enter", "Climb", "Climb-up", "Climb-down", "Climb-into", "Cross", "Jump-over", "Slash", "Pass-through"))
                targetSet = obstacles;
            if (n.hasAction("Mine", "Chop down", "Chop", "Net", "Lure", "Bait", "Cage", "Harpoon", "Pick", "Use", "Search for traps", "Steal", "Pick Lock")
                    || n.getName().equalsIgnoreCase("Anvil")
                    || n.getName().equalsIgnoreCase("Allotment")
                    || n.getName().equalsIgnoreCase("Tree patch")
                    || n.getName().equalsIgnoreCase("Mysterious ruins")
                    || n.getName().equalsIgnoreCase("Stove")
                    || n.getName().equalsIgnoreCase("Range"))
                targetSet = resources;
            if (n.hasAction("Talk-to", "Attack"))
                targetSet = npcs;
            if (n.hasAction("Take"))
                targetSet = grounditems;
            if (targetSet.equals(nodes))
                return null;
        }
        if (!targetSet.contains(n))
            return addNode(n, targetSet);
        WebNode node = null;
        for (WebNode nde : targetSet)
            if (nde.equals(n))
                node = nde;
        return node;
    }

    public static WebNode closestNode(ArrayList<WebNode> set, Position p) {
        WebNode v = null;
        for (WebNode vtx : set)
            if (p != null && vtx != null && p.getZ() == vtx.getZ())
                if (v == null || vtx.distance(p) < v.distance(p)) {
                    v = vtx;
                }
        return v;
    }

    public boolean isConnected(WebNode v1, WebNode v2) {
        return v1.getEdges().contains(v2) || v2.getEdges().contains(v1);
    }

    public static Web get() {
        return instance != null ? instance : (instance = read());
    }

    public static Web read() {
        return TextIO.readFile();
    }

    public static void write(Web web) {
        TextIO.writeFile(web);
    }
}


