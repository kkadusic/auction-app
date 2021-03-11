package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Bid;
import com.atlantbh.auctionapp.response.SimpleBidResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query(value = "SELECT b.id, p.first_name firstName, p.last_name lastName, p.image_url as imageUrl, b.date, b.amount, b.person_id personId " +
            "FROM bid b " +
            "INNER JOIN person p on p.id = b.person_id " +
            "WHERE b.product_id = :id " +
            "ORDER BY b.amount DESC, b.date",
            nativeQuery = true)
    List<SimpleBidResponse> getBidsForProduct(Long id);

    @Query(value = "SELECT MAX(amount) " +
            "FROM bid " +
            "WHERE person_id = :person_id AND product_id = :product_id",
            nativeQuery = true)
    BigDecimal getMaxBidFromPersonForProduct(@Param("person_id") Long personId, @Param("product_id") Long productId);
}
