package alpharunescape.src.task;

import alpharunescape.src.AlphaRunescape;
import org.osbot.rs07.api.ui.Skill;

import javax.swing.*;

public abstract class SubTask extends Task {

    private Task task;

    public SubTask(Task task, String name, Skill[] skills) {
        super((AlphaRunescape) task.getScript(), name, skills);
        this.task = task;
    }

    @Override
    public boolean init() {
        return true;
    }

    protected abstract boolean validate();

    @Override
    public void initSettings(JPanel panel) {

    }

    @Override
    public String getStatus() {
        return null;
    }

    public Task getTask() {
        return task;
    }
}
