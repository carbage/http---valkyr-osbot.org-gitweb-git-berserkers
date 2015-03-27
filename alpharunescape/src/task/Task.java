package alpharunescape.src.task;

import alpharunescape.src.AlphaRunescape;
import alpharunescape.src.task.target.Target;
import alpharunescape.src.task.target.Targets;
import alpharunescape.src.utils.LabelUpdater;
import org.osbot.rs07.api.ui.Skill;
import org.valkyr.api.framework.gui.utils.Returnable;
import org.valkyr.api.framework.script.LoopScript;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public abstract class Task {

    private final AlphaRunescape script;
    private final String name;
    private Skill[] skills;

    private Target target;

    final JPanel panel = new JPanel();
    final JCheckBox stealthMode = new JCheckBox("Stealth Mode");
    final JComboBox<Targets> targets = new JComboBox(Targets.values());
    final JPanel targetPanel = new JPanel();

    private LinkedList<SubTask> subTasks = new LinkedList<>();

    private SubTask currentSubtask = null;

    public Task(AlphaRunescape script, String name, Skill[] skills) {
        this.script = script;
        this.name = name;
        this.skills = skills;
        init();
    }

    public abstract boolean init();

    public int process() {
        for (SubTask s : subTasks) {
            if (s.validate()) {
                currentSubtask = s;
                getScript().log(s.getClass().getSimpleName());
                return s.process();
            }
        }
        if (target.isMet())
            script.getScheduler().removeTask(this);
        else execute();
        return 0;
    }

    public void addSubTask(SubTask s) {
        if (s != null && !subTasks.contains(s))
            subTasks.add(s);
    }

    public void execute() {

    }

    public LoopScript getScript() {
        return script;
    }

    public String getName() {
        if (name != null)
            return name;
        return "";
    }

    public Skill[] getSkills() {
        return skills;
    }

    public JPanel getSettingsPanel() {
        stealthMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getScript().setStealthMode(stealthMode.isSelected());
            }
        });
        targets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(true);
            }
        });
        panel.add(targets);
        panel.add(targetPanel);
        panel.add(stealthMode);
        initSettings(panel);
        update(false);
        return panel;
    }

    private void update(boolean refresh) {
        target = ((Targets) targets.getSelectedItem()).getTarget(this);
        targetPanel.removeAll();
        for (Component c : target.getSettings().getComponents())
            targetPanel.add(c);
        if (refresh)
            script.getGui().updateSettings();
    }

    public abstract void initSettings(JPanel panel);

    public JButton getRemoveButton() {
        JButton remove = new JButton("Remove");
        final Task task = this;
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                script.getScheduler().removeTask(task);
                script.getGui().refreshTasks();
            }
        });
        return remove;
    }

    public JPanel getTaskPanel() {
        JPanel panel = new JPanel();
        JLabel nameLabel = new JLabel(getName());
        JLabel statusLabel = new JLabel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new FlowLayout());
        new Thread(new LabelUpdater(statusLabel, new Returnable<String>() {
            @Override
            public String get() {
                if (getTarget() != null)
                    return "Status: " + (getTarget().isMet() ? "Complete" : script.getScheduler().indexOfTask(Task.this) == 0 ? getStatus() : "Idle");
                return "No target!";
            }
        }, LoopScript.INTERVAL)).start();

        panel.add(nameLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(statusLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(getRemoveButton());
        return panel;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    public String getStatus() {
        if (currentSubtask != null)
            return currentSubtask.getClass().getSimpleName();
        else return "Idle";
    }

    public boolean getStealthMode() {
        return stealthMode.isSelected();
    }
}

