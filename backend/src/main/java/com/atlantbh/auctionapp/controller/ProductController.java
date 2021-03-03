package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/featured/random")
    public ResponseEntity<List<Product>> getFeaturedRandomProducts() {
        return ResponseEntity.ok(productService.getFeaturedRandomProducts());
    }

    @GetMapping("/new")
    public ResponseEntity<List<Product>> getNewProducts() {
        return ResponseEntity.ok(productService.getNewProducts());
    }

    @GetMapping("/last")
    public ResponseEntity<List<Product>> getLastProducts() {
        return ResponseEntity.ok(productService.getLastProducts());
    }
}
