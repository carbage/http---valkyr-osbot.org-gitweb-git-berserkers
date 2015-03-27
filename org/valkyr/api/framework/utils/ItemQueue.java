package org.valkyr.api.framework.utils;

import org.valkyr.api.framework.script.LoopScript;

import java.util.HashMap;
import java.util.Map;

public class ItemQueue {

    private final LoopScript script;
    Map<String, Integer> itemsNeeded = new HashMap<>();

    public ItemQueue(LoopScript script) {
        this.script = script;
    }

    public void collectItems() {
        if (!itemsNeeded.isEmpty() && script.getBanking())
            if (script.getWebWalker().goBank(script.getStealthMode(), false))
                for (String s : itemsNeeded.keySet())
                    if (script.getWidgets().getInventory().contains(s))
                        if (script.getWidgets().getBank().withdraw(s, itemsNeeded.get(s)))
                            itemsNeeded.remove(s);
    }

    public void addItem(String name, int quantity) {
        itemsNeeded.put(name, quantity);
    }

    public void addItem(String name) {
        addItem(name, 1);
    }
}
