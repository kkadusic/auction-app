package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Bid;
import com.atlantbh.auctionapp.projection.SimpleBidProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query(value = "SELECT b.id, p.first_name firstName, p.last_name lastName, p.image_url as imageUrl, b.date, b.amount, b.person_id personId " +
            "FROM bid b " +
            "INNER JOIN person p on p.id = b.person_id " +
            "WHERE p.active AND b.product_id = :id " +
            "ORDER BY b.amount DESC, b.date",
            nativeQuery = true)
    List<SimpleBidProjection> getBidsForProduct(Long id);

    @Query(value = "SELECT MAX(amount) " +
            "FROM bid b " +
            "INNER JOIN person p on p.id = b.person_id " +
            "WHERE p.active AND b.person_id = :person_id AND b.product_id = :product_id",
            nativeQuery = true)
    BigDecimal getMaxBidFromPersonForProduct(@Param("person_id") Long personId, @Param("product_id") Long productId);

    @Query(value = "SELECT * FROM bid b " +
            "INNER JOIN product p on p.id = b.product_id " +
            "WHERE b.person_id = :person_id AND b.product_id = :product_id " +
            "AND p.start_date <= (now() + interval '2 hours') AND p.end_date > (now() + interval '2 hours')",
            nativeQuery = true)
    List<Bid> findAllByProductId(@Param("person_id") Long personId, @Param("product_id") Long productId);

    List<Bid> findAllByProductId(Long productId);

    @Query(value = "SELECT * FROM " +
            "bid b WHERE product_id = :product_id " +
            "ORDER BY b.amount DESC, b.date " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Bid> getHighestBidForProduct(@Param("product_id") Long productId);
}
