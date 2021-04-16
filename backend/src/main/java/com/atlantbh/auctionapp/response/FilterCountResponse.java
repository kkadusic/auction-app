package com.atlantbh.auctionapp.response;

import com.atlantbh.auctionapp.projection.ColorCountProjection;
import com.atlantbh.auctionapp.projection.SizeCountProjection;

import java.util.List;

public class FilterCountResponse {

    private List<ColorCountProjection> colors;
    private List<SizeCountProjection> sizes;
    private PriceCountResponse price;

    public FilterCountResponse() {
    }

    public FilterCountResponse(List<ColorCountProjection> colors, List<SizeCountProjection> sizes, PriceCountResponse price) {
        this.colors = colors;
        this.sizes = sizes;
        this.price = price;
    }

    public List<ColorCountProjection> getColors() {
        return colors;
    }

    public void setColors(List<ColorCountProjection> colors) {
        this.colors = colors;
    }

    public List<SizeCountProjection> getSizes() {
        return sizes;
    }

    public void setSizes(List<SizeCountProjection> sizes) {
        this.sizes = sizes;
    }

    public PriceCountResponse getPrice() {
        return price;
    }

    public void setPrice(PriceCountResponse price) {
        this.price = price;
    }
}
