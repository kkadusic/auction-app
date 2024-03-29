package com.atlantbh.auctionapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(min = 13, max = 19)
    @Column(nullable = false)
    private String cardNumber;

    @Min(2000)
    @Max(9999)
    @Column(nullable = false)
    private Integer expirationYear;

    @Column(nullable = false)
    @Min(1)
    @Max(12)
    private Integer expirationMonth;

    @Column(nullable = false)
    @Min(100)
    @Max(9999)
    private Integer cvc;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(nullable = false)
    private String stripeCardId;

    @Column(nullable = false)
    private Boolean saved;

    public Card() {
    }

    public Card(Person person) {
        this.person = person;
    }

    public Card(@NotBlank String name, @NotBlank @Size(min = 13, max = 19) String cardNumber,
                @Min(2000) @Max(9999) Integer expirationYear, @Min(1) @Max(2) Integer expirationMonth,
                @Min(100) @Max(999) Integer cvc, Person person, Boolean saved) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expirationYear = expirationYear;
        this.expirationMonth = expirationMonth;
        this.cvc = cvc;
        this.person = person;
        this.saved = saved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getStripeCardId() {
        return stripeCardId;
    }

    public void setStripeCardId(String stripeCardId) {
        this.stripeCardId = stripeCardId;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null)
            return null;
        return String.join("", Collections.nCopies(cardNumber.length() - 4, "*")) + cardNumber.substring(cardNumber.length() - 4);
    }
}
