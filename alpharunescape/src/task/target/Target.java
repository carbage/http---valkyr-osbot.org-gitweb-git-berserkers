package alpharunescape.src.task.target;

import javax.swing.*;

public abstract class Target {

    public abstract boolean isMet();

    public JPanel getSettings() {
        JPanel panel = new JPanel();
        initSettings(panel);
        return panel;
    }

    protected abstract void initSettings(JPanel panel);
}
