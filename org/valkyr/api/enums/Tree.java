package org.valkyr.api.enums;

public enum Tree {
    REGULAR("Tree", 51, 49, 9441, 305, 9),
    DEAD("Dead tree", 51, 49, 9441, 305, 9),
    EVERGREEN("Evergreen", 51, 49, 9441, 305, 9),
    ACHEY("Achey tree", 51, 49, 9441, 305, 9),
    OAK("Oak", 57, 55, 9443, 304, 4),
    WILLOW("Willow", 61, 59, 9445, 304, 4),
    MAPLE("Maple", 65, 63, 9449, 304, 4),
    YEW("Yew", 69, 67, 9453, 304, 4),
    MAGIC("Magic", 73, 71, 9355, 304, 4);
    private String name;
    private int interfaceId;
    private int fletchType;
    private int shortbow;
    private int longbow;
    private int crossbow;

    Tree(String name, int shortbow, int longbow, int crossbow,
         int interfaceId, int fletchType) {
        this.name = name;
        this.interfaceId = interfaceId;
        this.fletchType = fletchType;
        this.shortbow = shortbow;
        this.longbow = longbow;
        this.crossbow = longbow;
    }

    String getName() {
        return this.name;
    }

    public String getTreeName() {
        return this == MAPLE ? this.getName() + " tree" : this.getName();
    }

    public String getLogName() {
        return this == REGULAR || this == DEAD || this == EVERGREEN ? "Logs" : this.getName() + " logs";
    }

    public int getInterfaceId() {
        return this.interfaceId;
    }

    public int getFletchInterface() {
        return this.fletchType;
    }

    int getShortbow() {
        return shortbow;
    }

    int getLongbow() {
        return longbow;
    }

    int getCrossbow() {
        return crossbow;
    }
}

