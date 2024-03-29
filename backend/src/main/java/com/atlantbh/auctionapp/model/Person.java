package com.atlantbh.auctionapp.model;

import com.atlantbh.auctionapp.enumeration.Gender;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime birthDate;

    @Size(max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean verified = false;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Size(max = 320)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    @Size(max = 128)
    private String password;

    private String street;

    private String city;

    @Size(max = 32)
    private String zip;

    private String state;

    private String country;

    private String imageUrl = "http://www.gnd.center/bpm/resources/img/avatar-placeholder.gif";

    @Column(nullable = false)
    private Boolean emailNotify = false;

    @Column(nullable = false)
    private Boolean pushNotify = true;

    @Column(nullable = false)
    private Boolean active = true;

    private String stripeCustomerId;

    @DecimalMin("0")
    @DecimalMax("5")
    @Column(precision = 7, scale = 6, nullable = false)
    private BigDecimal rating = BigDecimal.ZERO;

    @Min(0)
    @Column(nullable = false)
    private Integer ratingCount = 0;

    public Person() {
    }

    public Person(Long id) {
        this.id = id;
    }

    public Person(@NotBlank @Size(min = 2, max = 50) String firstName,
                  @NotBlank @Size(min = 2, max = 50) String lastName,
                  @NotBlank @Size(max = 320) @Email String email,
                  @NotBlank @Size(max = 128) String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Boolean getEmailNotify() {
        return emailNotify;
    }

    public void setEmailNotify(Boolean emailNotify) {
        this.emailNotify = emailNotify;
    }

    public Boolean getPushNotify() {
        return pushNotify;
    }

    public void setPushNotify(Boolean pushNotify) {
        this.pushNotify = pushNotify;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
}
