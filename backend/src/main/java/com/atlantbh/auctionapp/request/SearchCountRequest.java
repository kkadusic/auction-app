package com.atlantbh.auctionapp.request;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;

public class SearchCountRequest {

    private String query = "";
    private Integer minPrice = 0;
    private Integer maxPrice = 1000000;
    private Color color = null;
    private Size size = null;

    public SearchCountRequest() {
    }

    public SearchCountRequest(String query, Integer minPrice, Integer maxPrice, Color color, Size size) {
        this.query = query;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.color = color;
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
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
}
