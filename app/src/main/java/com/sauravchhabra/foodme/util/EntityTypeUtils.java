package com.sauravchhabra.foodme.util;

public enum EntityTypeUtils {
    CITY("city"),
    SUBZONE("subzone"),
    ZONE("zone"),
    LANDMARK("landmark"),
    METRO("metro"),
    GROUP("group");

    private String type;

    public String getType() {
        return type;
    }

    EntityTypeUtils(String type) {
        this.type = type;
    }
}
