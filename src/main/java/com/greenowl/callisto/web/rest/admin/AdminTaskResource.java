package com.greenowl.callisto.web.rest.admin;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.exception.ErrorResponseFactory;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.security.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin/task")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class AdminTaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdminTaskResource.class);

    @Inject
    private ParkingActivityRepository parkingActivityRepository;

    /**
     * GET -> /task/updateParkingActivity updates a parking activity.
     */
    @RequestMapping(value = "/updateParkingActivity/{id}", method = {RequestMethod.GET, RequestMethod.POST}
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> getPaginatedUsers(@PathVariable("id") final Long id, @RequestParam(required = false) final Long entryDateTime,
                                               @RequestParam(required = false) final Long exitDateTime,
                                               @RequestParam(required = false) String parkingStatus, @RequestParam(required = false) String gateResponse) {

        LOG.info("Attempting to modify activity with id = {} for administrator = {}", id, SecurityUtils.getCurrentLogin());
        ParkingActivity activity = parkingActivityRepository.findOne(id);
        if (activity == null) {
            return new ResponseEntity<>(ErrorResponseFactory.notFound("Unable to find ParkingActivity with id = " + id, "/api/admin/task/updateParkingActivity/" + id), HttpStatus.NOT_FOUND);
        }

        if (entryDateTime != null) {
            DateTime dateTime = new DateTime(entryDateTime);
            LOG.info("Setting entry date time for parking activity id = {} to {}", id, entryDateTime);
            activity.setEntryDatetime(dateTime);
        }

        if (exitDateTime != null) {
            DateTime dateTime = new DateTime(exitDateTime);
            LOG.info("Setting exit date time for parking activity id = {} to {}", id, exitDateTime);
            activity.setExitDatetime(dateTime);
        }

        if (!StringUtils.isEmpty(gateResponse)) {
            LOG.info("Setting gate_response for parking activity id = {} to {}", id, gateResponse);
            activity.setGateResponse(gateResponse);
        }

        if (!StringUtils.isEmpty(parkingStatus)) {
            LOG.info("Setting gate_status for parking activity id = {} to {}", id, parkingStatus);
            activity.setParkingStatus(parkingStatus);
        }

        LOG.info("Saving new Parking activity = {} into database.", activity);

        parkingActivityRepository.save(activity);
        return new ResponseEntity<>(OK);
    }


}
