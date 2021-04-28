package com.atlantbh.auctionapp.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PaymentRequest {

    @NotNull(message = "Product id is required")
    private Long productId;

    @NotBlank(message = "Street can't be blank")
    @Size(max = 255, message = "Address can't be longer than 255 characters")
    private String street;

    @NotBlank(message = "City can't be blank")
    @Size(max = 255, message = "City can't be longer than 255 characters")
    private String city;

    @NotBlank(message = "Country can't be blank")
    @Size(max = 255, message = "Country can't be longer than 255 characters")
    private String country;

    @NotNull(message = "Zip can't be blank")
    @Size(max = 32, message = "Zip code can't be longer than 32 characters")
    private String zip;

    @NotBlank(message = "Phone can't be blank")
    @Size(max = 32, message = "Phone can't be longer than 32 characters")
    private String phoneNumber;

    @Valid
    private CardRequest card;

    public PaymentRequest() {
    }

    public PaymentRequest(Long productId, String street, String city, String country, String zip, String phoneNumber, CardRequest card) {
        this.productId = productId;
        this.street = street;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.card = card;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CardRequest getCard() {
        return card;
    }

    public void setCard(CardRequest card) {
        this.card = card;
    }
}
