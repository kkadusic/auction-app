package com.atlantbh.auctionapp.request;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductRequest {

    @NotBlank(message = "Name can't be blank")
    @javax.validation.constraints.Size(max = 60, message = "Name can't be longer than 60 characters")
    private String name;

    @NotNull(message = "Subcategory id is required")
    private Long subcategoryId;

    @javax.validation.constraints.Size(max = 700, message = "Description can't be longer than 700 characters")
    private String description;

    private Color color;
    private Size size;

    private List<String> images;

    @NotNull(message = "Start price is required")
    @DecimalMin(value = "0.01", message = "Start price can't be lower than $0.01")
    @DecimalMax(value = "999999.99", message = "Start price can't be higher than $999999.99")
    private BigDecimal startPrice;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @NotBlank(message = "Street can't be blank")
    private String street;

    @NotBlank(message = "City can't be blank")
    private String city;

    @NotBlank(message = "Country can't be blank")
    private String country;

    @NotNull(message = "Zip can't be blank")
    private String zip;

    @NotBlank(message = "Phone can't be blank")
    @javax.validation.constraints.Size(max = 15, message = "Phone can't be longer than 15 characters")
    private String phone;

    private Boolean shipping = false;
    private Boolean featured = false;

    private CardRequest card;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getShipping() {
        return shipping;
    }

    public void setShipping(Boolean shipping) {
        this.shipping = shipping;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public CardRequest getCard() {
        return card;
    }

    public void setCard(CardRequest card) {
        this.card = card;
    }
}
