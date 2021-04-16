package com.atlantbh.auctionapp.response;

import java.util.Comparator;

public class CountResponse implements Comparable<CountResponse> {

    private String name;
    private Integer count;

    public CountResponse() {
    }

    public CountResponse(String name, Integer count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public int compareTo(CountResponse o) {
        return Comparator.comparing(CountResponse::getCount).reversed()
                .thenComparing(CountResponse::getName)
                .compare(this, o);
    }
}
