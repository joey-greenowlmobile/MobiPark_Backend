package com.greenowl.callisto.service;

import org.springframework.stereotype.Service;

@Service
public class SalesActivityService {

//    @Inject
//    private ParkingPlanRepository parkingPlanRepository;
//
//    @Inject
//    private SalesActivityRepository salesActivityRepository;
//
//    @Inject
//    private UserRepository userRepository;
//
//    @Inject
//    private PlanSubscriptionRepository planSubscriptionRepository;
//
//    private static final Logger LOG = LoggerFactory.getLogger(SalesActivityService.class);
//
//    public SalesActivityDTO savePlanSaleRecord(User user, PlanSubscription plan) throws AuthenticationException,
//            InvalidRequestException, APIConnectionException, CardException, APIException {
//        return createPlanSaleRecord(user, plan);
//
//    }
//
//    /**
//     * Create sales record when user subscribe to the plan.
//     *
//     * @param user
//     * @param plan
//     * @return
//     */
//    public SalesActivityDTO createPlanSaleRecord(User user, PlanSubscription plan) throws AuthenticationException,
//            InvalidRequestException, APIConnectionException, CardException, APIException {
//        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
//        ParkingSaleActivity newActivity = new ParkingSaleActivity();
//        Map<String, Object> invoiceParams = new HashMap<String, Object>();
//        invoiceParams.put("limit", 3);
//        invoiceParams.put("customer", user.getStripeToken());
//
//        List<Invoice> invoices = Invoice.list(invoiceParams).getData();
//        for (Invoice invoice : invoices) {
//            if (invoice.getSubscription().equals(plan.getStripeId())) {
//                newActivity.setInvoiceId(invoice.getId());
//                break;
//            }
//        }
//        if (newActivity.getInvoiceId().isEmpty()) {
//            return null;
//        }
//        newActivity.setActivityHolder(user);
//        newActivity.setPlanId(plan.getPlanGroup().getId());
//        newActivity.setPlanName(parkingPlanRepository.getOneParkingPlanById(plan.getPlanGroup().getId()).getPlanName());
//        newActivity.setLotId(plan.getPlanGroup().getLotId());
//        newActivity.setUserEmail(user.getLogin());
//        newActivity.setUserPhoneNumber(user.getMobileNumber());
//        newActivity.setUserLicensePlate(user.getLicensePlate());
//        newActivity.setPlanSubscriptionDate(plan.getPlanStartDate());
//        Double totalCharge = plan.getPlanChargeAmount();
//        newActivity.setChargeAmount(totalCharge);
//        newActivity.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
//        newActivity.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
//        newActivity.setParkingStatus("PAYMENT_COMPLETED");
//        newActivity.setPpId(plan.getPaymentProfile().getId());
//        salesActivityRepository.save(newActivity);
//        return SalesActivityUtil.constructDTO(newActivity, user);
//
//    }
//
//    /**
//     * Create parking activity for plan user and store in the database.
//     *
//     * @param user
//     * @param plan
//     * @return
//     */
//    public SalesActivityDTO createParkingActivityForPlanUser(User user, ParkingPlan plan) {
//        ParkingSaleActivity newActivity = new ParkingSaleActivity();
//        newActivity.setActivityHolder(user);
//        newActivity.setPlanId(plan.getId());
//        newActivity.setPlanName(parkingPlanRepository.getOneParkingPlanById(plan.getId()).getPlanName());
//        newActivity.setLotId(plan.getLotId());
//        newActivity.setUserEmail(user.getLogin());
//        newActivity.setUserPhoneNumber(user.getMobileNumber());
//        newActivity.setUserLicensePlate(user.getLicensePlate());
//        Double totalCharge = 0.0;
//        newActivity.setChargeAmount(totalCharge);
//        newActivity.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
//        newActivity.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
//        newActivity.setParkingStatus(Constants.PARKING_STATUS_PARKING_START);
//        salesActivityRepository.save(newActivity);
//        return SalesActivityUtil.constructDTO(newActivity, user);
//    }
//
//    private List<ParkingSaleActivity> findAllActivityBetween(DateTime startTime, DateTime endTime) {
//        return salesActivityRepository.getParkingSaleActivityBetween(startTime, endTime);
//    }
//
//
//    /**
//     * Return all the in-flight activities for the user.
//     *
//     * @param user
//     * @return
//     */
//    public List<ParkingSaleActivity> findInFlightActivityByUser(User user) {
//        List<ParkingSaleActivity> parkingSaleActivities = salesActivityRepository.getParkingSaleActivitiesByUser(user);
//        List<ParkingSaleActivity> inFlightActivities = new ArrayList<ParkingSaleActivity>();
//        for (ParkingSaleActivity activity : parkingSaleActivities) {
//            if (activity.getEntryDateTime() != null && activity.getExitDateTime() == null) {
//                inFlightActivities.add(activity);
//            }
//        }
//        return inFlightActivities;
//    }
//
//    /**
//     * Return all the activities between start date and end date based on the
//     * filtered type.
//     *
//     * @param type
//     * @return
//     */
//    public List<ParkingSaleActivity> findAllActivitiesBetweenStartAndEndDates(DateTime start, DateTime end,
//                                                                                   String type) {
//        List<ParkingSaleActivity> filteredList = new ArrayList<>();
//        List<ParkingSaleActivity> parkingSaleActivities = findAllActivityBetween(start, end);
//        for (ParkingSaleActivity activity : parkingSaleActivities) {
//            switch (type.toLowerCase()) {
//                case "all":
//                    filteredList.add(activity);
//                    break;
//                case "sales":
//                    if (activity.getChargeAmount() != null && activity.getChargeAmount() != 0) {
//                        filteredList.add(activity);
//                    }
//                    break;
//                case "inflight":
//                    if (activity.getParkingStatus() != null) {
//                        if (activity.getParkingStatus().equals("IN_FLIGHT")) {
//                            filteredList.add(activity);
//                        }
//                    }
//                    break;
//                case "park":
//                    if (activity.getEntryDateTime() != null) {
//                        filteredList.add(activity);
//                    }
//                    break;
//                case "exception":
//                    if (activity.getParkingStatus() != null) {
//                        if (activity.getParkingStatus().equals("EXCEPTION")) {
//                            filteredList.add(activity);
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        return filteredList;
//    }
//
//    /**
//     * Create next day transaction record based on subscriber and mark the
//     * status as pre-transaction.
//     *
//     * @param nextDaySubscriptions
//     * @return
//     */
//    public List<SalesActivityDTO> createPreTransaction(List<PlanSubscription> nextDaySubscriptions) {
//        List<SalesActivityDTO> activities = new ArrayList<>();
//        for (PlanSubscription plan : nextDaySubscriptions) {
//            activities.add(createActivity(plan));
//        }
//        return activities;
//    }
//
//    private SalesActivityDTO createActivity(PlanSubscription subscription) {
//        ParkingSaleActivity activity = new ParkingSaleActivity();
//        activity.setActivityHolder(subscription.getUser());
//        activity.setPlanId(subscription.getPlanGroup().getId());
//        activity.setPlanName(subscription.getPlanGroup().getPlanName());
//        activity.setLotId(subscription.getPlanGroup().getLotId());
//        activity.setUserEmail(subscription.getUser().getLogin());
//        activity.setUserPhoneNumber(subscription.getUser().getMobileNumber());
//        activity.setUserLicensePlate(subscription.getUser().getLicensePlate());
//        Double totalCharge = subscription.getPlanGroup().getUnitChargeAmount();
//        activity.setChargeAmount(totalCharge);
//        activity.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
//        activity.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
//        activity.setParkingStatus("PRE_TRANSACTION");
//        salesActivityRepository.save(activity);
//        return SalesActivityUtil.constructDTO(activity, subscription.getUser());
//    }
//
//    public boolean validNewTransaction(User user, DateTime startDate, DateTime endDate) {
//        List<ParkingSaleActivity> activities = salesActivityRepository.getParkingSaleActivityBetweenForUser(startDate,
//                endDate, user);
//        for (ParkingSaleActivity activity : activities) {
//            if (activity.getChargeAmount() != 0 && activity.getParkingStatus().equals("PRE_TRANSACTION")) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * Get all the pre-transactions and check the status on stripe, update if
//     * the payment has been made. (Not fully tested yet)
//     *
//     * @param startDate
//     * @return
//     */
//    public Long checkEndOfDayTransaction(DateTime startDate) {
//        Long number = (long) 0;
//        List<ParkingSaleActivity> activities = salesActivityRepository
//                .getParkingSaleActivitiesByParkingStatus("PRE_TRANSACTION", startDate);
//        System.out.println(activities.size());
//        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
//        for (ParkingSaleActivity activity : activities) {
//            LOG.debug("activity ={}", activity.getId());
//            User user = userRepository.findSingleUserByLogin(activity.getUserEmail());
//            Long time = activity.getCreatedDate().getMillis() / 1000;
//            Map<String, Object> invoiceParams = new HashMap<String, Object>();
//            invoiceParams.put("limit", 3);
//            invoiceParams.put("customer", user.getStripeToken());
//            try {
//                List<Invoice> invoices = Invoice.list(invoiceParams).getData();
//                if (planSubscriptionRepository.getPlanSubscriptionByUser(user).size() == 0) {
//                    LOG.debug("User {} is not subscribed", user.getLogin());
//                    break;
//                }
//                PlanSubscription subscription = planSubscriptionRepository.getPlanSubscriptionByUser(user).get(0);
//                String subToken = subscription.getStripeId();
//                for (Invoice invoice : invoices) {
//                    if (invoice.getSubscription().equals(subToken) && invoice.getDate() > time) {
//                        if (invoice.getPaid()) {
//                            if (invoice.getAmountDue().doubleValue() / 100 == subscription.getPlanChargeAmount()) {
//                                LOG.debug("The amount match for subscription ={}", subToken);
//                                activity.setInvoiceId(invoice.getId());
//                                activity.setParkingStatus("PAYMENT_COMPLETED");
//                                salesActivityRepository.save(activity);
//                                number += 1;
//                                break;
//                            } else {
//                                LOG.debug("The amount doesn't match for subscription ={}", subToken);
//                            }
//                        } else {
//                            LOG.debug("Unpaid invoice {} for user {}", invoice.getId(), user.getStripeToken());
//                        }
//                    }
//                }
//            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
//                    | APIException e) {
//                LOG.debug("Failed at talking to stripe for user ={}", user.getLogin());
//                e.printStackTrace();
//            }
//        }
//        return number;
//    }
//
//    public ParkingSaleActivity getParkingSaleActivityById(long id) {
//        return salesActivityRepository.getParkingSaleActivityById(id);
//    }
//
//    public void updateParkingStatus(String parkingStatus, long id) {
//        salesActivityRepository.setParkingStatusById(parkingStatus, id);
//    }
//
//    public void updateGateResponse(String gateResponse, long id) {
//        salesActivityRepository.setGateResponse(gateResponse, id);
//    }
//
//    public void updateExitTime(DateTime timestamp, long id) {
//        salesActivityRepository.setExitTime(timestamp, id);
//    }
}
