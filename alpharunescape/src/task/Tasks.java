package alpharunescape.src.task;

import alpharunescape.src.task.tasks.combat.CombatTask;
import alpharunescape.src.task.tasks.fishing.FishingTask;
import org.osbot.rs07.api.ui.Skill;
import alpharunescape.src.AlphaRunescape;
import alpharunescape.src.task.tasks.woodcutting.WoodcuttingTask;

public enum Tasks {
    WOODCUTTING("Woodcutting", Skill.WOODCUTTING),
    FISHING("Fishing", Skill.FISHING),
    //COOKING("Cooking", Skill.COOKING),
    //MINING("Mining", Skill.MINING),
    //SMITHING("Smithing", Skill.SMITHING),
    //CRAFTING("Crafting", Skill.CRAFTING),
    //RUNECRAFTING("Runecrafting", Skill.RUNECRAFTING),
    COMBAT("Combat", Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.HITPOINTS);

    private final String name;
    private final Skill[] skills;

    Tasks(String name, Skill... skills) {
        this.name = name;
        this.skills = skills;
    }

    public Task getTask(AlphaRunescape script) {
        switch (this) {
            case WOODCUTTING:
                return new WoodcuttingTask(script, name, skills);
            case FISHING:
                return new FishingTask(script, name, skills);
            case COMBAT:
                return new CombatTask(script, name, skills);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
