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
import javax.validation.constraints.NotNull;

/**
 * A payment profile.
 */
@Entity
@Table(name = "T_CUSTOMER_PAYMENT_PROFILE")
public class PaymentProfile extends AbstractAuditingEntity implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;
	
    @Column(name = "stripe_token", nullable = false)
    private String stripeToken;

    @Column(name = "card_token", nullable = false)
    private String cardToken;

    @Column(name = "cc_type", nullable = false)
    private String ccType;

    @Column(name = "cardholder_name", nullable = false)
    private String cardholderName;

    @Column(name = "cc_expiry_date", nullable = false)
    private String ccExpiryDate;
    
    @Column(name = "active", nullable = false)
    private Boolean active;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User profileHolder; 

    public PaymentProfile(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public User getProfileHolder() {
		return profileHolder;
	}

	public void setProfileHolder(User profileHolder) {
		this.profileHolder = profileHolder;
	}
    
    
 
}	

