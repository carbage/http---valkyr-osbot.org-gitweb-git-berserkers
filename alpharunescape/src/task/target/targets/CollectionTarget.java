package alpharunescape.src.task.target.targets;

import alpharunescape.src.task.Task;
import alpharunescape.src.task.target.Target;

import javax.swing.*;

public class CollectionTarget extends Target {

    private Task task;
    private JLabel lblQuantity = new JLabel("Stop after collecting");
    private JTextField quantity = new JTextField(16);

    public CollectionTarget(Task task) {
        this.task = task;
    }

    @Override
    public boolean isMet() {
        return quantity.getText() != null && task.getScript().getLootTracker().getLoot().size() >= Integer.parseInt(quantity.getText());
    }

    @Override
    protected void initSettings(JPanel panel) {
        panel.add(lblQuantity);
        panel.add(quantity);
    }
}