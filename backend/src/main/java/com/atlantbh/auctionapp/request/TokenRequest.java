package com.atlantbh.auctionapp.request;

import javax.validation.constraints.NotNull;

public class TokenRequest {

    @NotNull(message = "Token is required.")
    private String token;

    public TokenRequest() {
    }

    public TokenRequest(@NotNull(message = "Token is required.") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
