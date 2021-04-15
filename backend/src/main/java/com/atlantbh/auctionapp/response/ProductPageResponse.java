package com.atlantbh.auctionapp.response;

import com.atlantbh.auctionapp.projection.SimpleProductProjection;

import java.util.List;

public class ProductPageResponse {

    private List<SimpleProductProjection> products;
    private Boolean lastPage;
    private String didYouMean;

    public ProductPageResponse() {
    }

    public ProductPageResponse(List<SimpleProductProjection> products, Boolean lastPage) {
        this.products = products;
        this.lastPage = lastPage;
    }

    public ProductPageResponse(List<SimpleProductProjection> products, Boolean lastPage, String didYouMean) {
        this.products = products;
        this.lastPage = lastPage;
        this.didYouMean = didYouMean;
    }

    public List<SimpleProductProjection> getProducts() {
        return products;
    }

    public void setProducts(List<SimpleProductProjection> products) {
        this.products = products;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public String getDidYouMean() {
        return didYouMean;
    }

    public void setDidYouMean(String didYouMean) {
        this.didYouMean = didYouMean;
    }
}
