package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.NotFoundException;
import com.atlantbh.auctionapp.model.Image;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.response.FullProductResponse;
import com.atlantbh.auctionapp.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<SimpleProductProjection> getFeaturedRandomProducts() {
        return productRepository.getFeaturedRandomProducts();
    }

    public List<SimpleProductProjection> getNewProducts() {
        return productRepository.getNewArrivalsProducts();
    }

    public List<SimpleProductProjection> getLastProducts() {
        return productRepository.getLastChanceProducts();
    }

    public ProductResponse getProduct(Long productId, Long userId) {
        List<FullProductResponse> fullProducts = productRepository.getProduct(productId, userId);
        if (fullProducts.isEmpty()) {
            throw new NotFoundException("Wrong product id");
        }

        ProductResponse productResponse = new ProductResponse(
                fullProducts.get(0).getId(),
                fullProducts.get(0).getName(),
                fullProducts.get(0).getDescription(),
                fullProducts.get(0).getStartPrice(),
                fullProducts.get(0).getEndDate(),
                fullProducts.get(0).getWished(),
                new ArrayList<>());

        if (fullProducts.get(0).getImageId() != null) {
            for (var fullProductResponse : fullProducts) {
                productResponse.getImages().add(new Image(
                        fullProductResponse.getImageId(),
                        fullProductResponse.getImageUrl(),
                        fullProductResponse.getImageFeatured()
                ));
            }
        }

        return productResponse;
    }
}
