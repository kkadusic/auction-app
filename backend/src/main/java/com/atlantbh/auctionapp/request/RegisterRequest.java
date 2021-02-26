package com.atlantbh.auctionapp.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email format is not valid")
    private String email;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(@NotBlank(message = "First name can't be empty") String firstName,
                           @NotBlank(message = "Last name can't be empty") String lastName,
                           @NotBlank(message = "Email can't be empty")
                           @Email(message = "Email format is not valid") String email,
                           @NotBlank(message = "Password can't be empty")
                           @Size(min = 8, message = "Password must contain at least 8 characters") String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
