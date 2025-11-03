package com.rwtool.controller;

import com.rwtool.model.Notification;
import com.rwtool.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Notification>> getAdminNotifications(@RequestParam(name = "unreadOnly", defaultValue = "false") boolean unreadOnly) {
        return ResponseEntity.ok(notificationService.getAdminNotifications(unreadOnly));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String email,
                                                                   @RequestParam(name = "unreadOnly", defaultValue = "false") boolean unreadOnly) {
        return ResponseEntity.ok(notificationService.getUserNotifications(email, unreadOnly));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable String id) {
        notificationService.markRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all/admin")
    public ResponseEntity<Map<String, Object>> markAllAdmin() {
        int count = notificationService.markAllReadForAdmin();
        Map<String, Object> res = new HashMap<>();
        res.put("updated", count);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/read-all/user/{email}")
    public ResponseEntity<Map<String, Object>> markAllUser(@PathVariable String email) {
        int count = notificationService.markAllReadForUser(email);
        Map<String, Object> res = new HashMap<>();
        res.put("updated", count);
        return ResponseEntity.ok(res);
    }
}


