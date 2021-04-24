package com.atlantbh.auctionapp.request;

import com.atlantbh.auctionapp.enumeration.Gender;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UpdateProfileRequest {

    @NotBlank(message = "First name can't be blank")
    @Size(min = 2, message = "First name must have at least 2 characters")
    @Size(max = 100, message = "First name can't be longer than 100 characters")
    private String firstName;

    @NotBlank(message = "Last name can't be blank")
    @Size(min = 2, message = "Last name must have at least 2 characters")
    @Size(max = 100, message = "Last name can't be longer than 100 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    private LocalDateTime birthDate;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Wrong email format")
    @Size(max = 100, message = "Email can't be longer than 100 characters")
    private String email;

    @NotBlank(message = "Phone can't be blank")
    @Size(max = 32, message = "Phone can't be longer than 32 characters")
    private String phoneNumber;

    private Boolean verified = false;

    private String imageUrl = "http://www.gnd.center/bpm/resources/img/avatar-placeholder.gif";

    @Valid
    private CardRequest card;

    @Size(max = 255, message = "Street can't be longer than 255 characters")
    private String street;

    @Size(max = 255, message = "Country can't be longer than 255 characters")
    private String country;

    @Size(max = 255, message = "City can't be longer than 255 characters")
    private String city;

    @Size(max = 255, message = "State can't be longer than 255 characters")
    private String state;

    @Size(max = 32, message = "Zip code can't be longer than 32 characters")
    private String zip;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String firstName, String lastName, Gender gender, LocalDateTime dateOfBirth,
                                String email, String phone, String photo, CardRequest card, String street,
                                String country, String city, String state, String zip) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = dateOfBirth;
        this.email = email;
        this.phoneNumber = phone;
        this.imageUrl = photo;
        this.card = card;
        this.street = street;
        this.country = country;
        this.city = city;
        this.state = state;
        this.zip = zip;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CardRequest getCard() {
        return card;
    }

    public void setCard(CardRequest card) {
        this.card = card;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
