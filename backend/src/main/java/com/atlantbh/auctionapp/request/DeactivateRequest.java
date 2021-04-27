package com.atlantbh.auctionapp.request;

import javax.validation.constraints.NotBlank;

public class DeactivateRequest {

    @NotBlank(message = "Password can't be blank")
    private String password;

    public DeactivateRequest() {
    }

    public DeactivateRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
