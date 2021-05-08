package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.request.PageRequest;
import com.atlantbh.auctionapp.response.NotificationPageResponse;
import com.atlantbh.auctionapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<NotificationPageResponse> getNotifications(@Valid PageRequest pageRequest) {
        return ResponseEntity.ok(notificationService.getNotifications(pageRequest.getPage(), pageRequest.getSize()));
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkNotifications(@RequestParam List<Long> ids) {
        notificationService.checkNotifications(ids);
        return ResponseEntity.ok("Notifications checked");
    }
}
