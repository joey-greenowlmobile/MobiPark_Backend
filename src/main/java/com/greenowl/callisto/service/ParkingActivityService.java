package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.util.PaginationUtil;
import com.greenowl.callisto.util.ParkingActivityUtil;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(ParkingActivityService.class);

    @Inject
    private ParkingActivityRepository parkingActivityRepository;

    @Inject
    private SalesActivityRepository salesActivityRepository;

    private List<ParkingActivity> findAllActivityBetween(DateTime startTime, DateTime endTime) {
        return parkingActivityRepository.getParkingActivityBetween(startTime, endTime);
    }

    /**
     * Create parking activity for plan user and store in the database.
     *
     * @param user the user this plan belongs to.
     * @param plan the parking plan.
     * @return
     */
    public ParkingActivityDTO createParkingActivityForPlanUser(User user, ParkingPlan plan) {
        ParkingActivity newActivity = new ParkingActivity();
        newActivity.setActivityHolder(user);
        newActivity.setLotId(plan.getLotId());
        newActivity.setType("subscription");
        newActivity.setParkingStatus(Constants.PARKING_STATUS_PARKING_START);
        parkingActivityRepository.save(newActivity);
        return ParkingActivityUtil.constructDTO(newActivity, user);
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

    public void updateParkingStatus(String parkingStatus, long id) {
        parkingActivityRepository.setParkingStatusById(parkingStatus, id);
    }

    public void updateGateResponse(String gateResponse, long id) {
        parkingActivityRepository.setGateResponse(gateResponse, id);
    }

    public void updateExitTime(DateTime timestamp, long id) {
        parkingActivityRepository.setExitTime(timestamp, id);
    }

    public Optional<ParkingActivity> getLatestActivityForUser(User user) {
        Page<ParkingActivity> page = parkingActivityRepository.findByActivityHolder(user, PaginationUtil.generateDescSortedIdrequest(0, 5));
        List<ParkingActivity> content = page.getContent();
        if (content != null && !content.isEmpty()) {
            content.get(0).getActivityHolder();
            return Optional.of(content.get(0));
        }
        return Optional.empty();
    }

}
