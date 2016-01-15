package com.greenowl.callisto.config;

/**
 * Application constants.
 */
public final class Constants {

    public enum MobilePlatform {
        IOS, ANDROID
    }

    private Constants() {
    }

    public static final String SPRING_PROFILE_STAGING = "stg";
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast"; // No Api Docs
    public static final String SYSTEM_ACCOUNT = "system";

    public static final String RUNTIME_CONFIG_FILE_PATH = "config/config.json";

}
