package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.projection.ReceiptProjection;
import com.atlantbh.auctionapp.repository.PaymentRepository;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public ReceiptProjection getReceipt(Long productId) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        return paymentRepository.getReceipt(personId, productId)
                .orElseThrow(() -> new BadRequestException("You don't have a receipt for this product"));
    }
}
