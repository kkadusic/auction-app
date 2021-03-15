package com.atlantbh.auctionapp.response;

import java.util.List;
import java.util.Objects;

public class SubcategoriesResponse {

    private Long id;
    private String name;
    private List<SubcategoryResponse> subcategories;

    public SubcategoriesResponse() {
    }

    public SubcategoriesResponse(Long id, String name, List<SubcategoryResponse> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public SubcategoriesResponse(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubcategoryResponse> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryResponse> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(SubcategoryResponse subcategory) {
        subcategories.add(subcategory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubcategoriesResponse that = (SubcategoriesResponse) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
