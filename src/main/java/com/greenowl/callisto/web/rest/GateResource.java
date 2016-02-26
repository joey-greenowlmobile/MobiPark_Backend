package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.config.AppConfigKey;
import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.ParkingActivityService;
import com.greenowl.callisto.service.ParkingValTicketStatusService;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.service.config.ConfigService;
import com.greenowl.callisto.util.ApiUtil;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.parking.GateOpenRequest;
import com.greenowlmobile.parkgateclient.parkgateCmdClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class GateResource {

    @Inject
    private SalesActivityService salesActivityService;

    @Inject
    private ParkingValTicketStatusService ticketStatusService;

    @Inject
    private UserService userService;

    @Inject
    private ConfigService configService;

    @Inject
    private ParkingActivityService parkingActivityService;

    private static final Logger LOG = LoggerFactory.getLogger(GateResource.class);

    @RequestMapping(value = "/enter",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> enterParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        if (ApiUtil.getVersion().getValue() == 2) {
            LOG.info("Returning V2 for Enter Parking Lot API.");
            return enterParkingLotV2(req);
        }
        LOG.info("Returning V1 for Enter Parking Lot API.");
        return enterParkingLotV1(req);
    }

    private ResponseEntity<?> enterParkingLotV1(GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (salesActivityService.findInFlightActivityByUser(user).size() != 0) {
            return new ResponseEntity<>(genericBadReq("User already inside parking lot, can't open gate.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (user.getPlanSubscriptions().size() > 0) {
            ParkingPlan subscribedPlan = user.getPlanSubscriptions().iterator().next().getPlanGroup();
            if (subscribedPlan != null) {
                if (subscribedPlan.getLotId() == req.getLotId()) {
                    SalesActivityDTO salesActivityDTO = salesActivityService.createParkingActivityForPlanUser(user, subscribedPlan);
                    if (salesActivityDTO != null) {
                        //open gate
                        final String result = openGate(1, salesActivityDTO.getId().toString());
                        if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {
                            salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, salesActivityDTO.getId());
                            ticketStatusService.createParkingValTicketStatus(salesActivityDTO.getId(), Constants.PARKING_TICKET_TYPE_ENTER, DateTime.now());
                            return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
                        } else {
                            salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, salesActivityDTO.getId());
                            salesActivityService.updateGateResponse(result, salesActivityDTO.getId());
                            return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate：" + result, "/gate"),
                                    org.springframework.http.HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>(genericBadReq("ERROR-Failed to create parking record.", "/gate"),
                                org.springframework.http.HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(genericBadReq("ERROR-Incorrect parking lot.", "/gate"),
                            org.springframework.http.HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(genericBadReq("ERROR-Fetching plan data.", "/gate"),
                        org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(genericBadReq("ERROR-User haven't subscribed.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> enterParkingLotV2(GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (parkingActivityService.findInFlightActivityByUser(user).size() != 0) {
            return new ResponseEntity<>(genericBadReq("User already inside parking lot, can't open gate.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (user.getPlanSubscriptions().size() > 0) {
            ParkingPlan subscribedPlan = user.getPlanSubscriptions().iterator().next().getPlanGroup();
            if (subscribedPlan != null) {
                if (subscribedPlan.getLotId() == req.getLotId()) {
                    ParkingActivityDTO parkingActivityDTO = parkingActivityService.createParkingActivityForPlanUser(user, subscribedPlan);
                    if (parkingActivityDTO != null) {
                        //open gate
                        final String result = openGate(1, parkingActivityDTO.getId().toString());
                        if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {
                            parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, parkingActivityDTO.getId());
                            ticketStatusService.createParkingValTicketStatus(parkingActivityDTO.getId(), Constants.PARKING_TICKET_TYPE_ENTER, DateTime.now());
                            return new ResponseEntity<>(parkingActivityDTO, org.springframework.http.HttpStatus.OK);
                        } else {
                            parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, parkingActivityDTO.getId());
                            parkingActivityService.updateGateResponse(result, parkingActivityDTO.getId());
                            return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate：" + result, "/gate"),
                                    org.springframework.http.HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>(genericBadReq("ERROR-Failed to create parking record.", "/gate"),
                                org.springframework.http.HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(genericBadReq("ERROR-Incorrect parking lot.", "/gate"),
                            org.springframework.http.HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(genericBadReq("ERROR-Fetching plan data.", "/gate"),
                        org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(genericBadReq("ERROR-User haven't subscribed.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }


    private String openGate(int gateId, String ticketNo) {
        parkgateCmdClient parkClient = new parkgateCmdClient("localhost", 2222);
        String response = parkClient.openGate(gateId, ticketNo);
        Boolean simulateMode = configService.get(AppConfigKey.GATE_SIMULATION_MODE.name(), Boolean.class, false);
        LOG.info("GATE_SIMULATE_MODE:" + simulateMode);
        if (simulateMode && response != null && response.contains("OPEN-GATE: NOT-PRESENT")) {
            response = "OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'";
            LOG.info("replace response with \"OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'\"");
        }
        return response;
    }

    @RequestMapping(value = "/exit",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> exitParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        if (ApiUtil.getVersion().getValue() == 2) {
            LOG.info("Returning V2 for Exit Parking Lot API.");
            return exitParkingLotV1(req);
        }
        LOG.info("Returning V1 for Exit Parking Lot API.");
        return exitParkingLotV2(req);
    }

    private ResponseEntity<?> exitParkingLotV1(GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (salesActivityService.findInFlightActivityByUser(user).size() == 0) {
            return new ResponseEntity<>(genericBadReq("User not in the parking lot, can't open gate.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        ParkingSaleActivity parkingSaleActivity = salesActivityService.findInFlightActivityByUser(user).get(0);
        if (parkingSaleActivity != null) {
            final String result = openGate(2, Long.toString(parkingSaleActivity.getId()));
            if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {
                salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, parkingSaleActivity.getId());
                ticketStatusService.createParkingValTicketStatus(parkingSaleActivity.getId(), Constants.PARKING_TICKET_TYPE_EXIT, DateTime.now());
                SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
                return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
            } else {
                salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, parkingSaleActivity.getId());
                salesActivityService.updateGateResponse(result, parkingSaleActivity.getId());
                return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate:" + result, "/gate"),
                        org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(genericBadReq("ERROR-failed to find parking record.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }


    private ResponseEntity<?> exitParkingLotV2(GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (parkingActivityService.findInFlightActivityByUser(user).size() == 0) {
            return new ResponseEntity<>(genericBadReq("User not in the parking lot, can't open gate.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        ParkingActivity parkingActivity = parkingActivityService.findInFlightActivityByUser(user).get(0);
        if (parkingActivity != null) {
            final String result = openGate(2, Long.toString(parkingActivity.getId()));
            if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {
                parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, parkingActivity.getId());
                ticketStatusService.createParkingValTicketStatus(parkingActivity.getId(), Constants.PARKING_TICKET_TYPE_EXIT, DateTime.now());
                ParkingActivityDTO parkingActivityDTO = parkingActivityService.contructDTO(parkingActivity, user);
                return new ResponseEntity<>(parkingActivityDTO, org.springframework.http.HttpStatus.OK);
            } else {
                parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, parkingActivity.getId());
                parkingActivityService.updateGateResponse(result, parkingActivity.getId());
                return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate:" + result, "/gate"),
                        org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(genericBadReq("ERROR-failed to find parking record.", "/gate"),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
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
        //salesActivityRepository.save(parkingSaleActivity);
        SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
        return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
    }
}

