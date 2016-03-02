package com.greenowl.callisto.config;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ErrorCodeConstants {
    public enum MobilePlatform {
        IOS, ANDROID
    }

    private ErrorCodeConstants() {
    }

    public static final Integer REGISTER_USERNAME_TAKEN = 1;
    public static final Integer REGISTER_PHONENUM_TAKEN = 2;
    public static final Integer REGISTER_STRIPE_FAILED = 3;
    public static final Integer REGISTER_PLAN_NOTFOUND = 4;
    public static final Integer REGISTER_DATABASE_ERROR = 5;

    public static final Integer GATE_USER_INSIDE_PARKING_LOT = 1;
    public static final Integer GATE_OPEN_FAILED = 2;
    public static final Integer GATE_DATABASE_ERROR = 3;
    public static final Integer GATE_INCORRECT_PARKING_LOT = 4;
    public static final Integer GATE_USER_UNSUBSCRIBED = 5;
    public static final Integer GATE_RECORD_NOTFOUND = 6;
    public static final Integer GATE_USER_NOT_INSIDE_PARKING_LOT = 7;

    public static final Integer SUBSCRIPITION_DATABASE_ERROR = 3;

}
