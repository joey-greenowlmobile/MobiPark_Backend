package com.greenowl.callisto.factory;

import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;

public class PaymentProfileFactory {

	private PaymentProfileFactory(){}
	
	public static PaymentProfile create(User user, String stripeToken, String cardToken, String ccType, String cardholderName, String ccExpiryDate){
		PaymentProfile paymentProfile=new PaymentProfile();
		paymentProfile.setProfileHolder(user);
		paymentProfile.setStripeToken(stripeToken);
		paymentProfile.setCardToken(cardToken);
		paymentProfile.setCcType(ccType);
		paymentProfile.setCardholderName(cardholderName);
		paymentProfile.setCcExpiryDate(ccExpiryDate);
		paymentProfile.setActive(true);

		return paymentProfile;
	}
}
