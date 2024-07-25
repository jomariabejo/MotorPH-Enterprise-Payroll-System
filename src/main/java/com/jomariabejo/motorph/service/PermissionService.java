package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.repository.PermissionRepository;

import java.util.List;

public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission getPermissionById(Integer id) {
        return permissionRepository.findById(id);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }

    public void updatePermission(Permission permission) {
        permissionRepository.update(permission);
    }

    public void deletePermission(Permission permission) {
        permissionRepository.delete(permission);
    }
}
