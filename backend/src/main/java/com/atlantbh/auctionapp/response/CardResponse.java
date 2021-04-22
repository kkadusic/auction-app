package com.atlantbh.auctionapp.response;

import com.atlantbh.auctionapp.model.Card;

public class CardResponse {

    private String name;
    private String cardNumber;
    private Integer expirationYear;
    private Integer expirationMonth;
    private Integer cvc;

    public CardResponse() {
    }

    public CardResponse(Card card) {
        this.name = card.getName();
        this.cardNumber = card.getMaskedCardNumber();
        this.expirationYear = card.getExpirationYear();
        this.expirationMonth = card.getExpirationMonth();
        this.cvc = card.getCvc();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getCvc() {
        return cvc;
    }

    public void setCvc(Integer cvc) {
        this.cvc = cvc;
    }
}
