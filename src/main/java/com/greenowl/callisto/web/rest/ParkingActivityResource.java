package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.service.ParkingActivityService;
import com.greenowl.callisto.util.ParkingActivityUtil;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class ParkingActivityResource {

    @Inject
    private ParkingActivityRepository parkingActivityRepository;

    @Inject
    private ParkingActivityService parkingActivityService;
    private static final Logger LOG = LoggerFactory.getLogger(ParkingActivityResource.class);

    /**
     * POST /records -> get the parking activities based on type.
     */
    @RequestMapping(value = "/records", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion,
                                        @RequestParam(required = false) final Long start,
                                        @RequestParam(required = false) final Long end) {
        List<ParkingActivity> parkingActivities;
        if (start == null && end == null) {
            LOG.debug("No start and end date requested. Returning all records.");
            parkingActivities = parkingActivityRepository.findAll();
        } else {
            LOG.debug("Checking for records for start date = {} and end date = {}", start, end);
            DateTime startDate = new DateTime(start);
            DateTime endDate = new DateTime(end);
            parkingActivities = parkingActivityService.findAllActivitiesBetweenStartAndEndDates(startDate, endDate);
        }

        List<ParkingActivityDTO> parkingActivityDTOs = parkingActivities.stream().map(ParkingActivityUtil::constructDTO).collect(Collectors.toList());
        LOG.info("Returning {} records", parkingActivityDTOs.size());
        return new ResponseEntity<>(parkingActivityDTOs, OK);

    }
}
