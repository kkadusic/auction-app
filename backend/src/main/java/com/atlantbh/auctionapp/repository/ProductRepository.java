package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.projection.ColorCountProjection;
import com.atlantbh.auctionapp.projection.ProductCountProjection;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.projection.SizeCountProjection;
import com.atlantbh.auctionapp.projection.UserProductProjection;
import com.atlantbh.auctionapp.projection.FullProductProjection;
import com.atlantbh.auctionapp.projection.WinnerProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.id, p.name, p.start_price AS startPrice, p.description, i.url AS imageUrl, c.name categoryName, s.name subcategoryName " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE p.featured = true AND i.featured = true AND p.start_date <= (now() + interval '2 hours') AND p.end_date > (now() + interval '2 hours') " +
            "ORDER BY RANDOM() " +
            "LIMIT 5",
            nativeQuery = true)
    List<SimpleProductProjection> getFeaturedRandomProducts();

    // Based on the date of creation
    @Query(value = "SELECT p.id, p.name, p.start_price AS startPrice, p.description, i.url AS imageUrl, c.name categoryName, s.name subcategoryName " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on p.person_id = p2.id " +
            "WHERE start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') AND i.featured = true AND p2.active " +
            "ORDER BY start_date DESC  " +
            "LIMIT 8",
            nativeQuery = true)
    List<SimpleProductProjection> getNewArrivalsProducts();

    // Based on the time left for bidding process
    @Query(value = "SELECT p.id, p.name, p.start_price AS startPrice, p.description, i.url AS imageUrl, c.name categoryName, s.name subcategoryName " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on p.person_id = p2.id " +
            "WHERE start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') AND i.featured = true AND p2.active " +
            "ORDER BY end_date " +
            "LIMIT 8",
            nativeQuery = true)
    List<SimpleProductProjection> getLastChanceProducts();

    @Query(value = "SELECT p.id, p.person_id personId, p.name, p.description, p.start_price startPrice, " +
            "p.start_date startDate, p.end_date endDate, " +
            "EXISTS(SELECT * FROM wishlist " +
            "WHERE product_id = :product_id AND person_id = :user_id) wished " +
            "FROM product p INNER JOIN person p2 on p.person_id = p2.id " +
            "WHERE p.id = :product_id AND p2.active",
            nativeQuery = true)
    Optional<FullProductProjection> getProduct(@Param("product_id") Long productId, @Param("user_id") Long userId);

    @Query(value = "SELECT pr.id, pr.name, pr.start_price startPrice, pr.description, " +
            "i.url AS imageUrl, c.name categoryName, s.name subcategoryName, pr.creation_date, " +
            "(SELECT count(id) FROM bid WHERE product_id = pr.id) bids, " +
            "(case when :id = -1 then false else EXISTS (SELECT 1 FROM wishlist WHERE product_id = pr.id AND person_id = :id) end) wished, " +
            "similarity(pr.name, :query) similarity " +
            "FROM product pr INNER JOIN image i on pr.id = i.product_id " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on pr.person_id = p2.id " +
            "WHERE p2.active AND (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :category = '' then true else lower(c.name) = :category end) " +
            "AND (case when :subcategory = '' then true else lower(s.name) = :subcategory end) " +
            "AND (case when :min_price <= 0 then true else start_price >= :min_price end) " +
            "AND (case when :max_price >= 1000000 then true else start_price <= :max_price end) " +
            "AND (case when :color = '' then true else pr.color = :color end) " +
            "AND (case when :size = '' then true else pr.size = :size end) " +
            "AND i.featured = true AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') " +
            "GROUP BY (pr.id, pr.name, pr.start_price, pr.description, i.url, c.name, s.name, pr.creation_date)",
            nativeQuery = true)
    Slice<SimpleProductProjection> search(String query, String tsquery, String category, String subcategory, Long id,
                                          @Param("min_price") Integer minPrice, @Param("max_price") Integer maxPrice,
                                          String color, String size, Pageable pageable);

    @Query(value = "SELECT c.name categoryName, s.name subcategoryName, count(s.name) " +
            "FROM product pr INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on pr.person_id = p2.id " +
            "WHERE p2.active AND (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :min_price <= 0 then true else start_price >= :min_price end) " +
            "AND (case when :max_price >= 1000000 then true else start_price <= :max_price end) " +
            "AND (case when :color = '' then true else pr.color = :color end) " +
            "AND (case when :size = '' then true else pr.size = :size end) " +
            "AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') " +
            "GROUP BY ROLLUP (c.name, s.name) " +
            "ORDER BY (c.name, s.name)",
            nativeQuery = true)
    List<ProductCountProjection> categoryCount(String query, String tsquery, @Param("min_price") Integer minPrice,
                                               @Param("max_price") Integer maxPrice, String color, String size);

    @Query(value = "SELECT color, count(color) " +
            "FROM product pr " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on pr.person_id = p2.id " +
            "WHERE p2.active AND (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :category = '' then true else lower(c.name) = lower(:category) end) " +
            "AND (case when :subcategory = '' then true else lower(s.name) = lower(:subcategory) end) " +
            "AND (case when :min_price <= 0 then true else start_price >= :min_price end) " +
            "AND (case when :max_price >= 1000000 then true else start_price <= :max_price end) " +
            "AND (case when :size = '' then true else pr.size = :size end) " +
            "AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') AND color IS NOT NULL AND pr.size IS NOT NULL GROUP BY color",
            nativeQuery = true)
    List<ColorCountProjection> colorCount(String query, String tsquery, String category, String subcategory,
                                          @Param("min_price") Integer minPrice, @Param("max_price") Integer maxPrice, String size);

    @Query(value = "SELECT pr.size, count(pr.size) " +
            "FROM product pr " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on pr.person_id = p2.id " +
            "WHERE p2.active AND (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :category = '' then true else lower(c.name) = lower(:category) end) " +
            "AND (case when :subcategory = '' then true else lower(s.name) = lower(:subcategory) end) " +
            "AND (case when :min_price <= 0 then true else start_price >= :min_price end) " +
            "AND (case when :max_price >= 1000000 then true else start_price <= :max_price end) " +
            "AND (case when :color = '' then true else pr.color = :color end) " +
            "AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') AND color IS NOT NULL AND pr.size IS NOT NULL GROUP BY pr.size",
            nativeQuery = true)
    List<SizeCountProjection> sizeCount(String query, String tsquery, String category, String subcategory,
                                        @Param("min_price") Integer minPrice, @Param("max_price") Integer maxPrice, String color);

    @Query(value = "SELECT p.id, p.name, i.url, max(b.amount) price, s.name subcategoryName, c.name categoryName, p.shipping, " +
            "p.start_date startDate, p.end_date endDate, (SELECT count(*) FROM bid b2 WHERE b2.product_id = p.id) bidCount, " +
            "(SELECT b2.person_id FROM bid b2 WHERE b2.product_id = p.id ORDER BY b2.amount DESC, b2.date LIMIT 1) personId, " +
            "(SELECT max(b2.amount) FROM bid b2 WHERE b2.product_id = p.id) maxBid, " +
            "(SELECT EXISTS(SELECT 1 FROM payment pa WHERE pa.product_id = p.id AND pa.person_id = :user_id)) paid " +
            "FROM product p LEFT OUTER JOIN image i on p.id = i.product_id LEFT OUTER JOIN bid b on p.id = b.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id INNER JOIN category c on c.id = s.category_id " +
            "WHERE b.person_id = :user_id AND (i.featured = true OR i.featured IS NULL) " +
            "GROUP BY (p.id, p.name, i.url, s.name, c.name, p.shipping, p.start_date, p.end_date) " +
            "ORDER BY p.end_date", nativeQuery = true)
    List<UserProductProjection> getUserBidProducts(@Param("user_id") Long userId);

    @Query(value = "SELECT EXISTS(SELECT 1 " +
            "FROM product pr INNER JOIN person p2 on pr.person_id = p2.id " +
            "WHERE p2.active AND (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.name || ' ' || pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours'))",
            nativeQuery = true)
    Boolean searchExists(String query, String tsquery);

    @Query(value = "SELECT start_price FROM product pr " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id INNER JOIN person p2 on pr.person_id = p2.id " +
            "WHERE p2.active AND (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :category = '' then true else lower(c.name) = lower(:category) end) " +
            "AND (case when :subcategory = '' then true else lower(s.name) = lower(:subcategory) end) " +
            "AND (case when :color = '' then true else pr.color = :color end) " +
            "AND (case when :size = '' then true else pr.size = :size end) " +
            "AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') ORDER BY start_price",
            nativeQuery = true)
    List<BigDecimal> prices(String query, String tsquery, String category, String subcategory, String color, String size);

    @Query(value = "SELECT p.id, p.name, i.url, p.start_price price, s.name subcategoryName, c.name categoryName, p.shipping, " +
            "p.start_date startDate, p.end_date endDate, count(b.id) bidCount, max(b.amount) maxBid, " +
            "(SELECT b2.person_id FROM bid b2 WHERE b2.product_id = p.id ORDER BY b2.amount DESC, b2.date LIMIT 1) personId, " +
            "(SELECT EXISTS(SELECT 1 FROM payment pa WHERE pa.product_id = p.id AND pa.person_id != :user_id)) paid " +
            "FROM product p LEFT OUTER JOIN image i on p.id = i.product_id LEFT OUTER JOIN bid b on p.id = b.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id INNER JOIN category c on c.id = s.category_id " +
            "WHERE p.person_id = :user_id AND (i.featured = true OR i.featured IS NULL) " +
            "GROUP BY (p.id, p.name, i.url, p.start_price, s.name, c.name, p.shipping, p.start_date, p.end_date) " +
            "ORDER BY p.creation_date DESC",
            nativeQuery = true)
    List<UserProductProjection> getUserProducts(@Param("user_id") Long userId);

    @Query(value = "SELECT * FROM product p " +
            "INNER JOIN person p2 on p2.id = p.person_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE p2.active AND p.id = :id",
            nativeQuery = true)
    Optional<Product> findByIdAndIsActive(Long id);

    @Query(value = "SELECT EXISTS " +
            "(SELECT 1 FROM product p INNER JOIN person p2 on p2.id = p.person_id " +
            "AND p2.active AND p.id = :id)",
            nativeQuery = true)
    Boolean existsByIdAndIsActive(Long id);

    @Query(value = "SELECT p.id, p.name, p2.url, max(b.amount) price, s.name subcategoryName, c.name categoryName, " +
            "p.start_date startDate, p.end_date endDate, (SELECT count(*) FROM bid b2 WHERE b2.product_id = p.id) bidCount, " +
            "(SELECT b2.person_id FROM bid b2 WHERE b2.product_id = p.id ORDER BY b2.amount DESC, b2.date LIMIT 1) personId, " +
            "(SELECT max(b2.amount) FROM bid b2 WHERE b2.product_id = p.id) maxBid " +
            "FROM product p LEFT OUTER JOIN image p2 on p.id = p2.product_id LEFT OUTER JOIN bid b on p.id = b.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id INNER JOIN category c on c.id = s.category_id " +
            "INNER JOIN wishlist w on p.id = w.product_id " +
            "WHERE w.person_id = :user_id AND (p2.featured = true OR p2.featured IS NULL) " +
            "GROUP BY (p.id, p.name, p2.url, s.name, c.name, p.start_date, p.end_date, w.date) " +
            "ORDER BY w.date DESC",
            nativeQuery = true)
    List<UserProductProjection> getUserWishlistProducts(@Param("user_id") Long userId);

    @Query(value = "SELECT p.id productId, p.name productName, " +
            "(SELECT max(b2.amount) FROM bid b2 INNER JOIN person p5 on b2.person_id = p5.id " +
            "WHERE p5.active AND b2.product_id = p.id) maxBid, " +
            "(SELECT p1.id FROM bid b1 INNER JOIN person p1 on p1.id = b1.person_id " +
            "WHERE b1.product_id = p.id AND p1.active ORDER BY b1.amount DESC, b1.date LIMIT 1) id," +
            "(SELECT p2.email FROM bid b2 INNER JOIN person p2 on p2.id = b2.person_id " +
            "WHERE b2.product_id = p.id AND p2.active ORDER BY b2.amount DESC, b2.date LIMIT 1) email," +
            "(SELECT p3.push_notify FROM bid b3 INNER JOIN person p3 on p3.id = b3.person_id " +
            "WHERE b3.product_id = p.id AND p3.active ORDER BY b3.amount DESC, b3.date LIMIT 1) pushNotify, " +
            "(SELECT p4.email_notify FROM bid b4 INNER JOIN person p4 on p4.id = b4.person_id " +
            "WHERE b4.product_id = p.id AND p4.active ORDER BY b4.amount DESC, b4.date LIMIT 1) emailNotify " +
            "FROM product p INNER JOIN bid b on p.id = b.product_id INNER JOIN person p2 on p.person_id = p2.id " +
            "WHERE end_date <= (now() + interval '2 hours') AND NOT notified " +
            "AND NOT EXISTS (SELECT 1 FROM payment p3 WHERE p3.product_id = p.id) " +
            "AND (SELECT EXISTS(SELECT 1 FROM bid b5 INNER JOIN person p5 on p5.id = b5.person_id " +
            "WHERE b5.product_id = p.id AND p5.active)) " +
            "AND p2.active GROUP BY (p.id, p.name)",
            nativeQuery = true)
    List<WinnerProjection> getNotNotifiedWinners();
}
