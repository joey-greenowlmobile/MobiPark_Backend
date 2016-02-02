package com.greenowl.callisto.factory;

import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.web.rest.dto.payment.CardProfile;

public class PaymentProfileFactory {

    private PaymentProfileFactory() {
    }

    public static PaymentProfile create(CardProfile card, String created, Boolean livemode, Boolean used, User user, String cardToken) {
        PaymentProfile paymentProfile = new PaymentProfile();
        paymentProfile.setProfileHolder(user);
        paymentProfile.setCardToken(cardToken);
        paymentProfile.setExpMonth(card.getExpMonth());
        paymentProfile.setExpYear(card.getExpYear());
        paymentProfile.setLast4(card.getLast4());
        paymentProfile.setActive(true);

        return paymentProfile;
    }
}
