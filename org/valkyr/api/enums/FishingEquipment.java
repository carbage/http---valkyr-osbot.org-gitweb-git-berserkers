package org.valkyr.api.enums;

public enum FishingEquipment {

    SMALL_NET("Small fishing net"),
    LARGE_NET("Big fishing net"),
    FISHING_ROD("Fishing rod"),
    FLY_FISHING_ROD("Fly fishing rod"),
    FISHING_BAIT("Fishing bait"),
    FEATHERS("Feather"),
    LOBSTER_POT("Lobster pot"),
    HARPOON("Harpoon");

    String name;

    private FishingEquipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
