package com.greenowl.callisto.web.rest.dto;

import java.io.Serializable;

public class PaymentProfileDTO  implements Serializable{
	
	private Long id;
	
	private Long userId;
	
	private String stripeToken;
	
	private String cardToken;
	
	private String ccType;
	
	private String cardholderName;

	private String ccExpiryDate;
	
	public PaymentProfileDTO(){}
	
	public PaymentProfileDTO(Long id, Long userId, String stripeToken, String cardToken, String ccType, String cardholderName, String ccExpiryDate){
		this.id=id;
		this.userId=userId;
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

    public String getCardToken() {
		return cardToken;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
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

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
    public String toString() {
        return "PaymentProfileDTO{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", stripeToken='" + stripeToken + '\'' +
                ", cardToken='" + cardToken + '\'' +
                ", ccType='" + ccType + '\'' +
                ", cardholderName='" + cardholderName + '\'' +
                ", ccExpiryDate=" + ccExpiryDate +
                '}';
    }
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentProfileDTO paymentProfileDTO = (PaymentProfileDTO) o;

        return id.equals(paymentProfileDTO.id);

    }

}
