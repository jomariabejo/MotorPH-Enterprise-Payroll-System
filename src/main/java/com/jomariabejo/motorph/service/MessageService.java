package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Message;
import com.jomariabejo.motorph.repository.MessageRepository;

import java.util.List;

public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public void updateMessage(Message message) {
        messageRepository.update(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }
}
