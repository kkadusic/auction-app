package com.atlantbh.auctionapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "paypal")
public class PayPal {

    @Id
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String orderId;

    public PayPal() {
    }

    public PayPal(String orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
