package org.valkyr.api.enums;

import org.osbot.rs07.api.map.Position;

public enum City {

    LUMBRIDGE(3222, 3218, 0),
    AL_KHARID(3293, 3174, 0),
    DRAYNOR(3093, 3244, 0),
    PORT_SARIM(3023, 3208, 0),
    KARAMJA(2948, 3147, 0),
    CRANDOR(2851, 3238, 0),
    RIMMINGTON(2957, 3214, 0),
    VARROCK(3210, 3424, 0),
    DIGSITE(3354, 3402, 0),
    FALADOR(2964, 3378, 0),
    BARBARIAN_VILLAGE(3082, 3420, 0),
    EDGEVILLE(3093, 3493, 0),
    BURTHORPE(2926, 3559, 0),
    CATHERBY(2813, 3447, 0),
    CAMELOT(2757, 3477, 0),
    SEERS_VILLAGE(2708, 3492, 0),
    WEST_ARDOUGNE(2529, 3307, 0),
    EAST_ARDOUGNE(2662, 3305, 0),
    TREE_GNOME_STRONGHOLD(2461, 3443, 0),
    POLLNIVNEACH(3359, 2910, 0),
    SOPHANEM(3274, 2784, 0),
    CANIFIS(3506, 3496, 0),
    PORT_PHASMATYS(3687, 3502, 0),
    MORT_TON(3489, 3288, 0),
    TZHAAR(2480, 5175, -2),
    APE_ATOLL(2755, 2784, 0);

    private int x, y, z;

    City(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position construct() {
        return new Position(x, y, z);
    }
}
