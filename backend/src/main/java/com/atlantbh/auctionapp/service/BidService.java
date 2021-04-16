package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Bid;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.repository.BidRepository;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.request.BidRequest;
import com.atlantbh.auctionapp.response.SimpleBidResponse;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;

    @Autowired
    public BidService(BidRepository bidRepository, PersonRepository personRepository, ProductRepository productRepository) {
        this.bidRepository = bidRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
    }

    public List<SimpleBidResponse> getBidsForProduct(Long id) {
        return bidRepository.getBidsForProduct(id);
    }

    public void add(BidRequest bidRequest) {
        Product product = productRepository.findById(bidRequest.getProductId()).orElseThrow(() -> new UnprocessableException("Wrong product id"));
        if (product.getStartPrice().compareTo(bidRequest.getAmount()) > 0) {
            throw new BadRequestException("Price can't be lower than the product start price");
        }
        if (product.getStartDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Auction is yet to start for this product");
        }
        if (product.getEndDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Auction ended for this product");
        }
        Long id = JwtTokenUtil.getRequestPersonId();
        if (id == null)
            throw new UnprocessableException("Invalid JWT signature");
        Person person = personRepository.findById(JwtTokenUtil.getRequestPersonId()).orElseThrow(() -> new UnprocessableException("Wrong person id"));
        if (product.getPerson().getId().equals(person.getId())) {
            throw new BadRequestException("You can't bid on your own product");
        }
        BigDecimal maxBid = bidRepository.getMaxBidFromPersonForProduct(person.getId(), product.getId());
        if (maxBid != null && (maxBid.compareTo(bidRequest.getAmount()) >= 0)) {
            throw new BadRequestException("Price can't be lower than your previous bid of $" + maxBid);
        }
        bidRepository.save(new Bid(bidRequest.getAmount(), person, product));
    }
}
