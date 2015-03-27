package alpharunescape.src.task.target;

import alpharunescape.src.task.Task;
import alpharunescape.src.task.target.targets.CollectionTarget;
import alpharunescape.src.task.target.targets.SkillTarget;

public enum Targets {

    SKILL_TARGET("Skill target"),
    RESOURCE_TARGET("Resource target");
    private final String name;

    Targets(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public Target getTarget(Task task) {
        switch (this) {
            case SKILL_TARGET:
                return new SkillTarget(task);
            case RESOURCE_TARGET:
                return new CollectionTarget(task);
        }
        return null;
    }
}
