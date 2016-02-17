package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class SalesActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(SalesActivityResource.class);

    @Inject
    private SalesActivityService salesActivityService;

    @Inject
    private SalesActivityRepository salesActivityRepository;

    /**
     * GET /api/{version}/parking/records -> Returns a list of records between a start and end date of type :type.
     */
    @RequestMapping(value = "/records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @RolesAllowed(value = AuthoritiesConstants.OPERATOR)
    public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion, @RequestParam(defaultValue = "all") final String type,
                                        @RequestParam(required = false) final Long start, @RequestParam(required = false) final Long end) {
        LOG.debug("Checking for records using type = {}, for start date = and end date = {}", type, start, end);
        List<ParkingSaleActivity> parkingSaleActivities;

        if (type.equals("all")) {
            parkingSaleActivities = salesActivityRepository.findAll();
        } else {
            DateTime startDate = new DateTime(start);
            DateTime endDate = new DateTime(end);
            parkingSaleActivities = salesActivityService.findAllActivityBetween(startDate, endDate);
        }

        List<SalesActivityDTO> salesActivityDTOs = new ArrayList<>();
        List<ParkingSaleActivity> filteredParkingSaleActivities = salesActivityService.filter(parkingSaleActivities, type);

        salesActivityDTOs.addAll(filteredParkingSaleActivities.stream().map(parkingSaleActivity -> salesActivityService.contructDTO(parkingSaleActivity, parkingSaleActivity.getActivityHolder())).collect(Collectors.toList()));
        LOG.info("Returning {} records", salesActivityDTOs.size());
        return new ResponseEntity<>(salesActivityDTOs, OK);

    }

}

