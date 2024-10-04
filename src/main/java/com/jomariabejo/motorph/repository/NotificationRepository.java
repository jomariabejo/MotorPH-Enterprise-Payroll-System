package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Notification;

public class NotificationRepository extends _AbstractHibernateRepository<Notification, Integer> {
    public NotificationRepository() {
        super(Notification.class);
    }
}
