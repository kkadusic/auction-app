package com.atlantbh.auctionapp.service;


import com.atlantbh.auctionapp.exception.NotFoundException;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDetails loadUserByUsername(String email) {
        Person person = personRepository.findByEmail(email);
        if (person == null) {
            throw new NotFoundException();
        }
        return new PersonDetails(person);
    }
}
