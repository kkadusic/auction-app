package com.atlantbh.auctionapp.response;

import java.math.BigDecimal;

public class PriceCountResponse {

    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal avgPrice;
    private int[] prices;

    public PriceCountResponse() {
    }

    public PriceCountResponse(BigDecimal minPrice, BigDecimal maxPrice, BigDecimal avgPrice, int[] prices) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.avgPrice = avgPrice;
        this.prices = prices;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }

    public int[] getPrices() {
        return prices;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }
}
