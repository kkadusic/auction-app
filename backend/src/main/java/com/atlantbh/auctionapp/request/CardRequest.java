package com.atlantbh.auctionapp.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CardRequest {

    @NotBlank(message = "Name can't be blank")
    @Size(max = 255, message = "Name can't be longer than 255 characters")
    private String name;

    @NotBlank(message = "Card number can't be blank")
    @Size(min = 13, max = 19, message = "Card number must contain between 13 and 19 digits")
    private String cardNumber;

    @NotNull(message = "Expiration year is required")
    @Min(value = 2000, message = "Expiration year can't be before 2000")
    @Max(value = 9999, message = "Expiration year can't be after 9999")
    private Integer expirationYear;

    @NotNull(message = "Expiration month is required")
    @Min(value = 1, message = "Expiration month can't be less than 1")
    @Max(value = 12, message = "Expiration month can't be more than 12")
    private Integer expirationMonth;

    @NotNull(message = "CVC is required")
    @Min(value = 100, message = "Cvc can't be less than 3 characters")
    @Max(value = 9999, message = "Cvc can't be more than 4 characters")
    private Integer cvc;

    public CardRequest() {
    }

    public CardRequest(String name, String cardNumber, Integer expirationYear, Integer expirationMonth, Integer cvc) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expirationYear = expirationYear;
        this.expirationMonth = expirationMonth;
        this.cvc = cvc;
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
