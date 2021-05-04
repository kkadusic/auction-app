package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.UnauthorizedException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Bid;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.projection.SimpleBidProjection;
import com.atlantbh.auctionapp.repository.BidRepository;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.request.BidDeleteRequest;
import com.atlantbh.auctionapp.request.BidRequest;
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

    public List<SimpleBidProjection> getBidsForProduct(Long id) {
        if (!productRepository.existsByIdAndIsActive(id))
            throw new UnprocessableException("Wrong product id");
        return bidRepository.getBidsForProduct(id);
    }

    public void add(BidRequest bidRequest) {
        Product product = productRepository.findByIdAndIsActive(bidRequest.getProductId()).orElseThrow(() -> new UnprocessableException("Wrong product id"));
        if (product.getStartPrice().compareTo(bidRequest.getAmount()) > 0) {
            throw new BadRequestException("Price can't be lower than the product start price");
        }
        if (product.getStartDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Auction is yet to start for this product");
        }
        if (product.getEndDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Auction ended for this product");
        }
        Long personId = JwtTokenUtil.getRequestPersonId();
        if (personId == null) {
            throw new UnprocessableException("Invalid JWT signature");
        }
        Person person = personRepository.findById(personId).
                orElseThrow(() -> new UnauthorizedException("Wrong person id"));
        if (product.getPerson().getId().equals(person.getId())) {
            throw new BadRequestException("You can't bid on your own product");
        }
        BigDecimal maxBid = bidRepository.getMaxBidFromPersonForProduct(person.getId(), product.getId());
        if (maxBid != null && (maxBid.compareTo(bidRequest.getAmount()) >= 0)) {
            throw new BadRequestException("Price can't be lower than your previous bid of $" + maxBid);
        }
        bidRepository.save(new Bid(bidRequest.getAmount(), person, product));
    }

    public void remove(BidDeleteRequest bidDeleteRequest) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        if (bidDeleteRequest.getProductId() == null)
            throw new BadRequestException("A product id has to be supplied");
        List<Bid> bids = bidRepository.findAllByProductId(personId, bidDeleteRequest.getProductId());
        if (bids.size() == 0)
            throw new BadRequestException("No bids found for this product or the auction has finished");
        bidRepository.deleteAll(bids);
    }
}
