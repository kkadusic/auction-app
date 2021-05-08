package com.atlantbh.auctionapp.request;

import javax.validation.constraints.Min;

public class PageRequest {

    @Min(value = 0, message = "Page number can't be negative")
    private Integer page = 0;

    @Min(value = 0, message = "Page size can't be negative")
    private Integer size = 7;

    public PageRequest() {
    }

    public PageRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
