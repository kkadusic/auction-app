package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.projection.PersonInfoProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByEmail(String email);

    Person findByEmail(String email);

    Optional<Person> findByEmailAndActiveIsTrue(String email);

    Optional<Person> findByIdAndActiveIsTrue(Long id);

    boolean existsByIdAndActiveIsTrue(Long id);

    @Query(value = "SELECT p.first_name || ' ' || p.last_name AS name, p.image_url AS photo, p.rating " +
            "FROM person p " +
            "WHERE id = :id",
            nativeQuery = true)
    Optional<PersonInfoProj> getUserInfo(Long id);
}
