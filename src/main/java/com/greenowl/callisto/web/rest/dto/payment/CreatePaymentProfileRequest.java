package com.greenowl.callisto.web.rest.dto.payment;

import com.greenowl.callisto.domain.User;

public class CreatePaymentProfileRequest {
	
	private String login;
	private String stripeToken;
	
	private String cardToken;
	
	private String ccType;
	
	private String cardholderName;

	private String ccExpiryDate;

	public CreatePaymentProfileRequest(){
		
	}
	
	public CreatePaymentProfileRequest(String login, String stripeToken, String cardToken, String ccType, String cardholderName, String ccExpiryDate){
		this.login=login;
		this.stripeToken=stripeToken;
		this.cardToken=cardToken;
		this.ccType=ccType;
		this.cardholderName=cardholderName;
		this.ccExpiryDate=ccExpiryDate;
	}




	public String getStripeToken() {
		return stripeToken;
	}

	public void setStripeToken(String stripeToken) {
		this.stripeToken = stripeToken;
	}

	public String getCcType() {
		return ccType;
	}

	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getCcExpiryDate() {
		return ccExpiryDate;
	}

	public void setCcExpiryDate(String ccExpiryDate) {
		this.ccExpiryDate = ccExpiryDate;
	}

    public String getCardToken() {
		return cardToken;
	}
	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
    public String toString() {
        return "CreatePaymentProfileRequest{" +
                ", login='" + login + '\'' +
                ", stripeToken='" + stripeToken + '\'' +
                ", cardToken='" + cardToken + '\'' +
                ", ccType='" + ccType + '\'' +
                ", cardholderName='" + cardholderName + '\'' +
                ", ccExpiryDate='" + ccExpiryDate  +
                '}';
    }
}
