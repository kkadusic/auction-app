package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.repository.BidRepository;
import com.atlantbh.auctionapp.response.SimpleBidResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;

    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<SimpleBidResponse> getBidsForProduct(Long id) {
        return bidRepository.getBidsForProduct(id);
    }
}
