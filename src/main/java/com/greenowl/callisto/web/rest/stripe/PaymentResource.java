package com.greenowl.callisto.web.rest.stripe;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.service.*;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.dto.payment.PaymentPlanRequest;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{version}/user/")
public class PaymentResource {

    private final static Logger LOG = LoggerFactory.getLogger(PaymentResource.class);

    @Inject
    private StripeAccountService stripeAccountService;

    @Inject
    private PaymentProfileRepository paymentProfileRepository;

    @Inject
    private UserService userService;

    @Inject
    private EligiblePlanUserService eligiblePlanUserService;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private SalesActivityService salesActivityService;

    @RequestMapping(value = "/payment", method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> addPayment(@PathVariable("version") String version, @Valid @RequestBody PaymentPlanRequest req) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        Stripe.apiKey = Constants.STRIPE_TEST_KEY;

        User user = userService.getCurrentUser();
        Customer customer = Customer.retrieve(user.getStripeToken());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", req.getToken().getId());
        String cardToken = customer.createCard(params).getId();
        PaymentProfileDTO paymentProfileDTO = stripeAccountService.registerPaymentProfile(req.getToken(), user.getLogin(), cardToken);
        String response = eligiblePlanUserService.subscribePlan(user.getLogin(), req.getPlanId());
        if (response.startsWith("sub_")) {
            PlanSubscription planSubscription = subscriptionService.createPlanSubscription(user, req.getPlanId(), paymentProfileDTO.getId(), response);
            if (planSubscription != null) {
                SalesActivityDTO salesActivityDTO = salesActivityService.saveSaleActivityWithPlan(user, planSubscription);
                if (salesActivityDTO != null) {
                    return new ResponseEntity<>(OK);
                } else {

                    LOG.error("Failed at adding to the sales activity table");
                }
            }
            LOG.error("Failed at adding to the subscription table");

        }
        return new ResponseEntity<>(genericBadReq(response, "/register"),
                BAD_REQUEST);
    }


    @RequestMapping(value = "/payment", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> getPreviousPayment(@PathVariable("version") String version) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
        User user = userService.getCurrentUser();
        List<PaymentProfileDTO> paymentProfileDTOs = stripeAccountService.getAllPaymentProfileDTOs(user);
        return new ResponseEntity<>(paymentProfileDTOs, OK);
    }


    @RequestMapping(value = "/payment", method = RequestMethod.DELETE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> deletePayment(@PathVariable("version") String version, @Valid @RequestParam Long id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
        User user = userService.getUserWithAuthorities();
        Customer customer = Customer.retrieve(user.getStripeToken());
        System.out.println(user.getStripeToken());

        PaymentProfile paymentProfile = stripeAccountService.getPaymentProfileById(id);
        if (paymentProfile == null) {
            return new ResponseEntity<>(genericBadReq("Can't find payment profile.", "/user/payment"), HttpStatus.NOT_FOUND);
        }
        for (ExternalAccount source : customer.getSources().getData()) {
            System.out.println(source.getId());
            if (source.getId().equals(paymentProfile.getCardToken())) {
                source.delete();
                paymentProfileRepository.delete(id);
                return new ResponseEntity<>(OK);
            }
        }
        return new ResponseEntity<>(genericBadReq("Failed to find payment profile on stripe.", "/user/payment"), HttpStatus.NOT_FOUND);
    }

}

