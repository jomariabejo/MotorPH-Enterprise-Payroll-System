package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Notification;
import com.jomariabejo.motorph.repository.NotificationRepository;

import java.util.List;

public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public void updateNotification(Notification notification) {
        notificationRepository.update(notification);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public List<Notification> getNotificationsForEmployee(Employee employee) {
        return notificationRepository.findByEmployee(employee);
    }

    public List<Notification> getUnreadNotificationsForEmployee(Employee employee) {
        return notificationRepository.findUnreadByEmployee(employee);
    }

    public long getUnreadNotificationCount(Employee employee) {
        return notificationRepository.countUnreadByEmployee(employee);
    }
}
