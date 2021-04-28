package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.projection.ReceiptProjection;
import com.atlantbh.auctionapp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/receipt")
    public ResponseEntity<ReceiptProjection> getReceipt(@RequestParam Long productId) {
        return ResponseEntity.ok(paymentService.getReceipt(productId));
    }
}
