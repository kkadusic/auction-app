package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.model.Notification;
import com.atlantbh.auctionapp.repository.NotificationRepository;
import com.atlantbh.auctionapp.response.NotificationPageResponse;
import com.atlantbh.auctionapp.response.NotificationResponse;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import com.atlantbh.auctionapp.utilities.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationPageResponse getNotifications(Integer page, Integer size) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findAllByPersonIdOrderByDateDesc(personId, pageRequest);
        return new NotificationPageResponse(notifications.stream().map(notification ->
                new NotificationResponse(
                        notification.getId(),
                        notification.getType(),
                        notification.getDescription(),
                        notification.getProduct().getId(),
                        notification.getProduct().getName(),
                        StringUtil.getNotificationUrl(notification.getType(), notification.getProduct()),
                        notification.getChecked()
                )
        ).collect(Collectors.toList()), !notifications.hasNext());
    }

    public void checkNotifications(List<Long> ids) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        List<Notification> notifications = notificationRepository.findAllById(ids);
        for (Notification notification : notifications) {
            if (!notification.getPerson().getId().equals(personId))
                throw new BadRequestException("One or more notifications can't be checked");
            notification.setChecked(true);
        }
        notificationRepository.saveAll(notifications);
    }
}
