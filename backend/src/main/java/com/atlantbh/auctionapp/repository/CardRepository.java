package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Card;
import com.atlantbh.auctionapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByNameAndCardNumberAndExpirationYearAndExpirationMonthAndCvcAndPerson(String name, String cardNumber,
                                                                                             Integer expirationYear,
                                                                                             Integer expirationMonth,
                                                                                             Integer cvc, Person person);

    Optional<Card> findByCardNumberAndCvcAndPerson(String cardNumber, Integer cvc, Person person);

    Optional<Card> findByPersonIdAndSavedIsTrue(Long personId);
}
