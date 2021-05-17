package com.atlantbh.auctionapp.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RateRequest {

    @NotNull(message = "Product id is required")
    private Long productId;

    @Min(value = 1, message = "Rating must be greater than 0")
    @Max(value = 5, message = "Rating must be less than 6")
    @NotNull(message = "Rating is required")
    private Integer rating;

    public RateRequest() {
    }

    public RateRequest(Long productId, Integer rating) {
        this.productId = productId;
        this.rating = rating;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
