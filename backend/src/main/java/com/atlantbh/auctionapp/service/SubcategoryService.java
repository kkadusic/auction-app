package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
