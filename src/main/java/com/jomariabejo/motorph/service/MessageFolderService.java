package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.MessageFolder;
import com.jomariabejo.motorph.repository.MessageFolderRepository;

import java.util.List;

public class MessageFolderService {

    private final MessageFolderRepository folderRepository;

    public MessageFolderService(MessageFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public MessageFolder getFolderById(Integer id) {
        return folderRepository.findById(id);
    }

    public List<MessageFolder> getAllFolders() {
        return folderRepository.findAll();
    }

    public void saveFolder(MessageFolder folder) {
        folderRepository.save(folder);
    }

    public void updateFolder(MessageFolder folder) {
        folderRepository.update(folder);
    }

    public void deleteFolder(MessageFolder folder) {
        folderRepository.delete(folder);
    }
}
