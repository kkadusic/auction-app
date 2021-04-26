package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Category;
import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.projection.SimpleSubcategoryProjection;
import com.atlantbh.auctionapp.projection.SubcategoryProjection;
import com.atlantbh.auctionapp.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<SubcategoryProjection> getRandomSubcategories() {
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

    public List<SimpleSubcategoryProjection> getSubcategoriesForCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new UnprocessableException("Wrong category id"));
        return subcategoryRepository.findAllByCategory(category);
    }
}
