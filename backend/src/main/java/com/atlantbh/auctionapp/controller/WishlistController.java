package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.request.WishlistRequest;
import com.atlantbh.auctionapp.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody @Valid WishlistRequest wishlistRequest) {
        wishlistService.add(wishlistRequest);
        return ResponseEntity.ok("Product added to wishlist");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> remove(@RequestBody @Valid WishlistRequest wishlistRequest) {
        wishlistService.remove(wishlistRequest);
        return ResponseEntity.ok("Product removed from wishlist");
    }
}
