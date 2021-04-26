package com.atlantbh.auctionapp.response;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;

public class FilterResponse {

    private Color[] colors;
    private Size[] sizes;

    public FilterResponse() {
    }

    public FilterResponse(Color[] colors, Size[] sizes) {
        this.colors = colors;
        this.sizes = sizes;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public Size[] getSizes() {
        return sizes;
    }

    public void setSizes(Size[] sizes) {
        this.sizes = sizes;
    }
}
