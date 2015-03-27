package org.valkyr.api.enums;

public enum Fish {
	SHRIMP("Shrimp", 318, 1),
	TROUT("Trout", 334, 15),
	SALMON("Salmon", 330, 25),
	TUNA("Tuna", 362, 30),
	LOBSTER("Lobster", 380, 40),
	BASS("Bass", 366, 43),
	SWORDFISH("Swordfish", 374, 45),
	MONKFISH("Monkfish", 7946, 62),
	SHARK("Shark", 386, 80),
	MANTA_RAY("Manta ray", 392, 91);
	
	private String name;
	private int id;
	private int level;
	
	Fish(String name, int id, int level) {
        this.name = name;
		this.id = id;
		this.level = level;
	}

    String getName() {
        return name;
    }
	
	int getRawId() {
		return id;
	}
	
	int getCookedId() {
		return id + 2;
	}
	
	int getLevel() {
		return level;
	}
}
