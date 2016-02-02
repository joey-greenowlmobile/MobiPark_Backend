package com.greenowl.callisto.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.greenowl.callisto.domain.Authority;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.security.SecurityUtils;
import com.greenowl.callisto.service.EligiblePlanUserService;
import com.greenowl.callisto.service.ParkingPlanService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.service.register.RegistrationService;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.web.rest.dto.ParkingPlanDTO;
import com.greenowl.callisto.web.rest.dto.PasswordUpdateDTO;
import com.greenowl.callisto.web.rest.dto.UserDTO;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;
import com.greenowl.callisto.web.rest.dto.user.UpdateAccountRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/{apiVersion}/")
public class AccountResource {

    private final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private static final String USERNAME_TAKEN = "username is already in use!";

    private static final String USER_NOT_REMOVED = "Unable to remove user.";

    private static final String USER_NOT_FOUND = "Unable to find user.";

    private static final String PHONE_NUM_TAKEN = "mobile phone number is already in use!";

    private static final String STRIPE_FAILED = "register with stripe failed!";

    private static final String PLAN_NOT_FOUND = "Unable to find suitable plan.";

    @Inject
    private UserRepository userRepository;

    @Inject
    private RegistrationService registrationService;

    @Inject
    private UserService userService;

    @Inject
    private EligiblePlanUserService eligiblePlanUserService;

    @Inject
    private ParkingPlanService parkingPlanService;

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = false)
    public ResponseEntity<?> registerAccount(@PathVariable("apiVersion") final String apiVersion, @Valid @RequestBody CreateUserRequest req) {
        Optional<User> optional = userRepository.findOneByLogin(req.getEmail()); //login available
        if (optional.isPresent()) {
            return new ResponseEntity<>(genericBadReq(USERNAME_TAKEN, "/register"),
                    BAD_REQUEST);
        }
        User dbUser = userRepository.findOneByMobileNumber(req.getMobileNumber());
        if (dbUser != null) {
            return new ResponseEntity<>(genericBadReq(PHONE_NUM_TAKEN, "/register"),
                    BAD_REQUEST);
        }
        List<PlanEligibleUser> users = eligiblePlanUserService.getPlansByUserEmail(req.getEmail());
        if (users.size() == 0) {
            return new ResponseEntity<>(genericBadReq(PLAN_NOT_FOUND, "/register"),
                    BAD_REQUEST);
        }
        String stripeToken = registrationService.stripeRegister(req);
        if (stripeToken == null) {
            return new ResponseEntity<>(genericBadReq(STRIPE_FAILED, "/register"),
                    BAD_REQUEST);
        }
        UserDTO dto = registrationService.register(req, stripeToken);
        List<ParkingPlanDTO> parkingPlanDTOs = new ArrayList<ParkingPlanDTO>();
        if (users.size() == 1) {
            ParkingPlanDTO parkingPlanDTO = parkingPlanService.createParkingPlanInformation(users.get(0).getPlanGroup());
            return new ResponseEntity<>(parkingPlanDTO, OK);
        }
        for (PlanEligibleUser user : users) {
            ParkingPlan plan = user.getPlanGroup();
            if (plan != null) {
                parkingPlanDTOs.add(parkingPlanService.createParkingPlanInformation(plan));
            }

        }
        return new ResponseEntity<>(parkingPlanDTOs, OK);
    }


    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getAccount(@PathVariable("apiVersion") final String apiVersion) {
        User user = userService.getUserWithAuthorities();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<String> roles = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        UserDTO userDTO = UserUtil.getUserDTO(user, roles);
        LOG.debug("Returning user back to client: {}", userDTO);
        return new ResponseEntity<>(userDTO, OK);
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    public ResponseEntity<?> saveAccount(@PathVariable("apiVersion") final String apiVersion, @RequestBody UpdateAccountRequest req) {
        userService.updateUserInformation(SecurityUtils.getCurrentLogin(),
                req.getFirstName(),
                req.getLastName(),
                req.getRegion());
        return new ResponseEntity<>(OK);
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@PathVariable("apiVersion") final String apiVersion, @RequestBody PasswordUpdateDTO dto) {
        String password = dto.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 5 || password.length() > 50) {
            return new ResponseEntity<>(genericBadReq("Password must be between 5 and 50 characters",
                    "api/account/change_password"), BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(OK);
    }


}
