package com.atlantbh.auctionapp.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PayPalRequest {

    @NotBlank(message = "Order id can't be blank")
    @Size(max = 255, message = "Order id can't be longer than 255 characters")
    private String orderId;

    public PayPalRequest() {
    }

    public PayPalRequest(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
