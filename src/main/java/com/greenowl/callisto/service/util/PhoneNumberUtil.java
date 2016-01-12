package com.greenowl.callisto.service.util;

/**
 * Created by Ahmed on 2015-05-27.
 */
public class PhoneNumberUtil {

    public static String getFormattedNumber(String phoneNumber){
        return phoneNumber.replaceAll("[^\\d.]", "");
    }
}
