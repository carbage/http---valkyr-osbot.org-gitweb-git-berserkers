package alpharunescape.src.task.tasks.fishing;

import org.osbot.rs07.api.ui.Skill;
import org.valkyr.api.enums.FishingSpot;
import org.valkyr.api.framework.webwalker.web.WebNode;
import alpharunescape.src.AlphaRunescape;
import alpharunescape.src.task.Task;

import javax.swing.*;
import java.awt.*;
public class FishingTask extends Task {

    protected WebNode fishingSpotLocation;
    protected WebNode bankLocation;
    JLabel spotsLabel;
    JComboBox<FishingSpot> spots;
    JCheckBox banking;

    public FishingTask(AlphaRunescape script, String name, Skill[] skills) {
        super(script, name, skills);
    }

    @Override
    public boolean init() {
        addSubTask(new Fishing(this, getName(), getSkills()));
        addSubTask(new Banking(this, getName(), getSkills()));
        addSubTask(new Dropping(this, getName(), getSkills()));
        return true;
    }

    @Override
    public void initSettings(JPanel panel) {
        spotsLabel = new JLabel("Fishing method:");
        spotsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        spots = new JComboBox<>(FishingSpot.values());

        banking = new JCheckBox("Banking?", false);

        panel.add(spotsLabel);
        panel.add(spots);
        panel.add(banking);
    }

    public boolean getBanking() {
        return banking != null && banking.isSelected();
    }

    public FishingSpot getFishingSpot() {
        return (FishingSpot) spots.getSelectedItem();
    }
}
