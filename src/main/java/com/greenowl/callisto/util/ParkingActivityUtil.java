package com.greenowl.callisto.util;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;

public class ParkingActivityUtil {

    public static ParkingActivityDTO constructDTO(ParkingActivity activity, User user) {
        return new ParkingActivityDTO(activity, user);
    }

    public static ParkingActivityDTO constructDTO(ParkingActivity activity) {
        return new ParkingActivityDTO(activity, activity.getActivityHolder());
    }

}
