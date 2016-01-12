package com.greenowl.callisto.config;

public enum MarkerType {
    ALL, PARKING, DEAL, CONSTRUCTION, USER, RESTAURANT;

    public static MarkerType getType(String name) {
        MarkerType[] types = MarkerType.values();
        for (MarkerType type : types) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return ALL;
    }
}
