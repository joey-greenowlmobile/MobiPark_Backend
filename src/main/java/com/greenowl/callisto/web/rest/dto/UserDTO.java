package com.greenowl.callisto.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO implements Serializable {

    private Long id;

    @Email
    @NotNull
    @Size(min = 1, max = 50)
    private String email; // Email

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = 2, max = 5)
    private String langKey;

    @Size(max = 50)
    private String stripeToken;

    private String licensePlate;

    private String mobileNumber;

    private String region;

    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String firstName, String lastName,
                   String langKey, List<String> roles, String stripeToken, String licensePlate, String mobileNumber, String region) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.langKey = langKey;
        this.roles = roles;
        this.stripeToken = stripeToken;
        this.licensePlate = licensePlate;
        this.mobileNumber = mobileNumber;
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLangKey() {
        return langKey;
    }

    public List<String> getRoles() {
        return roles;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", region='" + region + '\'' +
                ", langKey='" + langKey + '\'' +
                ", stripeToken='" + stripeToken + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return email.equals(userDTO.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
