package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.PayPal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayPalRepository extends JpaRepository<PayPal, Long> {
}
