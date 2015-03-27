package org.valkyr.api.enums;

import java.util.ArrayList;

public enum FishingSpot {

    NET("Net", FishingEquipment.SMALL_NET),
    BAIT("Bait", FishingEquipment.FISHING_ROD, FishingEquipment.FISHING_BAIT),
    LURE("Lure", FishingEquipment.FLY_FISHING_ROD, FishingEquipment.FEATHERS),
    HARPOON("Harpoon", FishingEquipment.HARPOON),
    CAGE("Cage", FishingEquipment.LOBSTER_POT);

    private String option;
    private FishingEquipment[] equipment;

    FishingSpot(String option, FishingEquipment... equipment) {
        this.option = option;
        this.equipment = equipment;
    }

    public String getName() {
        return "Fishing spot";
    }

    public String getOption() {
        return option;
    }

    public String[] getEquipment() {
        ArrayList<String> strings = new ArrayList<>();
        for (FishingEquipment eq : equipment)
            strings.add(eq.getName());
        return strings.toArray(new String[strings.size()]);
    }
}
