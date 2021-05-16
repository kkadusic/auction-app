package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.model.Notification;
import com.atlantbh.auctionapp.repository.NotificationRepository;
import com.atlantbh.auctionapp.response.NotificationResponse;
import com.atlantbh.auctionapp.utilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class PushService {

    private final SimpMessageSendingOperations notificationBroadcaster;
    private final NotificationRepository notificationRepository;

    @Autowired
    public PushService(SimpMessageSendingOperations notificationBroadcaster, NotificationRepository notificationRepository) {
        this.notificationBroadcaster = notificationBroadcaster;
        this.notificationRepository = notificationRepository;
    }

    public void broadcastNotification(Notification notification, String receiver) {
        notificationRepository.save(notification);
        NotificationResponse notificationResponse = new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getDescription(),
                notification.getProduct().getId(),
                notification.getProduct().getName(),
                StringUtil.getNotificationUrl(notification.getType(), notification.getProduct()),
                notification.getChecked()
        );
        notificationBroadcaster.convertAndSend("/topic/" + receiver, notificationResponse);
    }
}
