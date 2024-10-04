package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Announcement;

public class AnnouncementRepository extends _AbstractHibernateRepository<Announcement, Integer> {
    public AnnouncementRepository() {
        super(Announcement.class);
    }
}
