package com.greenowl.callisto.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;

public class PaymentProfileUtil {
	private PaymentProfileUtil(){}
	
	
	
	  /**
     * Retrieve a PaymentProfile DTO object from a paymentProfile entity.
     *
     * @param paymentProfile
     * @return
     */
	public static PaymentProfileDTO getPaymentProfileDTO(PaymentProfile paymentProfile){
		return new PaymentProfileDTO(paymentProfile.getId(),
				paymentProfile.getProfileHolder().getId(),
				paymentProfile.getCardToken(),
				paymentProfile.getExpMonth(),
				paymentProfile.getExpYear(),
				paymentProfile.getLast4());
	}
	
	
	 /**
     * Get a List of UserDTO objects from a List of Users
     *
     * @param users
     * @return
     */
    public static List<PaymentProfileDTO> getPaymentProfileDTOs(Collection<PaymentProfile> paymentProfiles) {
        final List<PaymentProfileDTO> dtos = new ArrayList<>();
        if (paymentProfiles == null || paymentProfiles.size() == 0) {
            return dtos;
        }
       for (PaymentProfile paymentProfile: paymentProfiles) {
                dtos.add(getPaymentProfileDTO(paymentProfile));
            
        };
        return dtos;
    }
}
