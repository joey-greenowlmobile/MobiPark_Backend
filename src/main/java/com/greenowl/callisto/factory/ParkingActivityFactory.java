package com.greenowl.callisto.factory;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.User;

/**
 * Created by ahmed on 2016-03-07.
 */
public class ParkingActivityFactory {

    public static ParkingActivity create(User user, ParkingPlan plan, String parkingStatus, String deviceInfo) {
        ParkingActivity newActivity = new ParkingActivity();
        newActivity.setActivityHolder(user);
        newActivity.setLotId(plan.getLotId());
        newActivity.setType("subscription");
        newActivity.setParkingStatus(parkingStatus);
        newActivity.setDeviceInfo(deviceInfo);
        return newActivity;
    }
}
