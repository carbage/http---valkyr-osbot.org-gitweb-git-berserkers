package alpharunescape.src.task.tasks.woodcutting;

import alpharunescape.src.task.Task;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Skill;
import org.valkyr.api.enums.Axe;
import org.valkyr.api.enums.Tree;
import org.valkyr.api.framework.webwalker.web.WebNode;
import alpharunescape.src.AlphaRunescape;

import javax.swing.*;

public class WoodcuttingTask extends Task {
    private State currentState = State.CUTTING;
    private WebNode treeLocation, bankLocation;

    JLabel treeLabel = new JLabel("Tree:");
    final JComboBox<Tree> trees = new JComboBox<>(Tree.values());
    JLabel axesLabel = new JLabel("Axe:");
    final JComboBox<Axe> axes = new JComboBox<>(Axe.values());
    final JCheckBox banking = new JCheckBox("Banking?", false);

    public WoodcuttingTask(AlphaRunescape script, String name, Skill[] skills) {
        super(script, name, skills);
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public void execute() {
        if (getScript().myPlayer().isUnderAttack())
            setState(State.BANKING);
        if (treeLocation == null && getLogType() != null) {
            treeLocation = getScript().getWebWalker().getClosestNode(getScript().myPosition(), getScript().getStealthMode(), getLogType().getTreeName());
            if (treeLocation != null && bankLocation == null)
                bankLocation = getScript().getWebWalker().getClosestBank(treeLocation.construct(), getScript().getStealthMode(), false);
        }
        switch (getState()) {
            case CUTTING:
                if (!getScript().getInventory().contains(getAxe().getName()) && !getScript().getEquipment().contains(getAxe().getName()))
                    setState(State.BANKING);
                if (getScript().getInventory().isFull()) {
                    setState(getBanking() ? State.BANKING : State.DROPPING);
                    break;
                }
                GroundItem nest = getScript().getGroundItems().closest("Bird Nest");
                if (nest != null) {
                    nest.interact("Take");
                    getScript().getAbhelper().waitInteractionDelay();
                }
                if (!getScript().myPlayer().isAnimating() && treeLocation != null)
                    getScript().getWebWalker().interactWith(treeLocation, "Chop down", getScript().getStealthMode());
                break;
            case BANKING:
                if (!getScript().getBank().isOpen()) {
                    getScript().getWebWalker().goBank(getScript().getStealthMode(), false);
                } else {
                    getScript().getBank().depositAllExcept(getAxe().getName());
                    getScript().getAbhelper().waitInteractionDelay();
                    if (!getScript().getInventory().contains(getAxe().getName()) && !getScript().getEquipment().contains(getAxe().getName()))
                        getScript().getBank().withdraw(getAxe().getName(), 1);
                    if (!getScript().getInventory().isFull()) {
                        if (getScript().getStealthMode()) {
                            treeLocation = getScript().getWebWalker().getClosestNode(getScript().myPosition(), true, getLogType().getTreeName());
                            bankLocation = getScript().getWebWalker().getClosestBank(treeLocation.construct(), true, false);
                        }
                        setState(State.CUTTING);
                        break;
                    }
                }
                getScript().getAbhelper().waitInteractionDelay();
                break;
            case DROPPING:
                if (getScript().inventory.contains(getLogType().getLogName()))
                    getScript().inventory.dropAllExcept(getAxe().getName());
                else setState(State.CUTTING);
                getScript().getAbhelper().waitInteractionDelay();
                break;
        }

    }

    @Override
    public void initSettings(JPanel panel) {
        treeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        axesLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(treeLabel);
        panel.add(trees);
        panel.add(axesLabel);
        panel.add(axes);
        panel.add(banking);
    }

    private State getState() {
        return this.currentState;
    }

    public void setState(State state) {
        currentState = state;
    }

    public Tree getLogType() {
        return (Tree) trees.getSelectedItem();
    }

    public Axe getAxe() {
        return (Axe) axes.getSelectedItem();
    }

    public boolean getBanking() {
        return banking.isSelected();
    }

    public static enum State {
        CUTTING("Cutting trees"), DROPPING("Dropping"), BANKING("Banking");
        private String status;

        State(String status) {
            this.status = status;
        }

        String getStatus() {
            return status;
        }
    }
}