package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.request.LoginRequest;
import com.atlantbh.auctionapp.request.RegisterRequest;
import com.atlantbh.auctionapp.response.LoginResponse;
import com.atlantbh.auctionapp.response.RegisterResponse;
import com.atlantbh.auctionapp.security.JsonWebToken;
import com.atlantbh.auctionapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PersonService personService;

    @Autowired
    public AuthController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        Person person = personService.register(registerRequest);
        return ResponseEntity.ok(new RegisterResponse(person, JsonWebToken.generateJWTToken(person)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        Person person = personService.login(loginRequest);
        return ResponseEntity.ok(new LoginResponse(person, JsonWebToken.generateJWTToken(person)));
    }
}