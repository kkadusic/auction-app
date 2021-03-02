package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByEmail(String email);

    Person findByEmail(String email);
}
