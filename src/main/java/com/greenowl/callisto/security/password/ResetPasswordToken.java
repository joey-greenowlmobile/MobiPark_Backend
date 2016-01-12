package com.greenowl.callisto.security.password;


import org.joda.time.DateTime;

public class ResetPasswordToken {

    private String token;

    private DateTime expireDate;

    public ResetPasswordToken(String token, DateTime expireDate) {
        this.token = token;
        this.expireDate = expireDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(DateTime expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "ResetPasswordToken{" +
                "token='" + token + '\'' +
                ", expireDate=" + expireDate +
                '}';
    }
}
