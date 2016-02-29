package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.*;
import com.greenowl.callisto.factory.PaymentProfileFactory;
import com.greenowl.callisto.repository.*;
import com.greenowl.callisto.web.rest.dto.payment.CardProfile;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class SubscriptionService {

	private final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);

	@Inject
	private PlanSubscriptionRepository planSubscriptionRepository;

	@Inject
	private ParkingPlanRepository parkingPlanRepository;

	@Inject
	private PaymentProfileRepository paymentProfileRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PlanEligibleUserRepository planEligibleUserRepository;

	public PlanSubscription getPlanSubscriptionById(Long id) {
		return planSubscriptionRepository.getPlanSubscriptionById(id);
	}

	/**
	 * Update plan subscription for a user based on one payment.
	 *
	 * @param user
	 * @param planId
	 * @param paymentProfileId
	 * @param stripeId
	 * @return
	 */
	public PlanSubscription createPlanSubscription(User user, Long planId, Long paymentProfileId, String stripeId) {
		PlanSubscription planSubscription = new PlanSubscription();
		DateTime createdDate = DateTime.now();
		ParkingPlan parkingPlan = parkingPlanRepository.getOneParkingPlanById(planId);
		PaymentProfile paymentProfile = paymentProfileRepository.getPaymentProfileById(paymentProfileId);
		if (parkingPlan != null & paymentProfile != null) {
			planSubscription.setUser(user);
			planSubscription.setPlanGroup(parkingPlan);
			planSubscription.setPaymentProfile(paymentProfile);
			planSubscription.setPlanStartDate(createdDate);
			planSubscription.setPlanChargeAmount(parkingPlan.getUnitChargeAmount());
			planSubscription.setStripeId(stripeId);
			planSubscriptionRepository.save(planSubscription);
			return planSubscription;
		} else {
			return null;
		}
	}

	//TODO: lingfei port to split SalesRecord and ParkingActivity
//	/**
//	 * Get the all the next day subscription that should be charged.
//	 *
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
//	public List<PlanSubscription> getNextDayRenewSubscription(DateTime startDate, DateTime endDate) {
//		List<PlanSubscription> nextDaySubscriptions = new ArrayList<>();
//		List<PlanSubscription> allPlanSubscriptions = planSubscriptionRepository.getAllPlanSubscription();
//		for (PlanSubscription plan : allPlanSubscriptions) {
//			if (checkNextDaySubscription(plan, startDate, endDate)
//					&& salesActivityService.validNewTransaction(plan.getUser(), startDate, endDate)) {
//				nextDaySubscriptions.add(plan);
//			}
//		}
//		return nextDaySubscriptions;
//	}

	/**
	 * Check if the subscription should be charged the next day.
	 *
	 * @param subscription
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private boolean checkNextDaySubscription(PlanSubscription subscription, DateTime startDate, DateTime endDate) {
		Stripe.apiKey = Constants.STRIPE_TEST_KEY;
		String token = subscription.getUser().getStripeToken();
		String subToken = subscription.getStripeId();
		LOG.debug("User token ={} with subToken= {}", token, subToken);
		try {
			Long startTime = Customer.retrieve(token).getSubscriptions().retrieve(subToken).getCurrentPeriodEnd();
			if (startTime == null) {
				LOG.debug("stripe can't find startTime for user={}", subscription.getUser().getLogin());
				return false;

			}
			DateTime rewNewDate = new DateTime(startTime * 1000);
			if (rewNewDate.isAfter(startDate) && rewNewDate.isBefore(endDate)) {
				return true;
			} else {
				return false;
			}
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			return false;
		}
	}

	/**
	 * Auto subscribe the user to plan table if they already paid out of the
	 * system.
	 *
	 * @param userId
	 * @param planId
	 * @return
	 */
	public PlanSubscription autoSubscribe(Long userId, Long planId) {
		User user = userRepository.findOne(userId);
		CardProfile fakeCard = new CardProfile();
		fakeCard.setExpMonth((long) 12);
		fakeCard.setExpYear((long) 2099);
		fakeCard.setLast4("9999");
		PaymentProfile savedProfile = PaymentProfileFactory.create(fakeCard, "admin", true, false, user,
				"Remote Subscriber");
		paymentProfileRepository.save(savedProfile);
		// might need to change this one later
		List<PlanEligibleUser> users = planEligibleUserRepository.getEligibleUsersByUserEmail(user.getLogin());
		for (PlanEligibleUser eligibleUser : users) {
			if (eligibleUser.getPlanGroup().getId().equals(planId)) {
				eligibleUser.setSubscribed(true);
				planEligibleUserRepository.save(eligibleUser);
				break;
			}
		}
		PlanSubscription planSubscription = createPlanSubscription(user, planId, savedProfile.getId(),
				"Remote Subscribed");
		return planSubscription;

	}
}
