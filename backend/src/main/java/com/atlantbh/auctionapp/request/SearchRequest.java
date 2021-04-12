package com.atlantbh.auctionapp.request;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;

import javax.validation.constraints.Min;

public class SearchRequest {

    private String query = "";
    private String category = "";
    private String subcategory = "";
    private String sort = "";
    private Integer minPrice = 0;
    private Integer maxPrice = 1000000;
    private Color color = null;
    private Size size = null;
    @Min(value = 0, message = "Page number can't be negative")
    private Integer page = 0;

    public SearchRequest() {
    }

    public SearchRequest(String query, String category, String subcategory, String sort, Integer minPrice,
                         Integer maxPrice, Color color, Size size,
                         @Min(value = 0, message = "Page number can't be negative") Integer page) {
        this.query = query;
        this.category = category;
        this.subcategory = subcategory;
        this.sort = sort;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.color = color;
        this.size = size;
        this.page = page;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
