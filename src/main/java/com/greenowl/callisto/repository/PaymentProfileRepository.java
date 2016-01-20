package com.greenowl.callisto.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenowl.callisto.domain.PaymentProfile;

public interface PaymentProfileRepository extends JpaRepository<PaymentProfile, Long> {
	   @Query("select u from PaymentProfile u where u.stripeToken = ?1")
	   List<PaymentProfile> getPaymentProfileByStripeToken(String stripeToken);
	   
	   @Query("select u from PaymentProfile u where u.id = ?1")
	   List<PaymentProfile> getPaymentProfileById(Integer id);
	   
	   @Query("select u from PaymentProfile u where u.stripeToken=?1 and u.cardToken=?2")
	   PaymentProfile getPaymentProfileByStripeTokenAndCardToken(String stripeToken, String cardToken);

	    @Query("select u from PaymentProfile u where u.active = false and u.createdDate > ?1")
	    List<PaymentProfile> findNotActivatedPaymentProfilesByCreationDateBefore(DateTime dateTime);

	   
}
