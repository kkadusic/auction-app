package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.repository.SubcategoryRepository;
import com.atlantbh.auctionapp.response.SubcategoriesResponse;
import com.atlantbh.auctionapp.response.SubcategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;

    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }

    public List<Subcategory> getRandomSubcategories() {
        return subcategoryRepository.getRandomSubcategories();
    }

    public List<SubcategoriesResponse> getSubcategories() {
        List<SubcategoriesResponse> response = new ArrayList<>();
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        for (Subcategory subcategory : subcategories) {
            int i = response.indexOf(new SubcategoriesResponse(subcategory.getCategory().getName()));
            if (i == -1) {
                response.add(new SubcategoriesResponse(
                        subcategory.getCategory().getId(),
                        subcategory.getCategory().getName(),
                        new ArrayList<>()
                ));
                i = response.size() - 1;
            }
            response.get(i).addSubcategory(new SubcategoryResponse(subcategory.getId(), subcategory.getName()));
        }
        response.sort(Comparator.comparing(SubcategoriesResponse::getName));
        return response;
    }
}
