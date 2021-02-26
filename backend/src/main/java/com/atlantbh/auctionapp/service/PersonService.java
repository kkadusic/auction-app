package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.ConflictException;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person register(RegisterRequest registerRequest) {
        if (personRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Email already in use");
        }
        Person person = personRepository.save(new Person(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()))
        );
        person.setPassword(null);
        return person;
    }
}
