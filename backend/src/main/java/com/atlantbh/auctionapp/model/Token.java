package com.atlantbh.auctionapp.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Type(type = "uuid-char")
    @Column(nullable = false)
    private UUID token;

    @Column(nullable = false)
    private Boolean used = false;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Token() {
    }

    public Token(UUID token, Person person) {
        this.token = token;
        this.person = person;
    }

    public Token(LocalDateTime dateCreated, UUID token, Boolean used, Person person) {
        this.dateCreated = dateCreated;
        this.token = token;
        this.used = used;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
