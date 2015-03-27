package org.valkyr.api.enums;

import org.osbot.rs07.api.ui.Skill;

public enum Resource {
    ORE_CLAY("Clay", 1),
    ORE_RUNE_ESS("Rune essence", 1),
    ORE_COPPER("Copper", 1),
    ORE_TIN("Tin", 1),
    ORE_IRON("Iron", 15),
    ORE_COAL("Coal", 30),
    ORE_PURE_ESS("Pure essence", 1),
    ORE_SANDSTONE("Sandstone", 1),
    ORE_MITHRIL("Mithril", 55),
    ORE_ADAMANTITE("Adamantite", 70),
    ORE_RUNITE("Runite", 85),
    TREE_REGULAR("Tree", 1),
    TREE_DEAD("Dead tree", 1),
    TREE_EVERGREEN("Evergreen", 1),
    TREE_ACHEY("Achey tree", 1),
    TREE_OAK("Oak", 15),
    TREE_WILLOW("Willow", 30),
    TREE_MAPLE("Maple", 45),
    TREE_YEW("Yew", 60),
    TREE_MAGIC("Magic", 75),
    FISHING_SHRIMP("Shrimp", 1, FishingEquipment.SMALL_NET.getName());

    private final String name;
    private final int level;
    private final String[] equipment;

    Resource(String name, int level, String... equipment) {
        this.name = name;
        this.level = level;
        this.equipment = equipment;
    }

    public String getItemName(Skill skill) {
        if (skill == Skill.MINING
                && this != ORE_CLAY
                && this != ORE_RUNE_ESS
                && this != ORE_COAL
                && this != ORE_SANDSTONE)
            return name + " ore";
        if (skill == Skill.WOODCUTTING)
            if (this == TREE_DEAD) return "Logs";
            else return name + " logs";
        return name;
    }

    public String getEntityName(Skill skill) {
        if (skill == Skill.MINING
                && (this != ORE_CLAY
                && this != ORE_RUNE_ESS
                && this != ORE_COAL
                && this != ORE_SANDSTONE))
            return "Rocks";
        if (skill == Skill.WOODCUTTING
                && (this == TREE_DEAD
                || this == TREE_MAPLE
                || this == TREE_ACHEY))
            return name + " tree";
        return name;
    }

    public String[] getEquipment() {
        return equipment;
    }
}
