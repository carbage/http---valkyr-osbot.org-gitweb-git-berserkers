package alpharunescape.src.task.tasks.fishing;

import alpharunescape.src.task.SubTask;
import alpharunescape.src.task.Task;
import org.osbot.rs07.api.ui.Skill;

public class Dropping extends SubTask {
    public Dropping(Task task, String name, Skill[] skills) {
        super(task, name, skills);
    }

    @Override
    protected boolean validate() {
        return getScript().widgets.getInventory().isFull() && !((FishingTask) getTask()).getBanking();
    }

    @Override
    public void execute() {
        getScript().getWidgets().getInventory().dropAllExcept(((FishingTask) getTask()).getFishingSpot().getEquipment());
    }
}
