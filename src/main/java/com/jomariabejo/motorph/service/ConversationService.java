package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Conversation;
import com.jomariabejo.motorph.repository.ConversationRepository;

import java.util.List;

public class ConversationService {

    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation getConversationById(Integer id) {
        return conversationRepository.findById(id);
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public void saveConversation(Conversation conversation) {
        conversationRepository.save(conversation);
    }

    public void updateConversation(Conversation conversation) {
        conversationRepository.update(conversation);
    }

    public void deleteConversation(Conversation conversation) {
        conversationRepository.delete(conversation);
    }
}
