package alpharunescape.src.task.target.targets;

import alpharunescape.src.task.Task;
import alpharunescape.src.task.target.Target;
import org.osbot.rs07.api.ui.Skill;

import javax.swing.*;

public class SkillTarget extends Target {

    private Task task;
    private JLabel lblTargetLevel = new JLabel("Stop at level");
    private JTextField txtFldTargetLevel = new JTextField(16);
    private JComboBox<Skill> skills;

    public SkillTarget(Task task) {
        this.task = task;
        skills = new JComboBox<>(task.getSkills());
    }

    @Override
    public boolean isMet() {
        return getSkill() != null && txtFldTargetLevel.getText().length() > 0 && task.getScript().getSkills().getStatic(getSkill()) >= Integer.parseInt(txtFldTargetLevel.getText());
    }

    @Override
    protected void initSettings(JPanel panel) {
        panel.add(skills);
        panel.add(lblTargetLevel);
        panel.add(txtFldTargetLevel);
    }

    public Skill getSkill() {
        return (Skill) skills.getSelectedItem();
    }
}
