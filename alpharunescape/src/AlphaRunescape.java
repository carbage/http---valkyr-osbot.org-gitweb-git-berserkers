package alpharunescape.src;

import alpharunescape.src.task.util.TaskSheduler;
import org.osbot.rs07.script.ScriptManifest;
import org.valkyr.api.framework.script.BasicLoopScript;
import org.valkyr.api.framework.webwalker.web.Web;

import java.awt.*;

@ScriptManifest(author = "Valkyr & Czar", name = "Alpha Runescape", info = "", version = 1.0, logo = "")
public class AlphaRunescape extends BasicLoopScript {
    private TaskSheduler scheduler;
    private AlphaRunescapeGui gui;

    @Override
    public boolean start() {
        try {
            Web.get();
            scheduler = new TaskSheduler();
            gui = new AlphaRunescapeGui(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public int process() {
        if (scheduler != null)
            scheduler.process();
        return 0;
    }

    @Override
    public void paint(Graphics g) {
        int mX = (int) mouse.getPosition().getX();
        int mY = (int) mouse.getPosition().getY();
        g.drawLine(mX - 5, mY - 5, mX + 5, mY + 5);
        g.drawLine(mX - 5, mY + 5, mX + 5, mY - 5);
    }

    @Override
    public void finish() {
        gui.dispose();
    }

    public TaskSheduler getScheduler() {
        return scheduler;
    }

    public AlphaRunescapeGui getGui() {
        return gui;
    }
}
