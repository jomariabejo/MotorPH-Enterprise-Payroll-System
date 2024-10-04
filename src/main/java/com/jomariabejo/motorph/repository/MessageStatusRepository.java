package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.MessageStatus;

public class MessageStatusRepository extends _AbstractHibernateRepository<MessageStatus, Integer> {
    public MessageStatusRepository() {
        super(MessageStatus.class);
    }
}
