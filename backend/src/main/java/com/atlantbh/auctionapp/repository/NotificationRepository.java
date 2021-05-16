package com.atlantbh.auctionapp.repository;

import com.atlantbh.auctionapp.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByPersonIdOrderByDateDesc(Long uuid, Pageable pageable);
}
