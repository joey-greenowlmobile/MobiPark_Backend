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


    public static final String PARKING_STATUS_PARKING_START = "PARKING_START";
    public static final String PARKING_STATUS_PENDING_ENTER = "PENDING_ENTER";
    public static final String PARKING_STATUS_PENDING_ENTER_MANUAL = "PENDING_ENTER_MANUAL";
    public static final String PARKING_STATUS_PENDING_EXIT = "PENDING_EXIT";
    public static final String PARKING_STATUS_PENDING_EXIT_MANUAL = "PENDING_EXIT_MANUAL";
    public static final String PARKING_STATUS_IN_FLIGHT = "IN_FLIGHT";
    public static final String PARKING_STATUS_IN_FLIGHT_MANUAL = "IN_FLIGHT_MANUAL";
    public static final String PARKING_STATUS_EXCEPTION_ENTER = "ALARM_ENTER";
    public static final String PARKING_STATUS_EXCEPTION_ENTER_MANUAL = "ALARM_ENTER_MANUAL";
    public static final String PARKING_STATUS_EXCEPTION_EXIT = "ALARM_EXIT";
    public static final String PARKING_STATUS_EXCEPTION_EXIT_MANUAL = "ALARM_EXIT_MANUAL";
    public static final String PARKING_STATUS_COMPLETED = "COMPLETED";
    public static final String PARKING_STATUS_COMPLETED_MANUAL = "COMPLETED_MANUAL";
   
    public static final String PARKING_ENTRY_EXCEPTION_ALREADY_INSIDE = "PARKING_ENTRY_EXCEPTION_ALREADY_INSIDE";
    public static final String PARKING_ENTRY_EXCEPTION_INCORRECT_LOT = "PARKING_ENTRY_EXCEPTION_INCORRECT_LOT";
    public static final String PARKING_ENTRY_EXCEPTION_UNSUBSCRIBED = "PARKING_ENTRY_EXCEPTION_UNSUBSCRIBED";
    public static final String PARKING_ENTRY_EXCEPTION_INTERNAL_ERROR = "PARKING_ENTRY_EXCEPTION_INTERNAL_ERROR";
    public static final String PARKING_ENTRY_EXCEPTION_GATE_OPEN_FAILED = "PARKING_ENTRY_EXCEPTION_GATE_OPEN_FAILED";
    
    public static final String PARKING_EXIT_EXCEPTION_NOT_INSIDE = "PARKING_EXIT_EXCEPTION_NOT_INSIDE";
    public static final String PARKING_EXIT_EXCEPTION_GATE_OPEN_FAILLED = "PARKING_EXIT_EXCEPTION_GATE_OPEN_FAILLED";
        

    public static final int PARKING_TICKET_TYPE_ENTER = 1;
    public static final int PARKING_TICKET_TYPE_EXIT = 2;

    public static final String GATE_OPEN_RESPONSE_1 = "OPEN-GATE: OPEN";
    public static final String GATE_OPEN_RESPONSE_2 = "OPEN-GATE: ALREADY-OPEN";

    public static final String  GATE_API_IP = "GATE_API_IP";
    public static final String  GATE_API_PORT = "GATE_API_PORT";
}
