package com.greenowl.callisto.web.rest.dto;

/**
 * Created by greenowl on 15-11-05.
 */
public class PasswordResetDTO {

    private String token;

    private String password;

    public PasswordResetDTO() {
    }

    public PasswordResetDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordResetDTO{" +
                "token='" + token + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
