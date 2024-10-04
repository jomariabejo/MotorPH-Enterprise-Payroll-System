package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Announcement;
import com.jomariabejo.motorph.repository.AnnouncementRepository;

import java.util.List;

public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Announcement getAnnouncementById(Integer id) {
        return announcementRepository.findById(id);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public void saveAnnouncement(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    public void updateAnnouncement(Announcement announcement) {
        announcementRepository.update(announcement);
    }

    public void deleteAnnouncement(Announcement announcement) {
        announcementRepository.delete(announcement);
    }
}
