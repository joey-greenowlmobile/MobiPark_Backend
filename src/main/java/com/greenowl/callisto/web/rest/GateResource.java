package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.parking.GateOpenRequest;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class GateResource {

    @Inject
    private SalesActivityService salesActivityService;

    @Inject
    private SalesActivityRepository salesActivityRepository;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/enter",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> enterParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (salesActivityService.findInFlightActivityByUser(user).size() != 0) {
            return new ResponseEntity<>(genericBadReq("User already inside parking lot, can't open gate.", "/gate"),
                    BAD_REQUEST);
        }
        if (user.getPlanSubscriptions().size() == 0) {
            ParkingPlan subscribedPlan = user.getPlanSubscriptions().iterator().next().getPlanGroup();
            if (subscribedPlan != null) {
                if (subscribedPlan.getLotId() == req.getLotId()) {
                    SalesActivityDTO salesActivityDTO = salesActivityService.createSaleActivityForPlanUser(user, subscribedPlan);
                    return new ResponseEntity<>(salesActivityDTO, OK);
                    //openGate function

                } else {
                    return new ResponseEntity<>(genericBadReq("Incorret parking lot.", "/gate"),
                            BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(genericBadReq("Error fetching plan data.", "/gate"),
                        BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(genericBadReq("User haven't subscribed.", "/gate"),
                    BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/exit",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> exitParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (salesActivityService.findInFlightActivityByUser(user).size() == 0) {
            return new ResponseEntity<>(genericBadReq("User not in the parking lot, can't open gate.", "/gate"),
                    BAD_REQUEST);
        }
        ParkingSaleActivity parkingSaleActivity = salesActivityService.findInFlightActivityByUser(user).get(0);
        parkingSaleActivity.setExitDatetime(DateTime.now());
        parkingSaleActivity.setParkingStatus("Finished parking");
        //closeGate Function
        salesActivityRepository.save(parkingSaleActivity);
        SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
        return new ResponseEntity<>(salesActivityDTO, OK);
    }

    @RequestMapping(value = "/systemOpen",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> systemOpenEnterGate(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req, @RequestParam final String reason) {
        User user = userService.getCurrentUser();
        ParkingSaleActivity parkingSaleActivity = new ParkingSaleActivity();
        parkingSaleActivity.setLotId(req.getLotId());
        parkingSaleActivity.setActivityHolder(user);
        parkingSaleActivity.setUserEmail(user.getLogin());
        parkingSaleActivity.setParkingStatus("System");
        parkingSaleActivity.setExceptionFlag(reason);
        //Gate Open
        salesActivityRepository.save(parkingSaleActivity);
        SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
        return new ResponseEntity<>(salesActivityDTO, OK);
    }
}

