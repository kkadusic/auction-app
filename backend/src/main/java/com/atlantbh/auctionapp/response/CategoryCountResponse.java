package com.atlantbh.auctionapp.response;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

public class CategoryCountResponse implements Comparable<CategoryCountResponse> {

    private String name;
    private Integer count;
    private Set<CountResponse> subcategories;

    public CategoryCountResponse() {
    }

    public CategoryCountResponse(String name, Integer count, Set<CountResponse> subcategories) {
        this.name = name;
        this.count = count;
        this.subcategories = subcategories;
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

    public Set<CountResponse> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<CountResponse> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(CountResponse countResponse) {
        subcategories.add(countResponse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryCountResponse that = (CategoryCountResponse) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int compareTo(CategoryCountResponse o) {
        return Comparator.comparing(CategoryCountResponse::getCount).reversed()
                .thenComparing(CategoryCountResponse::getName)
                .compare(this, o);
    }
}
