package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Conversation;

public class ConversationRepository extends _AbstractHibernateRepository<Conversation, Integer> {
    public ConversationRepository() {
        super(Conversation.class);
    }
}
