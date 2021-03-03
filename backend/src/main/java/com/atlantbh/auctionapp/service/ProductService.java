package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getFeaturedRandomProducts() {
        return productRepository.getFeaturedRandomProducts();
    }

    public List<Product> getNewProducts() {
        return productRepository.getNewArrivalsProducts();
    }

    public List<Product> getLastProducts() {
        return productRepository.getLastChanceProducts();
    }
}
