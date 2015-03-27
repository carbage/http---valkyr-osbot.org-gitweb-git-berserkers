package alpharunescape.src.task.tasks.fishing;

import org.osbot.rs07.api.ui.Skill;
import alpharunescape.src.task.SubTask;
import alpharunescape.src.task.Task;

public class Banking extends SubTask {
    public Banking(Task task, String name, Skill[] skills) {
        super(task, name, skills);
    }

    @Override
    protected boolean validate() {
        return getScript().widgets.getInventory().isFull() && ((FishingTask) getTask()).getBanking();
    }

    @Override
    public void execute() {
        if (getScript().getWebWalker().interactWith(((FishingTask) getTask()).bankLocation, "Bank", false)) {
            getScript().getWidgets().getBank().depositAllExcept(((FishingTask) getTask()).getFishingSpot().getEquipment());
        }
    }
}
