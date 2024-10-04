package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.MessageAttachment;

public class MessageAttachmentRepository extends _AbstractHibernateRepository<MessageAttachment, Integer> {
    public MessageAttachmentRepository() {
        super(MessageAttachment.class);
    }
}
