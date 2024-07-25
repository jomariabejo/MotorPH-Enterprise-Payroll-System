package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Message;

public class MessageRepository extends _AbstractHibernateRepository<Message, Integer> {
    public MessageRepository() {
        super(Message.class);
    }
}
