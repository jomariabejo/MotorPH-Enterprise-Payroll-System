package com.jomariabejo.motorph.service.RoleService;

import com.jomariabejo.motorph.entity.Role;
import com.jomariabejo.motorph.repository.RoleRepository.RoleRepository;

public class RoleService {
    private RoleRepository roleRepository;

    public RoleService() {
        this.roleRepository = new RoleRepository();
    }

    public int fetchRoleId(Role role) {
        return roleRepository.fetchRoleId(role);
    }
}
