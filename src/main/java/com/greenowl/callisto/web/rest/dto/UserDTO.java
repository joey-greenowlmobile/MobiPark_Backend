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
    private String login; // Email

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = 2, max = 5)
    private String langKey;

    @Size(max = 50)
    private String stripeToken;

    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String login, String firstName, String lastName,
                   String langKey, List<String> roles, String stripeToken) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.langKey = langKey;
        this.roles = roles;
        this.stripeToken=stripeToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
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


    public void setLogin(String login) {
        this.login = login;
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

	@Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
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

        return login.equals(userDTO.login);

    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
