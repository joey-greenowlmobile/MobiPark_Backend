package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.service.*;
import com.greenowl.callisto.web.rest.dto.ParkingPlanDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/{apiVersion}/user")
public class SubscribeResource {

    private final Logger LOG = LoggerFactory.getLogger(SubscribeResource.class);

    private static final String PLAN_NOT_FOUND = "Unable to find suitable plan.";

    @Inject
    private ParkingPlanService parkingPlanService;

    @Inject
    private EligiblePlanUserService eligiblePlanUserService;

    @Inject
    private UserService userService;

    /**
     * GET /api/{version}/user/plan -> Return all the plans that user can
     * subscribe to.
     */
    @RequestMapping(value = "/plan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> getAllPlan(@PathVariable("apiVersion") final String apiVersion) {
        User currentUser = userService.getCurrentUser();
        List<PlanEligibleUser> users = eligiblePlanUserService.getPlansByUserEmail(currentUser.getLogin());
        if (users.size() == 0) {
            return new ResponseEntity<>(genericBadReq(PLAN_NOT_FOUND, "/plan"), BAD_REQUEST);
        } else {
            if (users.size() == 1) {
                ParkingPlanDTO parkingPlanDTO = parkingPlanService
                        .createParkingPlanInformation(users.get(0).getPlanGroup());
                return new ResponseEntity<>(parkingPlanDTO, OK);
            }
            List<ParkingPlanDTO> parkingPlanDTOs = new ArrayList<ParkingPlanDTO>();
            for (PlanEligibleUser user : users) {
                ParkingPlan plan = user.getPlanGroup();
                if (plan != null) {
                    parkingPlanDTOs.add(parkingPlanService.createParkingPlanInformation(plan));
                }

            }
            return new ResponseEntity<>(parkingPlanDTOs, OK);
        }
    }
}
