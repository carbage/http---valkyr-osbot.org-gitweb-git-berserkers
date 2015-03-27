package org.valkyr.api.framework.webwalker;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.NPC;
import org.valkyr.api.framework.script.LoopScript;
import org.valkyr.api.framework.utils.Walker;
import org.valkyr.api.framework.webwalker.web.Pathfinder;
import org.valkyr.api.framework.webwalker.web.Web;
import org.valkyr.api.framework.webwalker.web.WebNode;

import java.util.ArrayList;

public class WebWalker {
    private Pathfinder pathfinder;
    private LoopScript script;
    private Web web;
    private Walker walker;

    public WebWalker(LoopScript script) {
        this.script = script;
        this.web = Web.get();
        this.pathfinder = new Pathfinder(this.script, this.web);
        this.walker = new Walker(script);
        script.log("WebWalker: Loaded " + web.getNodes().size() + " walkable nodes");
        script.log("WebWalker: Loaded " + web.getObstacles().size() + " obstacles");
        script.log("WebWalker: Loaded " + web.getBanks().size() + " banks");
        script.log("WebWalker: Loaded " + web.getResources().size() + " resources");
        script.log("WebWalker: Loaded " + web.getNpcs().size() + " npcs");
        script.log("WebWalker: Loaded " + web.getGroundItems().size() + " ground items");
    }

    public boolean interactWith(WebNode n, String option, boolean useSecondClosest, int distance) {
        if (n == null || !n.hasInteractOption(option))
            return false;
        Entity e = getEntityAt(n);
        if (e == null || !e.exists())
            e = getClosestEntity(n, useSecondClosest, option);
        if (e == null || (e.exists() && n.distance(script.myPosition()) > distance))
            if (!walk(n, distance))
                return false;
        boolean b = false;
        if (e != null && e.exists()) {
            if (!e.isVisible())
                script.getCamera().toEntity(e);
            b = e.interact(option);
            script.getAbhelper().waitInteractionDelay();
        }
        return e != null && b;
    }

    public boolean interactWith(WebNode n, String option, boolean useSecondClosest) {
        return interactWith(n, option, useSecondClosest, 4);
    }

    public boolean goBank(boolean useSecondClosest, boolean depositBox) {
        return script.getWidgets().getBank().isOpen() || interactWith(getClosestBank(script.myPosition(), useSecondClosest, depositBox), depositBox ? "Deposit" : "Bank", useSecondClosest);
    }

    private Entity getEntityAt(Position p, String... options) {
        for (Entity e : getAllEntities())
            if (e != null && e.exists())
                if (e.getPosition().equals(p) && (e.hasAction(options) || options.length == 0))
                    return e;
        return null;
    }


    private Entity getEntityAt(WebNode w, String... options) {
        for (Entity e : getAllEntities())
            if (e != null && e.exists())
                if (w.equals(e.getPosition()) && e.getName().equals(w.getName()) && e.hasAction(options))
                    return e;
        return null;
    }

    private Entity getClosestEntity(WebNode n, boolean useSecondClosest, String... options) {
        Entity closest = null, last = null;
        boolean secondClosest = false;
        if (script.getAbhelper() != null)
            secondClosest = !script.getAbhelper().getUseClosest();
        for (Entity e : getAllEntities())
            if (e != null && e.exists() && e.getName().equals(n.getName()) && e.hasAction(options)
                    && (!(e instanceof NPC) || (((NPC) e).getHealth() > 0 && !((NPC) e).isUnderAttack())))
                if (closest == null || e.getPosition().distance(n.construct()) < closest.getPosition().distance(n.construct()))
                    if ((!e.getName().equals("Tree")) || !e.hasAction("Talk to")) {  // Draynor Tree Trap
                        last = closest;
                        closest = e;
                    }
        return useSecondClosest && secondClosest ? last : closest;
    }

    private ArrayList<Entity> getAllEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.addAll(script.getNpcs().getAll());
        entities.addAll(script.getObjects().getAll());
        entities.addAll(script.getGroundItems().getAll());
        return entities;
    }

    public WebNode add(Position p) {
        return getWeb().add(p, getEntityAt(p), script.getMap());
    }

    private WebNode getClosest(ArrayList<WebNode> set, Position p, boolean useSecondClosest, String... names) {
        WebNode v = null;
        WebNode last = null;
        boolean secondClosest = false;
        if (script.getAbhelper() != null)
            secondClosest = !script.getAbhelper().getUseClosest();
        for (WebNode vtx : set)
            if (p != null && vtx != null) {
                if (v == null || vtx.distance(p) < v.distance(p)) {
                    for (String n : names)
                        if (vtx.getName().contains(n) || vtx.getName().equalsIgnoreCase(n)) {
                            Entity e = getEntityAt(vtx, vtx.getInteractOptions().toArray(new String[vtx.getInteractOptions().size()]));
                            if (e == null || e.exists()) {
                                last = v;
                                v = vtx;
                            }
                        }
                }
            }
        return useSecondClosest && secondClosest ? last : v;
    }

    public WebNode getClosestNode(Position p, boolean useSecondClosest, String... names) {
        return getClosest(getWeb().getAllNodes(), p, useSecondClosest, names);
    }

    public WebNode getClosestBank(Position p, boolean useSecondClosest, boolean depositBox) {
        return getClosestNode(p, useSecondClosest, depositBox ? "Bank Deposit Box" : "Bank booth");
    }

    public boolean walk(WebNode v, int distance) {
        ArrayList<WebNode> p1 = findPath(v);
        return walkPath(p1, distance);
    }

    public boolean walkPath(ArrayList<WebNode> path, int distance) {
        if (path == null) return false;
        if (path.isEmpty()) return true;
        return walker.walkPath(path, distance, 10);
    }

    public ArrayList<WebNode> findPath(WebNode n) {
        return pathfinder.findAStarPathTo(n);

    }

    public Web getWeb() {
        return web;
    }

    public void storeWeb() {
        Web.write(web);
    }
}
