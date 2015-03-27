package org.valkyr.api.framework.gui;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.valkyr.api.framework.gui.utils.DataTableModel;
import org.valkyr.api.framework.gui.utils.Returnable;
import org.valkyr.api.framework.gui.utils.TableUpdater;
import org.valkyr.api.framework.script.LoopScript;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Vector;

public class GuiTab extends JScrollPane implements ChangeListener {

    public final JTextArea output = new JTextArea();
    private final DataTableModel model = new DataTableModel();
    private final JTable table;
    private final Vector<TableUpdater> updaters = new Vector<>();
    private LoopScript script;

    public GuiTab(LoopScript script) {
        if (script != null) {
            this.script = script;
        }
        table = new JTable(model) {
            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };
        setViewportView(table);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane p = ((JTabbedPane) getParent());
        if (p != null) {
            Component c = p.getSelectedComponent();
            if (c != null && c.equals(this)) {
                for (TableUpdater updater : updaters) {
                    new Thread(updater).start();
                }
            }
        }
    }

    public DataTableModel getTableModel() {
        return model;
    }

    public <T> void addRow(String label, long interval, Returnable<T> returnable) {
        model.getLabels().add(label);
        model.getValues().add(returnable);
        int rowIndex = model.getRowCount() - 1;
        TableUpdater updater = new TableUpdater(rowIndex, 1, interval, this);
        updaters.add(updater);
        new Thread(updater).start();
    }

    public void addSkillTracker(final Skill skill) {
        Returnable ret = new Returnable<String>() {
            @Override
            public String get() {
                if (script.getClient().isLoggedIn())
                    return script.getExperienceTracker().getGainedXP(skill) + " (" + script.getExperienceTracker().getGainedLevels(skill) + " levels)  | To level " + (script.getSkills().getStatic(skill) + 1) + ": " + (script.getSkills().getExperienceForLevel((script.getSkills().getStatic(skill) + 1)) - script.getSkills().getExperience(skill));
                return "";
            }
        };
        addRow(skill.name().substring(0, 1) + skill.name().substring(1).toLowerCase() + " exprience gained", LoopScript.INTERVAL, ret);
    }

    public Script getScript() {
        return script;
    }

    public String getTabName() {
        return script.getName() + " @ Bot " + script.getIndex();
    }

    public JTable getTable() {
        return table;
    }

}
