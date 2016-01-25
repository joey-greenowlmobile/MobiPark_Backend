package com.greenowl.callisto.web.rest.dto.payment;

public class CardProfile {
	private String country;
	private Long expMonth;
	private Long expYear;
	private String last4;
	public CardProfile(){};
	public CardProfile(String country, Long expMonth, Long expYear, String last4){
		this.country= country;
		this.expMonth=expMonth;
		this.expYear=expYear;
		this.last4=last4;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
        return "CardProfile{" +
                ", country='" + country + '\'' +
                ", expMonth='" + expMonth.toString() + '\'' +
                ", expYear='" + expYear.toString() + '\'' +
                ", last4='" + last4  +
                '}';
    }
}
