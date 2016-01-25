package com.greenowl.callisto.web.rest.stripe;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.EligiblePlanUserService;
import com.greenowl.callisto.service.StripeAccountService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;
import com.greenowl.callisto.web.rest.dto.ResponseDTO;
import com.greenowl.callisto.web.rest.dto.payment.CreatePaymentProfileRequest;
import com.greenowl.callisto.web.rest.dto.payment.PaymentPlanRequest;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;
import com.greenowl.callisto.web.rest.dto.user.UpdateAccountRequest;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;

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
    @RequestMapping(value = "/payment", method = RequestMethod.POST
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = false)
    public ResponseEntity<?> AddPayment(@PathVariable("version") String version ,  @Valid @RequestBody PaymentPlanRequest req) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	
    	User user = userService.getCurrentUser();
    	Customer customer = Customer.retrieve(user.getStripeToken());
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("source", req.getToken().getId());
    	String cardToken = customer.createCard(params).getId();
    	stripeAccountService.registerPaymentProfile(req.getToken(),user.getLogin(),cardToken);
    	String response =eligiblePlanUserService.subscribePlan(user.getLogin(), req.getPlanId());
		ResponseDTO responseDTO=new ResponseDTO(response);
    	return new ResponseEntity<>(responseDTO,OK);
    }
   
    
    @RequestMapping(value = "/payment", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = false)
    public ResponseEntity<?> GetPreviousPayment(@PathVariable("version") String version ) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	User user = userService.getCurrentUser();
    	List<PaymentProfileDTO> paymentProfileDTOs =stripeAccountService.getAllPaymentProfileDTOs(user);
    	 return new ResponseEntity<>(paymentProfileDTOs, OK);
    }
   
    
    @RequestMapping(value = "/payment", method = RequestMethod.DELETE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = false)
    public ResponseEntity<?> DeletePayment(@PathVariable("version") String version, @Valid @RequestParam Long id) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException{
    	Stripe.apiKey=Constants.STRIPE_TEST_KEY;
    	User user = userService.getUserWithAuthorities();
    	Customer customer = Customer.retrieve(user.getStripeToken());
    	System.out.println(user.getStripeToken());
    	
    	PaymentProfile paymentProfile= stripeAccountService.getPaymentProfileById(id);
    	if (paymentProfile==null ){
          	 return new ResponseEntity<>(genericBadReq("Can't find payment profile.","/user/payment"),HttpStatus.NOT_FOUND);
		}
		for(ExternalAccount source : customer.getSources().getData()){
			System.out.println(source.getId());
			if(source.getId().equals(paymentProfile.getCardToken())){
				source.delete();
				paymentProfileRepository.delete(id);
				return new ResponseEntity<>(OK);
			}
		}
		return new ResponseEntity<>(genericBadReq("Failed to find payment profile on stripe.","/user/payment"),HttpStatus.NOT_FOUND);
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

