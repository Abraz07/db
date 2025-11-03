package com.rwtool.repository;

import com.rwtool.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByAudienceOrderByCreatedAtDesc(String audience);
    List<Notification> findByAudienceAndReadFlagOrderByCreatedAtDesc(String audience, boolean readFlag);
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String recipientEmail);
    List<Notification> findByRecipientEmailAndReadFlagOrderByCreatedAtDesc(String recipientEmail, boolean readFlag);
}


