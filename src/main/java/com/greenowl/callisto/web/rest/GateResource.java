package com.greenowl.callisto.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.greenowl.callisto.config.AppConfigKey;
import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.config.ErrorCodeConstants;
import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.service.ParkingActivityService;
import com.greenowl.callisto.service.ParkingValTicketStatusService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.service.config.ConfigService;
import com.greenowl.callisto.util.ParkingActivityUtil;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import com.greenowl.callisto.web.rest.parking.GateOpenRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Optional;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class GateResource {

    @Inject
    private ParkingValTicketStatusService ticketStatusService;

    @Inject
    private UserService userService;

    @Inject
    private ConfigService configService;

    @Inject
    private ParkingActivityService parkingActivityService;

    @Inject
    private ParkingActivityRepository parkingActivityRepository;

    private static final Logger LOG = LoggerFactory.getLogger(GateResource.class);

    /**
     * POST /enter -> open the entrance gate of the parking lot.
     */
    @RequestMapping(value = "/enter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> enterParkingLot(@PathVariable("apiVersion") final String apiVersion,
                                             @RequestBody GateOpenRequest req) {
        return enterParkingLot(req);
    }

    @Timed
    private ResponseEntity<?> enterParkingLot(GateOpenRequest req) {
        User user = userService.getCurrentUser();
        // No in flight record Exists.
        Optional<ParkingActivity> optional = parkingActivityService.getInFlightRecordForUser(user);
        if (optional.isPresent()) {
            return new ResponseEntity<>(
                    genericBadReq("User already inside parking lot, can't open gate.", "/gate",
                            ErrorCodeConstants.GATE_USER_INSIDE_PARKING_LOT),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        // User not associated with a plan subscription.
        if (user.getPlanSubscriptions() == null || user.getPlanSubscriptions().isEmpty()) {
            return new ResponseEntity<>(
                    genericBadReq("User is not associated with a subscription plan.", "/gate", ErrorCodeConstants.GATE_USER_UNSUBSCRIBED),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        ParkingPlan subscribedPlan = user.getPlanSubscriptions().iterator().next().getPlanGroup();

        //
        if (subscribedPlan == null) {
            return new ResponseEntity<>(
                    genericBadReq("Unable to fetch subscription plan.", "/gate", ErrorCodeConstants.GATE_DATABASE_ERROR),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }


        if (subscribedPlan.getLotId() != req.getLotId()) {
            return new ResponseEntity<>(
                    genericBadReq("ERROR-Incorrect parking lot.", "/gate",
                            ErrorCodeConstants.GATE_INCORRECT_PARKING_LOT),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }

        ParkingActivityDTO parkingActivityDTO;
        Optional<ParkingActivity> opt = parkingActivityService.getLatestActivityForUser(user);
        if (opt.isPresent()) {
            ParkingActivity parkingActivity = opt.get();
            String parkingStatus = parkingActivity.getParkingStatus();
            if (Constants.PARKING_STATUS_PARKING_START.equals(parkingStatus) ||
                    Constants.PARKING_STATUS_PENDING_ENTER.equals(parkingStatus) ||
                    Constants.PARKING_STATUS_IN_FLIGHT.equals(parkingStatus)) {
                parkingActivityDTO = ParkingActivityUtil.constructDTO(parkingActivity, user);
            } else {
                parkingActivityDTO = parkingActivityService.createParkingActivityForPlanUser(user, subscribedPlan, req.getDeviceInfo());
            }
        } else {
            parkingActivityDTO = parkingActivityService.createParkingActivityForPlanUser(user, subscribedPlan, req.getDeviceInfo());
        }

        if (parkingActivityDTO != null) {
            // open gate
            final String result = openGate(1, parkingActivityDTO.getId().toString());
            if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1)
                    || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {
                parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING_ENTER,
                        parkingActivityDTO.getId());
                ticketStatusService.createParkingValTicketStatus(parkingActivityDTO.getId(),
                        Constants.PARKING_TICKET_TYPE_ENTER, DateTime.now());
                return new ResponseEntity<>(parkingActivityDTO, org.springframework.http.HttpStatus.OK);
            } else {
                parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION_ENTER,
                        parkingActivityDTO.getId());
                parkingActivityService.updateGateResponse(result, parkingActivityDTO.getId());
                return new ResponseEntity<>(
                        genericBadReq("ERROR-Failed to open parking gate" + result, "/gate",
                                ErrorCodeConstants.GATE_OPEN_FAILED),
                        org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(
                    genericBadReq("ERROR-Failed to create parking record.", "/gate",
                            ErrorCodeConstants.GATE_DATABASE_ERROR),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }

    @Timed
    private String openGate(int gateId, String ticketNo) {
        String ip = configService.get(Constants.GATE_API_IP, String.class, "localhost");
        Integer port = Integer.parseInt(configService.get(Constants.GATE_API_PORT, String.class, "2222"));
        String response = null;

        try{
	        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  	        
	        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
	        StringBuilder url = new StringBuilder();
	        url.append("http://");
	        url.append(ip).append(":").append(port).append("/");
	        url.append("gatecmd/gate_open_cmd?gate_id=");
	        url.append(gateId);
	        url.append("&ticket=");
	        url.append(ticketNo);			
			HttpGet httpGet = new HttpGet(url.toString());							
	        HttpResponse httpResponse = closeableHttpClient.execute(httpGet); 
	        HttpEntity entity = httpResponse.getEntity();  
	        response = EntityUtils.toString(entity,"utf-8").trim();	
	        LOG.info("open gate,url:"+url.toString()+",response:"+response);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Boolean simulateMode = Boolean.parseBoolean(configService.get(AppConfigKey.GATE_SIMULATION_MODE.name(), String.class, "false"));
        LOG.info("GATE_SIMULATE_MODE:" + simulateMode);
        if (simulateMode && response != null && (response.contains("OPEN-GATE: NOT-PRESENT") || response.contains("Process exited with an error"))) {
            response = "OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'";
            LOG.info("replace response with \"OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'\"");
        }
        return response;
    }

    /**
     * POST /exit -> open the exit gate of the parking lot.
     */
    @RequestMapping(value = "/exit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> exitParkingLot(@PathVariable("apiVersion") final String apiVersion,
                                            @RequestBody GateOpenRequest req) {
        return exitParkingLot(req);
    }


    @Timed
    private ResponseEntity<?> exitParkingLot(GateOpenRequest req) {
        User user = userService.getCurrentUser();
        Optional<ParkingActivity> opt = parkingActivityService.getInFlightRecordForUser(user);
        if (!opt.isPresent()) {
            LOG.info("ALARM-ENFORCE-OPEN----user id:" + user.getId() + ",request:" + req.toString());
            opt = parkingActivityService.getLatestActivityForUser(user);
            if (opt.isPresent()) {
                ParkingActivity parkingActivity = opt.get();
                final String result = openGate(2, parkingActivity.getId().toString());
                if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1)
                        || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {
                    parkingActivityService.updateExceptionFlag(Constants.PARKING_EXCEPTION_ENFORCE_OPEN, parkingActivity.getId());
                    ticketStatusService.createParkingValTicketStatus(parkingActivity.getId(),
                            Constants.PARKING_TICKET_TYPE_EXIT, DateTime.now());
                }
            } else {
                LOG.info("ALARM-ENFORCE-OPEN----user id:" + user.getId() + ",no parking records");
            }
            return new ResponseEntity<>(
                    genericBadReq("User not in the parking lot, can't open gate.", "/gate",
                            ErrorCodeConstants.GATE_USER_NOT_INSIDE_PARKING_LOT),
                    org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        ParkingActivity parkingActivity = opt.get();
        final String result = openGate(2, Long.toString(parkingActivity.getId()));
        if (result != null && (result.contains(Constants.GATE_OPEN_RESPONSE_1)
                || result.contains(Constants.GATE_OPEN_RESPONSE_2))) {

            parkingActivity.setParkingStatus(Constants.PARKING_STATUS_PENDING_EXIT);
            parkingActivity.setDeviceInfo(req.getDeviceInfo());
            parkingActivityRepository.save(parkingActivity);
            ticketStatusService.createParkingValTicketStatus(parkingActivity.getId(),
                    Constants.PARKING_TICKET_TYPE_EXIT, DateTime.now());
            ParkingActivityDTO parkingActivityDTO = ParkingActivityUtil.constructDTO(parkingActivity, user);
            return new ResponseEntity<>(parkingActivityDTO, org.springframework.http.HttpStatus.OK);
        } else {
            parkingActivity.setParkingStatus(Constants.PARKING_STATUS_EXCEPTION_EXIT);
            parkingActivity.setGateResponse(result);
            parkingActivity.setDeviceInfo(req.getDeviceInfo());
            parkingActivityRepository.save(parkingActivity);
            return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate:" + result, "/gate",
                    ErrorCodeConstants.GATE_OPEN_FAILED), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }

}
