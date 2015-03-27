package org.valkyr.api.framework.utils;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTracker implements Runnable {

    private List<Item> current = new ArrayList<>();
    private Map<String, Integer> loot = new HashMap<>();
    private boolean isRunning = true;
    private Script s;

    public LootTracker(Script script) {
        this.s = script;
        updateItems();
    }

    public void run() {
        while (isRunning) {
            process();
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.isRunning = false;
    }

    public void updateItems() {
        current.clear();
        if (s.inventory.getItems() != null) {
            for (Item item : s.inventory.getItems()) {
                if (item != null) {
                    current.add(item);
                } else {
                    current.add(new Item(-1, -1));
                }
            }
        }
    }

    public void process() {
        List<Item> ourItems = current;
        List<Item> newItems = getNewList();

        for (int i = 0; i < ourItems.size(); i++) {
            Item ourItem = ourItems.get(i);
            Item newItem = newItems.get(i);

            if (ourItem.getId() != newItem.getId()) {
                add(newItem);
            } else if (ourItem.getAmount() != newItem.getAmount()) {
                if (ourItem.getAmount() < newItem.getAmount()) {
                    add(newItem);
                }
            }
        }
        updateItems();
    }

    private void add(Item item) {
        if (item != null && !s.getBank().isOpen()) {
            Object o;
            int i = (int) s.getInventory().getAmount(item.getName());
            if ((o = loot.get(item.getName())) != null) {
                i -= (int) o;
            }
            loot.put(item.getName(), i);
        }

    }

    private List<Item> getNewList() {
        List<Item> list = new ArrayList<>();
        if (s.inventory.getItems() != null) {
            for (Item item : s.inventory.getItems()) {
                if (item != null) {
                    list.add(item);
                } else {
                    list.add(new Item(-1, -1));
                }
            }
        }
        return list;
    }

    public Map<String, Integer> getLoot() {
        return loot;
    }

    public int getAllGained() {
        int i = 0;
        for (String s : loot.keySet()) {
            if (s != null)
                i += PriceGrab.getInstance().getPrice(s, 2) * loot.get(s);
        }

        return i;
    }

    public int getQuantity(String name) {
        for (String s : loot.keySet()) {
            if (s.equalsIgnoreCase(name))
                return loot.get(s);
        }
        return 0;
    }
}
