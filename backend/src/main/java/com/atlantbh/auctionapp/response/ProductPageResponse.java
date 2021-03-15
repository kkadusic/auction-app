package com.atlantbh.auctionapp.response;

import java.util.List;

public class ProductPageResponse {

    private List<SimpleProductResponse> products;
    private Boolean lastPage;

    public ProductPageResponse() {
    }

    public ProductPageResponse(List<SimpleProductResponse> products, Boolean lastPage) {
        this.products = products;
        this.lastPage = lastPage;
    }

    public List<SimpleProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<SimpleProductResponse> products) {
        this.products = products;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }
}
