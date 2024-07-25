package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.RolePermission;
import com.jomariabejo.motorph.repository.RolePermissionRepository;

import java.util.List;

public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RolePermission getRolePermissionById(Integer id) {
        return rolePermissionRepository.findById(id);
    }

    public List<RolePermission> getAllRolePermissions() {
        return rolePermissionRepository.findAll();
    }

    public void saveRolePermission(RolePermission rolePermission) {
        rolePermissionRepository.save(rolePermission);
    }

    public void updateRolePermission(RolePermission rolePermission) {
        rolePermissionRepository.update(rolePermission);
    }

    public void deleteRolePermission(RolePermission rolePermission) {
        rolePermissionRepository.delete(rolePermission);
    }
}
