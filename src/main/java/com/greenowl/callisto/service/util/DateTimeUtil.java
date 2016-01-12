package com.greenowl.callisto.service.util;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

/**
 * Created by Ahmed on 2015-05-27.
 * Simple Utility Class responsible for Date/Time manipulation
 */
public class DateTimeUtil {


    /**
     * Get a human readable time from a time stamp, for example "5 minutes ago"
     * @param timestamp
     * @return
     */
    public static String getPrettyTime(Long timestamp){
        DateTime time = new DateTime(timestamp);
        return new PrettyTime().format(time.toDate());
    }

    public static String getPrettyTimeNow(){
        return getPrettyTime(DateTime.now().getMillis());
    }
}
