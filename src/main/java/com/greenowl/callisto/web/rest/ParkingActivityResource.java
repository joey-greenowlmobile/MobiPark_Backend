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
                                        @RequestParam(defaultValue = "all") final String type, @RequestParam(required = false) final Long start,
                                        @RequestParam(required = false) final Long end) {
        LOG.debug("Checking for records using type = {}, for start date = {} and end date = {}", type, start, end);
        List<ParkingActivity> parkingActivities;
        if (type.equals("all")) {
            parkingActivities = parkingActivityRepository.findAll();
        } else {
            DateTime startDate = new DateTime(start);
            DateTime endDate = new DateTime(end);
            parkingActivities = parkingActivityService.findAllFilteredActivityBetweenStartAndEndDate(startDate, endDate,
                    type);
        }

        List<ParkingActivityDTO> parkingActivityDTOs = parkingActivities.stream().map(ParkingActivityUtil::constructDTO).collect(Collectors.toList());
        LOG.info("Returning {} records", parkingActivityDTOs.size());
        return new ResponseEntity<>(parkingActivityDTOs, OK);

    }
}
