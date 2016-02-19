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

	public static final String STRIPE_TEST_KEY="sk_test_ZJmc3FVt5QFtpOp2eAWEGaFJ ";
    
    public static final String RUNTIME_CONFIG_FILE_PATH = "config/config.json";
    public static final Double SERVICE_FEES_PERCENTAGE = 0.2;
    
    public static final int  ENTER_GATE = 1;
    

    public static final String PARKING_STATUS_PARKING_START = "PARKING_START";
    public static final String PARKING_STATUS_PENDING = "PENDING";
    public static final String PARKING_STATUS_IN_FLIGHT = "IN_FLIGHT";
    public static final String PARKING_STATUS_EXCEPTION = "EXCEPTION";
    public static final String PARKING_STATUS_COMPLETED = "COMPLETED";
    public static final String PARKING_STATUS_EXCEPTION_NO_PASSED = "EXCEPTION_NO PASSED";
    public static final String PARKING_STATUS_EXCEPTION_NO_COMM = "EXCEPTION_NO_COMM";
    public static final String PARKING_STATUS_EXCEPTION_NO_COMM_NO_PASSED = "EXCEPTION_NO_COMM_NO_PASSED";
    
    public static final int PARKING_TICKET_TYPE_ENTER = 1;
    public static final int PARKING_TICKET_TYPE_EXIT = 2;
    
    
}
