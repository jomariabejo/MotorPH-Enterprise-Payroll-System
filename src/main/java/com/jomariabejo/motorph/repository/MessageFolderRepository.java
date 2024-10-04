package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.MessageFolder;

public class MessageFolderRepository extends _AbstractHibernateRepository<MessageFolder, Integer> {
    public MessageFolderRepository() {
        super(MessageFolder.class);
    }
}
