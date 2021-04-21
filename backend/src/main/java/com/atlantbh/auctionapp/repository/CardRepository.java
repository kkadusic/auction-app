package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByNameAndCardNumberAndExpirationYearAndExpirationMonthAndCvc(String name, String cardNumber,
                                                                                    Integer expirationYear,
                                                                                    Integer expirationMonth,
                                                                                    Integer cvc);
}