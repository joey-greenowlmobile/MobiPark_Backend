package com.greenowl.callisto.web.rest.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

/**
 * Created by greenowl on 15-09-28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialRegistrationRequest {

    @NotNull
    private Long id;

    @JsonProperty(value = "platform_id")
    @NotNull
    private String platformId;

    private String platform;

    @NotNull
    @Email
    private String email;

    public SocialRegistrationRequest() {
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
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

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SocialRegistrationRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
