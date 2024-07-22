package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.repository.RoleRepository;

import java.util.List;

public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public void updateRole(Role role) {
        roleRepository.update(role);
    }

    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }
}
