package com.atlantbh.auctionapp.response;

import com.atlantbh.auctionapp.model.Image;
import com.atlantbh.auctionapp.projection.FullProductProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {

    private Long id;
    private Long personId;
    private String name;
    private String description;
    private BigDecimal startPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean wished;
    private List<Image> images;

    public ProductResponse() {
    }

    public ProductResponse(FullProductProjection product, List<Image> images) {
        this.id = product.getId();
        this.personId = product.getPersonId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.startPrice = product.getStartPrice();
        this.startDate = product.getStartDate();
        this.endDate = product.getEndDate();
        this.wished = product.getWished();
        this.images = images;
    }

    public ProductResponse(Long id, Long personId, String name, String description, BigDecimal startPrice, LocalDateTime startDate, LocalDateTime endDate, Boolean wished, List<Image> images) {
        this.id = id;
        this.personId = personId;
        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.wished = wished;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getWished() {
        return wished;
    }

    public void setWished(Boolean wished) {
        this.wished = wished;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
