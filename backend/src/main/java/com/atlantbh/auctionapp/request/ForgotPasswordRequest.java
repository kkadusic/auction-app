package com.atlantbh.auctionapp.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ForgotPasswordRequest {

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    @Size(max = 320, message = "Email can't be longer than 320 characters")
    private String email;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(@NotBlank(message = "Email can't be blank") @Email(message = "Wrong email format")
                                 @Size(max = 100, message = "Email can't be longer than 100 characters") String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
