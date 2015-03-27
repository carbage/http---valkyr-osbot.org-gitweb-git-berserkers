package alpharunescape.src.task.tasks.combat;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.valkyr.api.enums.Food;
import org.valkyr.api.framework.webwalker.web.WebNode;
import alpharunescape.src.AlphaRunescape;
import alpharunescape.src.task.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CombatTask extends Task {

    private State currentState = State.FIGHTING;
    private String monster;
    private WebNode npcLocation, bankLocation;
    private ArrayList<String> lootItems = new ArrayList<>();
    private Position lastNpcPosition;

    final JCheckBox banking = new JCheckBox("Banking?", false);
    final JCheckBox bonesToPeaches = new JCheckBox("Bones to peaches?", false);
    final JLabel lootLabel = new JLabel("Select your loot");
    final JTextField loot = new JTextField("item1,item2,item3");
    final JLabel foodLabel = new JLabel("Select your food");
    final JComboBox<Food> food = new JComboBox<>(Food.values());
    JComboBox<String> monsterComboBox;
    final ButtonGroup bg = new NoneSelectedButtonGroup();
    final ArrayList<String> monsters = getMonsters();

    public CombatTask(AlphaRunescape script, String name, Skill[] skills) {
        super(script, name, skills);
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public void execute() {
        if (getScript().myPlayer().getInteracting() != null)
            if (lastNpcPosition == null || getScript().myPlayer().getInteracting().getPosition() != lastNpcPosition)
                lastNpcPosition = getScript().myPlayer().getInteracting().getPosition();
        if (npcLocation == null && monster != null) {
            npcLocation = getScript().getWebWalker().getClosestNode(getScript().myPosition(), true, monster);
            if (npcLocation != null && bankLocation == null)
                bankLocation = getScript().getWebWalker().getClosestBank(getScript().myPosition(), false, false);
        }
        switch (getState()) {
            case FIGHTING:
                if (getScript().myPlayer().getHealth() < getScript().getAbhelper().getNextHpPercent()) {
                    if (getBonesToPeaches()) {
                        Item i = getScript().inventory.getItem("Bones to peaches");
                        if (i != null) {
                            i.interact("Break");
                            getScript().getAbhelper().waitInteractionDelay();
                        }
                    }
                    Item food = getFood();
                    if (getFood() != null) {
                        food.interact("Eat");
                        getScript().getAbhelper().waitInteractionDelay();
                    } else if (getBanking()) setState(State.BANKING);
                }
                if (getScript().inventory.isFull() && getFood() == null && getBanking()) {
                    setState(State.BANKING);
                    break;
                } else lootItems(lootItems);
                if (!getScript().getCombat().isFighting() && !getScript().myPlayer().isAnimating())
                    getScript().getWebWalker().interactWith(npcLocation, "Attack", getScript().getStealthMode());
                break;
            case BANKING:
                if (!getBanking())
                    setState(State.FIGHTING);
                if (!getScript().getBank().isOpen()) {
                    getScript().getWebWalker().goBank(getScript().getStealthMode(), false);
                } else {
                    getScript().getBank().depositAllExcept(food.getName());
                    getScript().getBank().withdraw(food.getName(), getScript().getInventory().getEmptySlots());
                    if (getFood() != null) {
                        if (getScript().getStealthMode()) {
                            npcLocation = getScript().getWebWalker().getClosestNode(getScript().myPosition(), getScript().getStealthMode(), monster);
                            bankLocation = getScript().getWebWalker().getClosestBank(npcLocation.construct(), getScript().getStealthMode(), false);
                        }
                        setState(State.FIGHTING);
                    }
                }
                break;

        }
    }

    @Override
    public void initSettings(JPanel panel) {
        foodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lootLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(new JLabel("Select your monster"));
        monsterComboBox = new JComboBox<>(monsters.toArray(new String[monsters.size()]));
        panel.add(monsterComboBox);

        panel.add(foodLabel);
        panel.add(food);

        panel.add(lootLabel);
        panel.add(loot);

        bg.add(bonesToPeaches);
        panel.add(bonesToPeaches);

        panel.add(banking);

    }

    public ArrayList<String> getMonsters() {
        ArrayList<String> mon = new ArrayList<>();
        for (WebNode npc : getScript().getWebWalker().getWeb().getNpcs())
            if (npc.hasAction("Attack") && !mon.contains(npc.getName())) {
                mon.add(npc.getName());
            }
        return mon;

    }

    class NoneSelectedButtonGroup extends ButtonGroup {

        @Override
        public void setSelected(ButtonModel model, boolean selected) {
            if (selected) {
                super.setSelected(model, selected);
            } else {
                clearSelection();
            }
        }
    }

    private void lootItems(ArrayList<String> lootItems) {
        if (getScript().getCombat().isFighting()) {
            NPC n = (NPC) getScript().myPlayer().getInteracting();
            if (n != null)
                lastNpcPosition = n.getPosition();
        }
        if (!getScript().inventory.isFull())
            if (lootItems != null && !lootItems.isEmpty())
                for (GroundItem g : getScript().getGroundItems().getAll())
                    for (String s : lootItems) {
                        if (g.getName().contains(s) || g.getName().equalsIgnoreCase(s)) {
                            getScript().getAbhelper().waitNewObjectDelay();
                            g.interact("Take");
                        }

                    }
    }

    public State getState() {
        return currentState;
    }

    public void setState(State state) {
        currentState = state;
    }

    public void addLootItem(String s) {
        if (s != null)
            lootItems.add(s);
    }

    public void setMonster(String monster) {
        this.monster = monster;
    }

    public boolean getBanking() {
        return banking.isSelected();
    }


    public boolean getBonesToPeaches() {
        boolean b = bonesToPeaches.isSelected();
        String bones = "ones";
        if (b) lootItems.add(bones);
        else if (lootItems.contains(bones)) lootItems.remove(bones);
        return b;
    }

    public Item getFood() {
        for (Item i : getScript().inventory.getItems())
            if (i != null && Arrays.asList(i.getActions()).contains("Eat") && (!getBanking() || i.getName().equals(food.getName())))
                return i;
        return null;
    }

    public static enum State {
        FIGHTING("Killing monsters"), BANKING("Banking");
        private String status;

        State(String status) {
            this.status = status;
        }

        String getStatus() {
            return status;
        }
    }
}
