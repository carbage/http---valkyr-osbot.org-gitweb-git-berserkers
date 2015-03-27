package alpharunescape.src.task.util;

import alpharunescape.src.task.Task;

import java.util.LinkedList;

public class TaskSheduler {

    private LinkedList<Task> tasks = new LinkedList<>();

    public void process() {
        if (!tasks.isEmpty()) {
            Task t = tasks.peek();
            if (t.getTarget() != null && !t.getTarget().isMet())
                t.execute();
            else tasks.remove(t);
        }
    }

    public void addTask(Task t) {
        if (!tasks.contains(t))
            tasks.add(t);
    }

    public void removeTask(Task task) {
        if (task != null && tasks.contains(task))
            tasks.remove(task);
    }

    public int indexOfTask(Task task) {
        if (tasks.contains(task))
            return tasks.indexOf(task);
        return -1;

    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }
}
