package com.greenowl.callisto.web.rest.dto;

import org.joda.time.DateTime;

import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.domain.User;

public class SalesRecordDTO {
	private Long id;

	private Long lotId;

	private Long userId;

	private Long planId;
	
	private String userEmail;

	private String userPhoneNumber;

	private String userLicensePlate;
	
	private Double chargeAmount;

	private Double serviceAmount;

	private Double netAmount;

	private Long ppId;
	
	private String type;
	public SalesRecordDTO() {

	}

	public SalesRecordDTO(SalesRecord record, User user) {
		this.id=record.getId();
		this.lotId=record.getLotId();
		this.userId=user.getId();
		this.planId=record.getPlanId();
		this.userEmail=user.getLogin();
		this.userPhoneNumber=user.getMobileNumber();
		this.userLicensePlate=user.getLicensePlate();
		this.chargeAmount=record.getChargeAmount();
		this.serviceAmount=record.getServiceAmount();
		this.netAmount=record.getNetAmount();
		this.ppId=record.getPpId();
		this.type=record.getType();
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

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
