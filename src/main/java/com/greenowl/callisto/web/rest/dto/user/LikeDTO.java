package com.greenowl.callisto.web.rest.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikeDTO {

    private String login;

    @JsonProperty("date_liked")
    private Long dateLiked;

    @JsonProperty("dido_id")
    private Long didoId;

    private String message;

    public LikeDTO() {
    }

    public LikeDTO(Long didoId, String login, Long dateLiked, String message) {
        this.didoId = didoId;
        this.login = login;
        this.dateLiked = dateLiked;
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getDateLiked() {
        return dateLiked;
    }

    public void setDateLiked(Long dateLiked) {
        this.dateLiked = dateLiked;
    }

    public Long getDidoId() {
        return didoId;
    }

    public void setDidoId(Long didoId) {
        this.didoId = didoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LikeDTO{" +
                "login='" + login + '\'' +
                ", dateLiked=" + dateLiked +
                ", didoId=" + didoId +
                ", message='" + message + '\'' +
                '}';
    }
}
