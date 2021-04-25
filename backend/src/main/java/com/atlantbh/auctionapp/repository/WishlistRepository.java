package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByPersonAndProduct(Person person, Product product);

    Boolean existsByPersonAndProduct(Person person, Product product);
}
