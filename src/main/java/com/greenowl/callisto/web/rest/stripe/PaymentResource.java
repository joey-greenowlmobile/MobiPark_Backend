package com.greenowl.callisto.web.rest.stripe;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.StripeAccountService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;
import com.greenowl.callisto.web.rest.dto.payment.CreatePaymentProfileRequest;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;
import com.greenowl.callisto.web.rest.dto.user.UpdateAccountRequest;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

@RestController
@RequestMapping("/api/{version}/stripe/")
public class PaymentResource {

    @Inject
	private StripeAccountService stripeAccountService;
    @Inject
    private UserService userService;
    @RequestMapping(value = "/addPayment", method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = false)
    public ResponseEntity<?> AddPayment(@PathVariable("version") String version ,  @Valid @RequestBody CreatePaymentProfileRequest req) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	Customer customer = Customer.retrieve(req.getStripeToken());
    	Card card = (Card)customer.getSources().retrieve(req.getCardToken());
  
    	PaymentProfileDTO paymentProfileDTO = stripeAccountService.registerPaymentProfile(req);
    	 return new ResponseEntity<>(paymentProfileDTO, OK);
    }
   
    @RequestMapping(value = "/registerStripe", method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = false)
    public ResponseEntity<?> AddStripeToken(@PathVariable("version") String version ,  @RequestParam final String stripeToken) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	User user = userService.getUserWithAuthorities();
    	if(user.getStripeToken()==null){
    	userService.changeStripeToken(stripeToken);}
    	else{
    		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    	}
    	return new ResponseEntity<>(OK);
    }
    
    @RequestMapping(value = "/test2", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = true)
    public ResponseEntity<?> TestPayment2(@PathVariable("version") String version, @RequestParam final String token){
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	try {
    		Map<String, Object> chargeParams = new HashMap<String, Object>();
    		  chargeParams.put("amount", 1000); // amount in cents, again
    		  chargeParams.put("currency", "cad");
    		  chargeParams.put("customer", "cus_7ik4R3LrwqS9WJ");
    		  chargeParams.put("description", "Example charge");

    		  Charge charge = Charge.create(chargeParams);
    		} catch (Exception e) {
    	    	 return new ResponseEntity<>( OK);
    		}
    	 return new ResponseEntity<>( OK);
    }
    
    @RequestMapping(value = "/test3", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = true)
    public ResponseEntity<?> TestPayment3(@PathVariable("version")@RequestBody String token ,final String version){
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	try {
    		Customer cu = Customer.retrieve("cus_7ik4R3LrwqS9WJ");
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("plan", "DOC");
    		cu.createSubscription(params);
    		} catch (Exception e) {
    	    	 return new ResponseEntity<>( OK);
    		}
    	 return new ResponseEntity<>( OK);
    }
    
    @RequestMapping(value = "/test4", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = true)
    public ResponseEntity<?> TestPayment4(@PathVariable("version")@RequestBody String token ,final String version){
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;;
    	try {
    		Customer cu = Customer.retrieve("cus_7ik4R3LrwqS9WJ");
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("plan", "DOC");
    		cu.createSubscription(params);
    		} catch (Exception e) {
    	    	 return new ResponseEntity<>( OK);
    		}
    	 return new ResponseEntity<>( OK);
    }

    }

