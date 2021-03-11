package com.atlantbh.auctionapp.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BidRequest {

    @NotNull(message = "Price must be supplied")
    @DecimalMin(value = "0.01", message = "Price can't be less than $0.01")
    @DecimalMax(value = "999999.99", message = "Price can't be more than $99999999.99")
    private BigDecimal price;

    @NotNull(message = "Product must be supplied")
    private Long productId;

    public BidRequest() {
    }

    public BidRequest(@NotNull(message = "Price must be supplied")
                      @Min(value = 0, message = "Price can't be less than 0") BigDecimal price,
                      @NotNull(message = "Product must be supplied") Long productId) {
        this.price = price;
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
