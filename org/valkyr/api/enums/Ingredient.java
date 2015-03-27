package org.valkyr.api.enums;

public enum Ingredient {

    FLOUR("Flour"),
    MILK("Bucket of milk"),
        WATER("of water"),
    MEAT("meat");




    String name;

    private Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    }
