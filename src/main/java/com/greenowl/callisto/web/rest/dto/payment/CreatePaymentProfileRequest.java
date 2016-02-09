package com.greenowl.callisto.web.rest.dto.payment;


import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePaymentProfileRequest {

    private CardProfile card;

    private String created;

    private String id;

    @JsonProperty("livemode")
    private Boolean liveMode;

    private Boolean used;

    public CreatePaymentProfileRequest() {

    }

    public CreatePaymentProfileRequest(CardProfile card, String created, String id, Boolean liveMode, Boolean used) {
        this.card = card;
        this.created = created;
        this.id = id;
        this.liveMode = liveMode;
        this.used = used;
    }


    public CardProfile getCard() {
        return card;
    }

    public void setCard(CardProfile card) {
        this.card = card;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getLiveMode() {
        return liveMode;
    }

    public void setLiveMode(Boolean liveMode) {
        this.liveMode = liveMode;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "CreatePaymentProfileRequest{" +
                ", card='" + card.toString() + '\'' +
                ", created='" + created + '\'' +
                ", id='" + id + '\'' +
                ", liveMode='" + liveMode + '\'' +
                ", used='" + used +
                '}';
    }
}
