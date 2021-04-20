package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Subcategory;
import com.atlantbh.auctionapp.projection.SimpleSubcategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    @Query(value = "SELECT sc.id, sc.name, c.name categoryName, sc.image_url url, min(start_price) startPrice " +
            "FROM subcategory sc INNER JOIN category c on c.id = sc.category_id " +
            "INNER JOIN product p on sc.id = p.subcategory_id " +
            "GROUP BY (sc.id, sc.name, c.name, sc.image_url) " +
            "ORDER BY RANDOM() LIMIT 4",
            nativeQuery = true)
    List<SimpleSubcategoryProjection> getRandomSubcategories();
}
