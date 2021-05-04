package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Payment;
import com.atlantbh.auctionapp.projection.ReceiptProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM payment WHERE product_id = :product_id AND person_id = :user_id)",
            nativeQuery = true)
    Boolean isProductPaidByUser(@Param("user_id") Long userId, @Param("product_id") Long productId);

    @Query(value = "SELECT COALESCE(p.stripe_charge_id, '') || COALESCE(p3.order_id, '') id, p.date date, p.amount amount, " +
            "p.street street, p.country country, p.city city, p.zip zip, p.phone phone, " +
            "p2.street sellerStreet, p2.country sellerCountry, p2.city sellerCity, " +
            "p2.zip sellerZip, p2.phone_number sellerPhone, p2.name, p2.description description, " +
            "p2.shipping shipping, p2.color color, p2.size size, p4.first_name || ' ' || p4.last_name sellerName " +
            "FROM payment p " +
            "INNER JOIN product p2 on p2.id = p.product_id " +
            "LEFT OUTER JOIN paypal p3 on p3.id = p.paypal_id " +
            "INNER JOIN person p4 on p4.id = p2.person_id " +
            "WHERE p.product_id = :product_id AND p.person_id = :user_id",
            nativeQuery = true)
    Optional<ReceiptProjection> getReceipt(@Param("user_id") Long userId, @Param("product_id") Long productId);
}
