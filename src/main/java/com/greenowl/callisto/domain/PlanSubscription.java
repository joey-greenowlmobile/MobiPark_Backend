package com.greenowl.callisto.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "T_CUSTOMER_PLAN_SUBSCRIPTION")
public class PlanSubscription extends AbstractAuditingEntity implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id", referencedColumnName = "id")
	private ParkingPlan planGroup;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_id", referencedColumnName = "id")
	private PaymentProfile paymentProfile;
	   
	@Column(name= "plan_start_date", nullable= false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime planStartDate;
	
	@Column(name= "plan_charge_amount", nullable= false)
	private Double planChargeAmount;
	
	@Column(name= "plan_expiry_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime planExpiryDate;

	@Column(name= "cancel_request_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime cancel_request_date;
	
	@Column(name ="cancel_request_reason")
	private String cancelRequestReason;
	
	@Column(name= "cancel_effective_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime cancelEffectiveDate;

	@Column(name ="cancel_handled_by")
	private String cancelHandledBy;

	@Column(name= "cancel_refund_amount")
	private Double cancelRefundAmount;
	
	@Column(name ="stripe_id" , nullable=false)
	private String stripeId;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ParkingPlan getPlanGroup() {
		return planGroup;
	}

	public void setPlanGroup(ParkingPlan planGroup) {
		this.planGroup = planGroup;
	}

	public PaymentProfile getPaymentProfile() {
		return paymentProfile;
	}

	public void setPaymentProfile(PaymentProfile paymentProfile) {
		this.paymentProfile = paymentProfile;
	}

	public DateTime getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(DateTime planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Double getPlanChargeAmount() {
		return planChargeAmount;
	}

	public void setPlanChargeAmount(Double planChargeAmount) {
		this.planChargeAmount = planChargeAmount;
	}

	public DateTime getPlanExpiryDate() {
		return planExpiryDate;
	}

	public void setPlanExpiryDate(DateTime planExpiryDate) {
		this.planExpiryDate = planExpiryDate;
	}

	public DateTime getCancel_request_date() {
		return cancel_request_date;
	}

	public void setCancel_request_date(DateTime cancel_request_date) {
		this.cancel_request_date = cancel_request_date;
	}

	public String getCancelRequestReason() {
		return cancelRequestReason;
	}

	public void setCancelRequestReason(String cancelRequestReason) {
		this.cancelRequestReason = cancelRequestReason;
	}

	public DateTime getCancelEffectiveDate() {
		return cancelEffectiveDate;
	}

	public void setCancelEffectiveDate(DateTime cancelEffectiveDate) {
		this.cancelEffectiveDate = cancelEffectiveDate;
	}

	public String getCancelHandledBy() {
		return cancelHandledBy;
	}

	public void setCancelHandledBy(String cancelHandledBy) {
		this.cancelHandledBy = cancelHandledBy;
	}

	public Double getCancelRefundAmount() {
		return cancelRefundAmount;
	}

	public void setCancelRefundAmount(Double cancelRefundAmount) {
		this.cancelRefundAmount = cancelRefundAmount;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	
	
	
	
}
