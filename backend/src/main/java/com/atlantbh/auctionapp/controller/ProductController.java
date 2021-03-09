package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.response.ProductResponse;
import com.atlantbh.auctionapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<SimpleProductProjection>> getFeaturedRandomProducts() {
        return ResponseEntity.ok(productService.getFeaturedRandomProducts());
    }

    @GetMapping("/new")
    public ResponseEntity<List<SimpleProductProjection>> getNewProducts() {
        return ResponseEntity.ok(productService.getNewProducts());
    }

    @GetMapping("/last")
    public ResponseEntity<List<SimpleProductProjection>> getLastProducts() {
        return ResponseEntity.ok(productService.getLastProducts());
    }

    @GetMapping("/")
    public ResponseEntity<ProductResponse> getProduct(@RequestParam(name = "product_id") Long productId,
                                                      @RequestParam(name = "user_id", defaultValue = "") Long userId) {
        return ResponseEntity.ok(productService.getProduct(productId, userId));
    }
}
