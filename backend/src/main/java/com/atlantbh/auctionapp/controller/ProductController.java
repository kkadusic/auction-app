package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.projection.UserProductProjection;
import com.atlantbh.auctionapp.request.FilterCountRequest;
import com.atlantbh.auctionapp.request.PaymentRequest;
import com.atlantbh.auctionapp.request.ProductRequest;
import com.atlantbh.auctionapp.request.SearchCountRequest;
import com.atlantbh.auctionapp.request.SearchRequest;
import com.atlantbh.auctionapp.request.WishlistRequest;
import com.atlantbh.auctionapp.response.CategoryCountResponse;
import com.atlantbh.auctionapp.response.FilterCountResponse;
import com.atlantbh.auctionapp.response.FilterResponse;
import com.atlantbh.auctionapp.response.ProductPageResponse;
import com.atlantbh.auctionapp.response.ProductResponse;
import com.atlantbh.auctionapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @GetMapping
    public ResponseEntity<ProductResponse> getProduct(@RequestParam Long productId,
                                                      @RequestParam(defaultValue = "") Long userId) {
        return ResponseEntity.ok(productService.getProduct(productId, userId));
    }


    @GetMapping("/search")
    public ResponseEntity<ProductPageResponse> search(@Valid SearchRequest searchRequest) {
        return ResponseEntity.ok(productService.search(
                searchRequest.getQuery(),
                searchRequest.getCategory(),
                searchRequest.getSubcategory(),
                searchRequest.getPage(),
                searchRequest.getSort(),
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                searchRequest.getColor(),
                searchRequest.getSize()
        ));
    }

    @GetMapping("/search/count")
    public ResponseEntity<List<CategoryCountResponse>> searchCount(@Valid SearchCountRequest searchCountRequest) {
        return ResponseEntity.ok(productService.searchCount(
                searchCountRequest.getQuery(),
                searchCountRequest.getMinPrice(),
                searchCountRequest.getMaxPrice(),
                searchCountRequest.getColor(),
                searchCountRequest.getSize()
        ));
    }

    @GetMapping("/filter/count")
    public ResponseEntity<FilterCountResponse> filterCount(@Valid FilterCountRequest filterCountRequest) {
        return ResponseEntity.ok(productService.filterCount(
                filterCountRequest.getQuery(),
                filterCountRequest.getCategory(),
                filterCountRequest.getSubcategory(),
                filterCountRequest.getMinPrice(),
                filterCountRequest.getMaxPrice(),
                filterCountRequest.getColor(),
                filterCountRequest.getSize()
        ));
    }

    @GetMapping("/user/bid")
    public ResponseEntity<List<UserProductProjection>> getUserBidProducts() {
        return ResponseEntity.ok(productService.getUserBidProducts());
    }

    @GetMapping("/filters")
    public ResponseEntity<FilterResponse> getFilters() {
        return ResponseEntity.ok(new FilterResponse(Color.values(), Size.values()));
    }

    @PostMapping("/add")
    public ResponseEntity<Long> add(@RequestBody @Valid ProductRequest productRequest) {
        Long productId = productService.add(productRequest);
        return ResponseEntity.ok(productId);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestBody @Valid WishlistRequest productRequest) {
        productService.remove(productRequest.getProductId());
        return ResponseEntity.ok("Product removed");
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserProductProjection>> getUserProducts() {
        return ResponseEntity.ok(productService.getUserProducts());
    }

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestBody @Valid PaymentRequest paymentRequest) {
        productService.pay(paymentRequest);
        return ResponseEntity.ok("Product paid");
    }

    @GetMapping("/user/wishlist")
    public ResponseEntity<List<UserProductProjection>> getUserWishlistProducts() {
        return ResponseEntity.ok(productService.getUserWishlistProducts());
    }

    @GetMapping("/related")
    public ResponseEntity<List<SimpleProductProjection>> getRelatedProducts(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(productService.getRelatedProducts(id));
    }
}
