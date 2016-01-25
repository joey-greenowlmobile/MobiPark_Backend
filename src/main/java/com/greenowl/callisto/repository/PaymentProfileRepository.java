package com.greenowl.callisto.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;

public interface PaymentProfileRepository extends JpaRepository<PaymentProfile, Long> {
	   
	   @Query("select u from PaymentProfile u where u.id = ?1")
	   PaymentProfile getPaymentProfileById(Long id);
	   
	   @Query("select u from PaymentProfile u where u.cardToken = ?1")
	   PaymentProfile getPaymentProfileByCardToken(String cardToken);

	   @Query("select u from PaymentProfile u where u.active = false and u.createdDate > ?1")
	   List<PaymentProfile> findNotActivatedPaymentProfilesByCreationDateBefore(DateTime dateTime);
	   
	   @Query("select u from PaymentProfile u where u.profileHolder=?1")
	   List<PaymentProfile> getPaymentProfilesByUser(User user);

	   
}
