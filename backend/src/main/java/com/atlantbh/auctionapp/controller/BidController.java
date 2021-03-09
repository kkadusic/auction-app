package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.response.SimpleBidResponse;
import com.atlantbh.auctionapp.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @GetMapping("/product/")
    public ResponseEntity<List<SimpleBidResponse>> getBidsForProduct(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(bidService.getBidsForProduct(id));
    }
}
