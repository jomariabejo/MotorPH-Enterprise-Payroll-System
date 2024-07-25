package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.MessageStatus;
import com.jomariabejo.motorph.repository.MessageStatusRepository;

import java.util.List;

public class MessageStatusService {

    private final MessageStatusRepository statusRepository;

    public MessageStatusService(MessageStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public MessageStatus getStatusById(Integer id) {
        return statusRepository.findById(id);
    }

    public List<MessageStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    public void saveStatus(MessageStatus status) {
        statusRepository.save(status);
    }

    public void updateStatus(MessageStatus status) {
        statusRepository.update(status);
    }

    public void deleteStatus(MessageStatus status) {
        statusRepository.delete(status);
    }
}
