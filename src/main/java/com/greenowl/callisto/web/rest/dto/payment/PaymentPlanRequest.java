package com.greenowl.callisto.web.rest.dto.payment;

public class PaymentPlanRequest {
	private CreatePaymentProfileRequest token;
	private Long planId;
	
	public PaymentPlanRequest(){}
	public PaymentPlanRequest(CreatePaymentProfileRequest token, Long planId){
		this.token=token;
		this.planId=planId;
	}
	public CreatePaymentProfileRequest getToken() {
		return token;
	}
	public void setToken(CreatePaymentProfileRequest token) {
		this.token = token;
	}
	public Long getPlanId() {
		return planId;
	}
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	@Override
	public String toString(){
		return "PaymentPlanRequest{"+
				", token='" + token.toString() + '\'' +
				", planId='" + planId+
				"}";
	}
}
