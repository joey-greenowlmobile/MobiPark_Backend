package com.greenowl.callisto.config;

public enum ApiVersion {
    v1(1), v2(2), v3(3); // Supported versions

    private int value;

    ApiVersion(int v) {
        this.value = v;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static ApiVersion latest() {
        return ApiVersion.v1;
    }
}
