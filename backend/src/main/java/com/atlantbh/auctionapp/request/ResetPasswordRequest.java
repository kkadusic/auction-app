package com.atlantbh.auctionapp.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class ResetPasswordRequest {

    @NotNull(message = "Token is required")
    private UUID token;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Size(max = 255, message = "Password can't be longer than 255 characters")
    private String password;

    public ResetPasswordRequest(@NotNull(message = "Token is required") UUID token,
                                @NotBlank(message = "Password can't be blank")
                                @Size(min = 8, message = "Password must have at least 8 characters")
                                @Size(max = 255, message = "Password can't be longer than 255 characters") String password) {
        this.token = token;
        this.password = password;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
