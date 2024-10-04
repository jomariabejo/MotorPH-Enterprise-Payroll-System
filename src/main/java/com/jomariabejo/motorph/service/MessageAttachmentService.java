package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.MessageAttachment;
import com.jomariabejo.motorph.repository.MessageAttachmentRepository;

import java.util.List;

public class MessageAttachmentService {

    private final MessageAttachmentRepository attachmentRepository;

    public MessageAttachmentService(MessageAttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public MessageAttachment getAttachmentById(Integer id) {
        return attachmentRepository.findById(id);
    }

    public List<MessageAttachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    public void saveAttachment(MessageAttachment attachment) {
        attachmentRepository.save(attachment);
    }

    public void updateAttachment(MessageAttachment attachment) {
        attachmentRepository.update(attachment);
    }

    public void deleteAttachment(MessageAttachment attachment) {
        attachmentRepository.delete(attachment);
    }
}
