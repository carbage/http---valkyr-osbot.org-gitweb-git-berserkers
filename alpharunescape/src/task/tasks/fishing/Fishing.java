package alpharunescape.src.task.tasks.fishing;

import org.osbot.rs07.api.ui.Skill;
import alpharunescape.src.task.SubTask;
import alpharunescape.src.task.Task;

public class Fishing extends SubTask {

    FishingTask task;

    public Fishing(Task task, String name, Skill[] skills) {
        super(task, name, skills);
        this.task = (FishingTask) task;
    }

    @Override
    protected boolean validate() {
        return !getScript().getWidgets().getInventory().isFull() && getScript().getInventory().contains(task.getFishingSpot().getEquipment());
    }

    @Override
    public void execute() {
        if (getScript().myPlayer().getInteracting() == null || (getScript().myPlayer().getInteracting() != null && !getScript().myPlayer().isAnimating()))
            getScript().getWebWalker().interactWith(task.fishingSpotLocation, task.getFishingSpot().getOption(), getScript().getStealthMode());
    }
}
