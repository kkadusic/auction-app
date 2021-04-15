package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.projection.ColorCountProjection;
import com.atlantbh.auctionapp.projection.ProductCountProjection;
import com.atlantbh.auctionapp.projection.SimpleProductProjection;
import com.atlantbh.auctionapp.projection.SizeCountProjection;
import com.atlantbh.auctionapp.projection.UserProductProjection;
import com.atlantbh.auctionapp.response.FullProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') AND i.featured = true " +
            "ORDER BY start_date DESC " +
            "LIMIT 8",
            nativeQuery = true)
    List<SimpleProductProjection> getNewArrivalsProducts();

    // Based on the time left for bidding process
    @Query(value = "SELECT p.id, p.name, p.start_price AS startPrice, p.description, i.url AS imageUrl, c.name categoryName, s.name subcategoryName " +
            "FROM product p " +
            "INNER JOIN image i ON p.id = i.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') AND i.featured = true " +
            "ORDER BY end_date " +
            "LIMIT 8",
            nativeQuery = true)
    List<SimpleProductProjection> getLastChanceProducts();

    @Query(value = "SELECT p.id, p.person_id personId, p.name, p.description, p.start_price startPrice, p.start_date startDate, p.end_date endDate, " +
            "EXISTS(SELECT * FROM wishlist " +
            "WHERE product_id = :product_id AND person_id = :user_id) wished, i.id AS imageId, i.url AS imageUrl, i.featured AS photoFeatured " +
            "FROM product p LEFT OUTER JOIN image i on p.id = i.product_id " +
            "WHERE p.id = :product_id " +
            "ORDER BY i.featured DESC",
            nativeQuery = true)
    List<FullProductResponse> getProduct(@Param("product_id") Long productId, @Param("user_id") Long userId);

    @Query(value = "SELECT pr.id, pr.name, pr.start_price startPrice, pr.description, " +
            "i.url AS imageUrl, c.name categoryName, s.name subcategoryName, pr.creation_date, " +
            "(SELECT count(id) FROM bid WHERE product_id = pr.id) bids, " +
            "similarity(pr.name, :query) similarity " +
            "FROM product pr INNER JOIN image i on pr.id = i.product_id " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
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
    Slice<SimpleProductProjection> search(String query, String tsquery, String category, String subcategory,
                                          @Param("min_price") Integer minPrice, @Param("max_price") Integer maxPrice,
                                          String color, String size, Pageable pageable);

    @Query(value = "SELECT c.name categoryName, s.name subcategoryName, count(s.name) " +
            "FROM product pr INNER JOIN image i on pr.id = i.product_id " +
            "                INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "                INNER JOIN category c on c.id = s.category_id " +
            "WHERE (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :min_price <= 0 then true else start_price >= :min_price end) " +
            "AND (case when :max_price >= 1000000 then true else start_price <= :max_price end) " +
            "AND (case when :color = '' then true else pr.color = :color end) " +
            "AND (case when :size = '' then true else pr.size = :size end) " +
            "AND i.featured = true AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') " +
            "GROUP BY (c.name, s.name) " +
            "ORDER BY (c.name, s.name)",
            nativeQuery = true)
    List<ProductCountProjection> categoryCount(String query, String tsquery, @Param("min_price") Integer minPrice,
                                               @Param("max_price") Integer maxPrice, String color, String size);

    @Query(value = "SELECT color, count(color) " +
            "FROM product pr " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
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
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
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

    @Query(value = "SELECT p.id, p.name, i.url, max(b.amount) price, s.name subcategoryName, c.name categoryName, " +
            "p.start_date startDate, p.end_date endDate, (SELECT count(*) FROM bid b2 WHERE b2.product_id = p.id) bidCount, " +
            "(SELECT b2.person_id FROM bid b2 WHERE b2.product_id = p.id ORDER BY b2.amount DESC, b2.date LIMIT 1) personId, " +
            "(SELECT max(b2.amount) FROM bid b2 WHERE b2.product_id = p.id) maxBid " +
            "FROM product p LEFT OUTER JOIN image i on p.id = i.product_id LEFT OUTER JOIN bid b on p.id = b.product_id " +
            "INNER JOIN subcategory s on s.id = p.subcategory_id INNER JOIN category c on c.id = s.category_id " +
            "WHERE b.person_id = :user_id AND (i.featured = true OR i.featured IS NULL) " +
            "GROUP BY (p.id, p.name, i.url, s.name, c.name, p.start_date, p.end_date) " +
            "ORDER BY p.end_date",
            nativeQuery = true)
    List<UserProductProjection> getUserBidProducts(@Param("user_id") Long userId);

    @Query(value = "SELECT EXISTS(SELECT 1 " +
            "FROM product pr " +
            "WHERE (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND start_date <= now() AND end_date > now())",
            nativeQuery = true)
    Boolean searchExists(String query, String tsquery);

    @Query(value = "SELECT start_price FROM product pr " +
            "INNER JOIN subcategory s on s.id = pr.subcategory_id " +
            "INNER JOIN category c on c.id = s.category_id " +
            "WHERE (lower(pr.name) LIKE lower('%' || :query || '%') OR pr.name % :query OR " +
            "to_tsvector('english', pr.description) @@ to_tsquery('english', :tsquery)) " +
            "AND (case when :category = '' then true else lower(c.name) = lower(:category) end) " +
            "AND (case when :subcategory = '' then true else lower(s.name) = lower(:subcategory) end) " +
            "AND (case when :color = '' then true else pr.color = :color end) " +
            "AND (case when :size = '' then true else pr.size = :size end) " +
            "AND start_date <= (now() + interval '2 hours') AND end_date > (now() + interval '2 hours') ORDER BY start_price",
            nativeQuery = true)
    List<BigDecimal> prices(String query, String tsquery, String category, String subcategory, String color, String size);
}
