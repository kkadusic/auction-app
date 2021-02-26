package com.atlantbh.auctionapp.response;

import com.atlantbh.auctionapp.model.Person;

public class RegisterResponse {
    private Person person;
    private String token;

    public RegisterResponse() {
    }

    public RegisterResponse(Person person, String token) {
        this.person = person;
        this.token = token;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
