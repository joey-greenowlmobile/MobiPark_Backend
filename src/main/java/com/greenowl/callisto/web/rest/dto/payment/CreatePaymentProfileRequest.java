package com.greenowl.callisto.web.rest.dto.payment;

public class CreatePaymentProfileRequest {

    private CardProfile card;

    private String created;

    private String id;

    private Boolean livemode;

    private Boolean used;

    public CreatePaymentProfileRequest() {

    }

    public CreatePaymentProfileRequest(CardProfile card, String created, String id, Boolean livemode, Boolean used) {
        this.card = card;
        this.created = created;
        this.id = id;
        this.livemode = livemode;
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

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
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
                ", livemode='" + livemode + '\'' +
                ", used='" + used +
                '}';
    }
}
