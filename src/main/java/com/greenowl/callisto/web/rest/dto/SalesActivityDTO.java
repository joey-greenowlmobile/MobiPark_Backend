package com.greenowl.callisto.web.rest.dto;


import org.joda.time.DateTime;

import com.greenowl.callisto.domain.User;

public class SalesActivityDTO {
	private Long id;
	private Long lotId;
	private Long userId;
	private String userEmail;
	private String userPhoneNumber;
	private String userLicensePlate;
	private Long planId;
	private String planName;
	private DateTime planSubscriptionDate;
	private DateTime planExpiryDate;
	private Double chargeAmount;
	private Double serviceAmount;
	private Double netAmount;
	private Long ppId;
	private DateTime entryDateTime;
	private DateTime exitDateTime;
	private String parkingStatus;
	private Boolean exceptionFlag;
	private String invoiceId;
	
	public SalesActivityDTO(){};
	public SalesActivityDTO(Long id, Long lotId, User user, Long planId, DateTime planSubscriptionDate,
			DateTime planExpiryDate , Double chargeAmount, Double serviceAmount,
			Double netAmount, Long ppId, DateTime entryDateTime, DateTime exitDateTime, String parkingStatus,
			Boolean exceptionFlag, String invoiceId){
		this.id=id;
		this.lotId=lotId;
		this.userId=user.getId();
		this.userEmail=user.getLogin();
		this.userPhoneNumber=user.getMobileNumber();
		this.userLicensePlate=user.getLicensePlate();
		this.planId=planId;
		this.planSubscriptionDate=planSubscriptionDate;
		this.planExpiryDate= planExpiryDate;
		this.chargeAmount=chargeAmount;
		this.serviceAmount=serviceAmount;
		this.netAmount=netAmount;
		this.ppId=ppId;
		this.entryDateTime=entryDateTime;
		this.exitDateTime=exitDateTime;
		this.parkingStatus=parkingStatus;
		this.exceptionFlag=exceptionFlag;
		this.invoiceId=invoiceId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLotId() {
		return lotId;
	}
	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
	public String getUserLicensePlate() {
		return userLicensePlate;
	}
	public void setUserLicensePlate(String userLicensePlate) {
		this.userLicensePlate = userLicensePlate;
	}
	public Long getPlanId() {
		return planId;
	}
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public DateTime getPlanSubscriptionDate() {
		return planSubscriptionDate;
	}
	public void setPlanSubscriptionDate(DateTime planSubscriptionDate) {
		this.planSubscriptionDate = planSubscriptionDate;
	}
	public DateTime getPlanExpiryDate() {
		return planExpiryDate;
	}
	public void setPlanExpiryDate(DateTime planExpiryDate) {
		this.planExpiryDate = planExpiryDate;
	}
	public Double getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public Double getServiceAmount() {
		return serviceAmount;
	}
	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Long getPpId() {
		return ppId;
	}
	public void setPpId(Long ppId) {
		this.ppId = ppId;
	}
	public DateTime getEntryDateTime() {
		return entryDateTime;
	}
	public void setEntryDateTime(DateTime entryDateTime) {
		this.entryDateTime = entryDateTime;
	}
	public DateTime getExitDateTime() {
		return exitDateTime;
	}
	public void setExitDateTime(DateTime exitDateTime) {
		this.exitDateTime = exitDateTime;
	}
	public String getParkingStatus() {
		return parkingStatus;
	}
	public void setParkingStatus(String parkingStatus) {
		this.parkingStatus = parkingStatus;
	}
	public Boolean getExceptionFlag() {
		return exceptionFlag;
	}
	public void setExceptionFlag(Boolean exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	@Override
    public String toString() {
        return "SalesActivityDTO{" +
                "id='" + id + '\'' +
                ", lotId='" + lotId + '\'' +
                ", userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userLicensePlate='" + userLicensePlate + '\'' +
                ", planId='" + planId + '\'' +
                ", planName='" + planName + '\'' +
                ", planSubscriptionDate='" + planSubscriptionDate + '\'' +
                ", planExpiryDate='" + planExpiryDate + '\'' +
                ", chargeAmount='" + chargeAmount + '\'' +
                ", serviceAmount='" + serviceAmount + '\'' +
                ", netAmount='" + netAmount + '\'' +
                ", ppId='" + ppId + '\'' +
                ", entryDateTime='" + entryDateTime + '\'' +
                ", exitDateTime=" + exitDateTime +  '\'' +
                ", parkingStatus=" + parkingStatus +  '\'' +
                ", exceptionFlag=" + exceptionFlag +  '\'' +
                ", invoiceId=" + invoiceId +
                '}';
    }
}
