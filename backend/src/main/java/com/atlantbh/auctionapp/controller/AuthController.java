package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.exception.BadGatewayException;
import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.request.DeactivateRequest;
import com.atlantbh.auctionapp.request.ForgotPasswordRequest;
import com.atlantbh.auctionapp.request.LoginRequest;
import com.atlantbh.auctionapp.request.RegisterRequest;
import com.atlantbh.auctionapp.request.ResetPasswordRequest;
import com.atlantbh.auctionapp.request.TokenRequest;
import com.atlantbh.auctionapp.request.UpdateNotifRequest;
import com.atlantbh.auctionapp.request.UpdateProfileRequest;
import com.atlantbh.auctionapp.response.LoginResponse;
import com.atlantbh.auctionapp.response.RegisterResponse;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import com.atlantbh.auctionapp.service.PersonService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final PersonService personService;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, PersonService personService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.personService = personService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        Person person = personService.register(registerRequest);
        return ResponseEntity.ok(new RegisterResponse(person, jwtTokenUtil.generateToken(person)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        final Person person = personService.login(loginRequest);
        final String token = jwtTokenUtil.generateToken(person);
        return ResponseEntity.ok(new LoginResponse(person, token));
    }

    @PostMapping("/forgot_password")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
            @ApiResponse(code = 502, message = "Bad gateway", response = BadGatewayException.class),
    })
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPassRequest) {
        return ResponseEntity.ok(personService.forgotPassword(forgotPassRequest));
    }

    @PostMapping("/reset_password")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
    })
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPassRequest) {
        return ResponseEntity.ok(personService.resetPassword(resetPassRequest));
    }

    @PostMapping("/valid_token")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
    })
    public ResponseEntity<Boolean> validToken(@RequestBody @Valid TokenRequest tokenRequest) {
        return ResponseEntity.ok(personService.validToken(tokenRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<LoginResponse> update(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        Person person = personService.update(updateProfileRequest);
        return ResponseEntity.ok(new LoginResponse(person, jwtTokenUtil.generateToken(person)));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<String> deactivate(@RequestBody @Valid DeactivateRequest deactivateRequest) {
        personService.deactivate(deactivateRequest.getPassword());
        return ResponseEntity.ok("User deactivated");
    }

    @PostMapping("/notifications/update")
    public ResponseEntity<String> updateNotifications(@RequestBody @Valid UpdateNotifRequest updateNotifRequest) {
        personService.updateNotifications(updateNotifRequest);
        return ResponseEntity.ok("Notification preferences successfully updated");
    }
}
