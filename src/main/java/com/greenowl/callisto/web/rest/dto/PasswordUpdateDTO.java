package com.greenowl.callisto.web.rest.dto;


public class PasswordUpdateDTO {

    private String password;

    public PasswordUpdateDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordUpdateDTO{" +
                "password='" + password + '\'' +
                '}';
    }
}
