package com.atlantbh.auctionapp.request;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BidRequest {

    @NotNull(message = "Amount must be supplied")
    @DecimalMin(value = "0.01", message = "Amount can't be less than $0.01")
    @DecimalMax(value = "9999999", message = "Amount can't be more than $9999999")
    private BigDecimal amount;

    @NotNull(message = "Product must be supplied")
    private Long productId;

    public BidRequest() {
    }

    public BidRequest(@NotNull(message = "Amount must be supplied")
                      @Min(value = 0, message = "Amount can't be less than 0") BigDecimal amount,
                      @NotNull(message = "Product must be supplied") Long productId) {
        this.amount = amount;
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
