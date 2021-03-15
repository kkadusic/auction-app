package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.response.SubcategoriesResponse;
import com.atlantbh.auctionapp.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @GetMapping()
    public ResponseEntity<List<SubcategoriesResponse>> getSubcategories() {
        return ResponseEntity.ok(subcategoryService.getSubcategories());
    }

    @GetMapping("/random")
    public ResponseEntity<List<Subcategory>> getRandomSubcategories() {
        return ResponseEntity.ok(subcategoryService.getRandomSubcategories());
    }
}
