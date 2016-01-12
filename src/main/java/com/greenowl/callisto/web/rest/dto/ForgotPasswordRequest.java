package com.greenowl.callisto.web.rest.dto;

import org.hibernate.validator.constraints.Email;

public class ForgotPasswordRequest {

    @Email
    private String email;

    public ForgotPasswordRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ForgotPasswordRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
