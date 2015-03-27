package org.valkyr.api.enums;

public enum Container {

    JUG("Jug"),
    BUCKET("Bucket"),
    VIAL("Vial");

    String name;

    private Container(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
