package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    boolean existsByEmail(String email);

}
