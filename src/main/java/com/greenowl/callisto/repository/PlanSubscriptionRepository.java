package com.greenowl.callisto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.User;

public interface PlanSubscriptionRepository extends JpaRepository<PlanSubscription,Long> {
	
	@Query("select u from PlanSubscription u where u.planGroup = ?1")
	List<PlanSubscription> getPlanSubscriptionByPlan(ParkingPlan planGroup);
	

	@Query("select u from PlanSubscription u where u.user = ?1")
	List<PlanSubscription> getPlanSubscriptionByUser(User user);
	
	@Query("select u from PlanSubscription u where u.paymentProfile = ?1")
	List<PlanSubscription> getPlanSubscriptionByPaymentProfile(PaymentProfile paymentProfile);

	@Query("select u from PlanSubscription u where u.id = ?1")
	PlanSubscription getPlanSubscriptionById(Long id);
}
