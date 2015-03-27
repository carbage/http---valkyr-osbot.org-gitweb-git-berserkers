package org.valkyr.api.framework.script;

import org.osbot.BotApplication;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.Script;
import org.valkyr.api.enums.Teleport;
import org.valkyr.api.framework.antiban.AntibanHelper;
import org.valkyr.api.framework.antiban.AntibanNode;
import org.valkyr.api.framework.gui.Gui;
import org.valkyr.api.framework.gui.utils.Returnable;
import org.valkyr.api.framework.script.utils.MousePathPoint;
import org.valkyr.api.framework.script.utils.Timer;
import org.valkyr.api.framework.utils.LootTracker;
import org.valkyr.api.framework.utils.PriceGrab;
import org.valkyr.api.framework.webwalker.WebWalker;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @Author Josef
 */
public abstract class LoopScript extends Script {

    public static final int INTERVAL = 1000;
    private AntibanHelper abhelper;
    private LootTracker lootTracker;
    private WebWalker webWalker = null;
    private Timer timer = new Timer(0);
    private AntibanNode antibanNode;
    private boolean stealthMode;
    private boolean startedExperienceTracker = false;
    private boolean banking = false;

    public Teleport getTeleport() {
        return teleport;
    }

    public void setTeleport(Teleport teleport) {
        this.teleport = teleport;
    }

    private Teleport teleport = null;

    @Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode();
    }

    @Override
    public void onStart() {
        getAntiBan().unregisterAllBehaviors();
        this.abhelper = new AntibanHelper(this);
        this.antibanNode = new AntibanNode(this);
        this.lootTracker = new LootTracker(this);
        new Thread(lootTracker).start();
        Gui.get().addTab(this);
        Gui.get().getTab(this).addRow("Run time:", INTERVAL, new Returnable<String>() {
            @Override
            public String get() {
                return getRunTime();
            }
        });
        Gui.get().getTab(this).addRow("Gold gained:", INTERVAL * 30, new Returnable<Integer>() {
            @Override
            public Integer get() {
                return getLootTracker().getAllGained();
            }
        });
        getWebWalker().getWeb();
        if (!start()) stop();
    }

    public abstract boolean start();

    @Override
    public int onLoop() {
        if (myPlayer() != null && myPlayer().exists()) {
            getAbhelper().activateRun();
            if (antibanNode.validate())
                antibanNode.process();
            return process();
        } else return 0;
    }

    public abstract int process();

    @Override
    public void onPaint(Graphics2D g) {
        paint(g);
    }

    public void paint(Graphics g) {
    }

    @Override
    public void onExit() {
        submitStats();
        for (String name : lootTracker.getLoot().keySet()) {
            int q = lootTracker.getLoot().get(name);
            log("[LOOT] " + name + " x" + q + " (" + PriceGrab.getInstance().getPrice(name, 2) * q + ")");
        }
        finish();
        Gui.get().removeTab(this);
    }

    @Override
    public ExperienceTracker getExperienceTracker() {
        if (myPlayer().exists() && !startedExperienceTracker) {
            skills.getExperienceTracker().startAll();
            startedExperienceTracker = true;
        }
        return skills.getExperienceTracker();
    }

    public void submitStats() {
        try {
            int expGained = 0;
            int goldGained = getLootTracker().getAllGained();
            for (Skill s : Skill.values())
                expGained += getExperienceTracker().getGainedXP(s);
            if (goldGained < 0)
                goldGained = 0;
            String params = "script=" + getName() + "&name=" + BotApplication.getInstance().getOSAccount().username + "&exp=" + expGained + "&gold=" + goldGained + "&runtime=" + getTimer().getElapsed();
            URL url = new URL("https://kbve.com/valkyr/scriptstats/submit.php?" + params);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String formatTime(long s) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(s),
                TimeUnit.MILLISECONDS.toMinutes(s) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(s) % TimeUnit.MINUTES.toSeconds(1));
    }

    public abstract void finish();

    public int getIndex() {
        return getBot().getId();
    }

    public String getRunTime() {
        return timer.toElapsedString();
    }

    public Timer getTimer() {
        return timer;
    }

    public AntibanHelper getAbhelper() {
        return abhelper;
    }

    public LootTracker getLootTracker() {
        return lootTracker;
    }

    public WebWalker getWebWalker() {
        return webWalker == null ? webWalker = new WebWalker(this) : webWalker;
    }

    public ArrayList<Entity> getAllEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.addAll(getNpcs().getAll());
        entities.addAll(getObjects().getAll());
        entities.addAll(getGroundItems().getAll());
        return entities;
    }

    public boolean getStealthMode() {
        return stealthMode;
    }

    public void setStealthMode(boolean b) {
        stealthMode = b;
    }


    public boolean getBanking() {
        return banking;
    }

    public void setBanking(boolean b) {
        banking = b;
    }
}
