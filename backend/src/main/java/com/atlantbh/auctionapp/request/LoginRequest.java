package com.atlantbh.auctionapp.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email format is not valid")
    private String email;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(@NotBlank(message = "Email can't be empty") @Email(message = "Email format is not valid")
                                String email, @NotBlank(message = "Password can't be empty")
                        @Size(min = 8, message = "Password must contain at least 8 characters") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
