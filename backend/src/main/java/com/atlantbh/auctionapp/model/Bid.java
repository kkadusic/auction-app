package com.atlantbh.auctionapp.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DecimalMin("0.50")
    @DecimalMax("9999999.00")
    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Bid() {
    }

    public Bid(@PositiveOrZero @DecimalMin("0.05") @DecimalMax("9999999.00") BigDecimal amount, LocalDateTime date, Person person, Product product) {
        this.amount = amount;
        this.date = date;
        this.person = person;
        this.product = product;
    }

    public Bid(@PositiveOrZero @DecimalMin("0.05") @DecimalMax("9999999.00") BigDecimal amount, Person person, Product product) {
        this.amount = amount;
        this.person = person;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
