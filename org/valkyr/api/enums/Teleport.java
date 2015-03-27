package org.valkyr.api.enums;

import org.osbot.rs07.api.map.Position;

public enum Teleport {
    VARROCK_TAB("Varrock teleport tab", City.VARROCK),
    LUMBRIDGE_TAB("Lumbridge teleport tab", City.LUMBRIDGE),
    FALADOR_TAB("Falador teleport tab", City.VARROCK);
    private final String name;
    private final City destination;

    Teleport(String name, City destination) {
        this.name = name;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public Position getDestination() {
        return destination.construct();
    }
}
