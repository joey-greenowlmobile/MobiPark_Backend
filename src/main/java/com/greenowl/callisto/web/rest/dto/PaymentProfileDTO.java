package com.greenowl.callisto.web.rest.dto;

import java.io.Serializable;

public class PaymentProfileDTO  implements Serializable{
	
	private Long id;
	
	private Long userId;
	
	private String cardToken;
	
	private Long expMonth;
	
	private Long expYear;
	
	private String last4;

	
	public PaymentProfileDTO(){}
	
	public PaymentProfileDTO(Long id, Long userId,  String cardToken, Long expMonth, Long expYear , String last4){
		this.id=id;
		this.userId=userId;
		this.cardToken=cardToken;
		this.expMonth=expMonth;
		this.expYear=expYear;
		this.last4=last4;
	}
	

    public String getCardToken() {
		return cardToken;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
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

	public Long getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(Long expMonth) {
		this.expMonth = expMonth;
	}

	public Long getExpYear() {
		return expYear;
	}

	public void setExpYear(Long expYear) {
		this.expYear = expYear;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	@Override
    public String toString() {
        return "PaymentProfileDTO{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", cardToken='" + cardToken + '\'' +
                ", expMonth='" + expMonth + '\'' +
                ", expYear=" + expYear +  '\'' +
                ", last4=" + last4 +
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
