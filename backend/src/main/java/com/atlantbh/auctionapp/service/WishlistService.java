package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.UnauthorizedException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.model.Wishlist;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.repository.WishlistRepository;
import com.atlantbh.auctionapp.request.WishlistRequest;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, PersonRepository personRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
    }

    public void add(WishlistRequest wishlistRequest) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new UnauthorizedException("Wrong person id"));
        Product product = productRepository.findByIdAndIsActive(wishlistRequest.getProductId())
                .orElseThrow(() -> new UnprocessableException("Wrong product id"));
        if (wishlistRepository.existsByPersonAndProduct(person, product))
            throw new BadRequestException("You already wishlisted this product");
        wishlistRepository.save(new Wishlist(person, product));
    }

    public void remove(WishlistRequest wishlistRequest) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new UnauthorizedException("Wrong person id"));
        Product product = productRepository.findByIdAndIsActive(wishlistRequest.getProductId())
                .orElseThrow(() -> new UnprocessableException("Wrong product id"));
        Wishlist wishlist = wishlistRepository.findByPersonAndProduct(person, product)
                .orElseThrow(() -> new BadRequestException("You didn't wishlist this product"));
        wishlistRepository.delete(wishlist);
    }
}
