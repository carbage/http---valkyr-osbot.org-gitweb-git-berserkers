package org.valkyr.api.framework.webwalker.web;

import org.osbot.rs07.api.map.Position;

import java.util.ArrayList;

public class WebNode {

    private String name;
    private ArrayList<String> actions = new ArrayList<>();
    private short x, y, z;
    private ArrayList<WebNode> edges = new ArrayList<>();

    transient private int distance = Integer.MAX_VALUE;
    transient private WebNode previous = null;

    public WebNode() {
    }

    public WebNode(WebNode node) {
        this.x = node.getX();
        this.y = node.getY();
        this.z = node.getZ();
        this.name = node.getName();
        this.actions = node.getInteractOptions();
        this.edges = node.getEdges();
        this.distance = node.getDistance();
        this.previous = node.getPrevious();
    }

    public WebNode(Position p, String name, String[] interactOptions) {
        this.x = (short) p.getX();
        this.y = (short) p.getY();
        this.z = (short) p.getZ();
        this.name = name;
        if (interactOptions != null)
            for (String s : interactOptions)
                this.actions.add(s);
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getZ() {
        return z;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public ArrayList<String> getInteractOptions() {
        return actions;
    }

    public boolean hasInteractOption(String option) {
        for (String s : actions)
            if (s != null && s.equalsIgnoreCase(option))
                return true;
        return false;
    }

    public void addEdge(WebNode n) {
        if (n != null && !edges.contains(n)) {
            edges.add(n);
        }
    }

    public ArrayList<WebNode> getEdges() {
        return edges;
    }

    public short distance(WebNode v3d) {
        return distance(v3d.construct());
    }

    public short distance(Position p) {
        return (short) new Position(x, y, z).distance(new Position(p.getX(), p.getY(), p.getZ()));
    }

    public Position construct() {
        return new Position(x, y, z);
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setPrevious(WebNode v) {
        previous = v;
    }

    public WebNode getPrevious() {
        return previous;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WebNode) {
            WebNode v = (WebNode) o;
            return getX() == v.getX()
                    && getY() == v.getY()
                    && getZ() == v.getZ();
        } else if (o instanceof Position) {
            Position p = (Position) o;
            return getX() == p.getX()
                    && getY() == p.getY()
                    && getZ() == p.getZ();
        }
        return o.equals(this);
    }

    @Override
    public String toString() {
        return String.valueOf((name.length() > 0 ? name + " " : "") + String.valueOf(getX()) + ":" + String.valueOf(getY()) + ":" + String.valueOf(getZ()));
    }

    public boolean hasAction(String... actions) {
        if (getInteractOptions() != null)
            for (String action : actions)
                for (String s : getInteractOptions())
                    if (action != null && s != null)
                        if (action.equalsIgnoreCase(s))
                            return true;
        return false;
    }
}
