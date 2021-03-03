package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.id, p.name, p.start_price, p.description " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "WHERE p.featured = true AND p.start_date <= now() AND p.end_date > now() " +
            "ORDER BY RANDOM() " +
            "LIMIT 5",
            nativeQuery = true)
    List<Product> getFeaturedRandomProducts();

    // Based on the date of creation
    @Query(value = "SELECT p.id, p.name, p.start_price, p.description " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "WHERE start_date <= now() AND end_date > now() " +
            "ORDER BY start_date DESC " +
            "LIMIT 8",
            nativeQuery = true)
    List<Product> getNewArrivalsProducts();

    // Based on the time left for bidding process
    @Query(value = "SELECT p.id, p.name, p.start_price, p.description " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "WHERE start_date <= now() AND end_date > now() " +
            "ORDER BY end_date " +
            "LIMIT 8",
            nativeQuery = true)
    List<Product> getLastChanceProducts();
}
