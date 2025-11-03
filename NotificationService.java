package com.rwtool.service;

import com.rwtool.model.Notification;
import com.rwtool.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification notifyAdmin(String type, String title, String message) {
        Notification n = new Notification();
        n.setId(UUID.randomUUID().toString());
        n.setAudience("ADMIN");
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        n.setCreatedAt(LocalDateTime.now());
        n.setReadFlag(false);
        return notificationRepository.save(n);
    }

    @Transactional
    public Notification notifyUser(String email, String type, String title, String message) {
        Notification n = new Notification();
        n.setId(UUID.randomUUID().toString());
        n.setAudience("USER");
        n.setRecipientEmail(email);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        n.setCreatedAt(LocalDateTime.now());
        n.setReadFlag(false);
        return notificationRepository.save(n);
    }

    public List<Notification> getAdminNotifications(boolean unreadOnly) {
        return unreadOnly
                ? notificationRepository.findByAudienceAndReadFlagOrderByCreatedAtDesc("ADMIN", false)
                : notificationRepository.findByAudienceOrderByCreatedAtDesc("ADMIN");
    }

    public List<Notification> getUserNotifications(String email, boolean unreadOnly) {
        return unreadOnly
                ? notificationRepository.findByRecipientEmailAndReadFlagOrderByCreatedAtDesc(email, false)
                : notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    @Transactional
    public void markRead(String id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setReadFlag(true);
            notificationRepository.save(n);
        });
    }

    @Transactional
    public int markAllReadForAdmin() {
        List<Notification> list = notificationRepository.findByAudienceAndReadFlagOrderByCreatedAtDesc("ADMIN", false);
        list.forEach(n -> n.setReadFlag(true));
        notificationRepository.saveAll(list);
        return list.size();
    }

    @Transactional
    public int markAllReadForUser(String email) {
        List<Notification> list = notificationRepository.findByRecipientEmailAndReadFlagOrderByCreatedAtDesc(email, false);
        list.forEach(n -> n.setReadFlag(true));
        notificationRepository.saveAll(list);
        return list.size();
    }
}


