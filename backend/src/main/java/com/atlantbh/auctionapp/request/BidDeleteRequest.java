package com.atlantbh.auctionapp.request;

public class BidDeleteRequest {

    private Long productId;

    public BidDeleteRequest() {
    }

    public BidDeleteRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
