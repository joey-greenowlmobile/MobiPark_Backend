package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingActivityService {


    @Inject
    private ParkingPlanRepository parkingPlanRepository;

    @Inject
    private ParkingActivityRepository parkingActivityRepository;


    private List<ParkingActivity> findAllActivityBetween(DateTime startTime, DateTime endTime) {
        return parkingActivityRepository.getParkingActivityBetween(startTime, endTime);
    }

    /**
     * Create parking activity for plan user and store in the database.
     *
     * @param user
     * @param plan
     * @return
     */
    public ParkingActivityDTO createParkingActivityForPlanUser(User user, ParkingPlan plan) {
        ParkingActivity newActivity = new ParkingActivity();
        newActivity.setActivityHolder(user);
        newActivity.setLotId(plan.getLotId());
        newActivity.setType("subscription");
        Double totalCharge = 0.0;
        newActivity.setParkingStatus(Constants.PARKING_STATUS_PARKING_START);
        parkingActivityRepository.save(newActivity);
        ParkingActivityDTO parkingActivityDTO = contructDTO(newActivity, user);
        return parkingActivityDTO;
    }

    /**
     * Return all the in-flight activities for the user.
     *
     * @param user
     * @return
     */
    public List<ParkingActivity> findInFlightActivityByUser(User user) {
        List<ParkingActivity> parkingActivities = parkingActivityRepository.getParkingActivitiesByUser(user);
        List<ParkingActivity> inFlightActivities = new ArrayList<ParkingActivity>();
        for (ParkingActivity activity : parkingActivities) {
            if (activity.getEntryDatetime() != null && activity.getExitDatetime() == null) {
                inFlightActivities.add(activity);
            }
        }
        return inFlightActivities;
    }


    /**
     * Return all the activities between start date and end date based on the
     * filtered type.
     *
     * @param type
     * @return
     */
    public List<ParkingActivity> findAllFilteredActivityBetweenStartAndEndDate(DateTime start, DateTime end,
                                                                               String type) {
        List<ParkingActivity> filteredList = new ArrayList<>();
        List<ParkingActivity> parkingActivities = findAllActivityBetween(start, end);
        for (ParkingActivity activity : parkingActivities) {
            switch (type.toLowerCase()) {
                case "all":
                    filteredList.add(activity);
                    break;
                case "inflight":
                    if (activity.getParkingStatus() != null) {
                        if (activity.getParkingStatus().equals("IN_FLIGHT")) {
                            filteredList.add(activity);
                        }
                    }
                    break;
                case "park":
                    if (activity.getEntryDatetime() != null) {
                        if (activity.getParkingStatus().equals("COMPLETED")) {
                            filteredList.add(activity);
                        }
                    }
                    break;
                case "exception":
                    if (activity.getParkingStatus() != null) {
                        if (activity.getParkingStatus().equals("EXCEPTION")) {
                            filteredList.add(activity);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return filteredList;
    }

    public ParkingActivityDTO contructDTO(ParkingActivity activity, User user) {
        return new ParkingActivityDTO(activity, user);
    }

    public void updateParkingStatus(String parkingStatus, long id) {
        parkingActivityRepository.setParkingStatusById(parkingStatus, id);
    }

    public void updateGateResponse(String gateResponse, long id) {
        parkingActivityRepository.setGateResponse(gateResponse, id);
    }

    public void updateExitTime(DateTime timestamp, long id) {
        parkingActivityRepository.setExitTime(timestamp, id);
    }
}
