package org.valkyr.api.framework.utils;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.valkyr.api.framework.script.LoopScript;

public class WidgetHandler {

    private LoopScript script;

    public WidgetHandler(LoopScript script) {
        this.script = script;
    }

    public RS2Widget getWidgetFromText(String name) {
        if (script.getWidgets() != null && script.getWidgets().getAll() != null)
            for (RS2Widget child : script.getWidgets().getAll())
                if (child != null)
                    if (child.getSpellName().contains(name) || child.getMessage().contains(name) || child.getToolTip().contains(name))
                        return child;
        return null;
    }

    public boolean interactWithWidget(String name, String option) {
        RS2Widget child = getWidgetFromText(name);
        return child != null && child.interact(option);
    }

    public boolean interactWithItem(String name, String option) {
        RS2Widget child = script.getWidgets().getInventory().getItem(name).getOwner();
        return child != null && child.interact(option);
    }

    public boolean useItemOnItem(String itm1, String itm2) {
        Item item1 = script.getWidgets().getInventory().getItem(itm1);
        Item item2 = script.getWidgets().getInventory().getItem(itm2);
        if (item1 != null && item2 != null)
            if (!script.getWidgets().getInventory().isItemSelected()) {
                item1.getOwner().interact("Use");
            } else if (script.getWidgets().getInventory().getSelectedItemName().equals(itm1)) {
                item2.interact("Use");
                return true;
            } else if (script.getWidgets().getInventory().getSelectedItemName().equals(itm2)) {
                item1.interact("Use");
                return true;
            } else {
                script.getWidgets().getInventory().deselectItem();
            }
        return false;
    }
}
