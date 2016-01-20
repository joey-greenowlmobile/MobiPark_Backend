package com.greenowl.callisto.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.factory.PaymentProfileFactory;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.util.PaymentProfileUtil;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;
import com.greenowl.callisto.web.rest.dto.payment.CreatePaymentProfileRequest;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class StripeAccountService {

    private final Logger LOG = LoggerFactory.getLogger(UserService.class);
	@Inject
	private PaymentProfileRepository paymentProfileRepository;
	@Inject
	private UserRepository userRepository;

	public List<PaymentProfile> getPaymentProfileById(Integer id){
		
		return paymentProfileRepository.getPaymentProfileById(id);
	}
	public List<PaymentProfile> getPaymentProfileByStripeToken(String stripeToken){
		return paymentProfileRepository.getPaymentProfileByStripeToken(stripeToken);
	}
	
	public PaymentProfileDTO registerPaymentProfile(CreatePaymentProfileRequest req){
		return createPaymentInformation(req.getLogin(),req.getStripeToken(),req.getCardToken(),req.getCardholderName(),req.getCcType(),req.getCcExpiryDate());
	}
	
	private  PaymentProfileDTO createPaymentInformation(String login, String stripeToken, String cardToken, String ccType,String cardholderName, String ccExpiryDate){
	  	Optional<User> optUser = userRepository.findOneByLogin(login);
    	if(optUser.isPresent()){
    		User user = optUser.get();
    		PaymentProfile savedProfile= PaymentProfileFactory.create(user, stripeToken, cardToken, ccType, cardholderName, ccExpiryDate);
    		if(paymentProfileRepository.getPaymentProfileByStripeTokenAndCardToken(stripeToken, cardToken)==null){
    		paymentProfileRepository.save(savedProfile);
    		PaymentProfileDTO paymentProfileDTO = PaymentProfileUtil.getPaymentProfileDTO(savedProfile);
    		return paymentProfileDTO;}
    		else{
    			System.out.println((paymentProfileRepository.getPaymentProfileByStripeTokenAndCardToken(stripeToken, cardToken)).getCardToken());
    		}
    	}
		return null;
		
	}
	}

