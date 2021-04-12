package com.atlantbh.auctionapp.request;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;

public class FilterCountRequest {

    private String query = "";
    private String category = "";
    private String subcategory = "";
    private Integer minPrice = 0;
    private Integer maxPrice = 1000000;
    private Color color = null;
    private Size size = null;

    public FilterCountRequest() {
    }

    public FilterCountRequest(String query, String category, String subcategory, Integer minPrice, Integer maxPrice, Color color, Size size) {
        this.query = query;
        this.category = category;
        this.subcategory = subcategory;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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
