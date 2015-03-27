package org.valkyr.api.framework.gui;

import org.osbot.rs07.script.Script;
import org.valkyr.api.framework.script.LoopScript;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Gui extends JFrame {

    private static final int PADDING = 5;
    public static Gui instance;
    private final JPanel container = new JPanel();
    private final JTabbedPane tabs = new JTabbedPane();
    private final Map<Script, GuiTab> tabMap = new HashMap<Script, GuiTab>();

    public Gui() {
        setTitle("ValkyrScripts GUI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        setContentPane(container);

        getContentPane().add(tabs);

        pack();
        setVisible(true);
    }

    public static Gui get() {
        if (instance == null) {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        instance = new Gui();
                    }
                });
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!instance.isVisible())
            instance.setVisible(true);
        return instance;
    }

    public void addTab(final LoopScript script) {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    GuiTab newTab = new GuiTab(script);
                    tabMap.put(script, newTab);
                    tabs.add(newTab.getTabName(), newTab);
                    tabs.addChangeListener(newTab);
                    pack();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTab(final Script script) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                tabs.remove(tabMap.get(script));
                tabMap.remove(script);
                if (tabMap.size() <= 0) {
                    tabs.removeAll();
                    instance.dispose();
                }
            }
        });
    }

    public GuiTab getTab(Script script) {
        return tabMap.get(script);
    }

    public Map getTabs() {
        return tabMap;
    }
}