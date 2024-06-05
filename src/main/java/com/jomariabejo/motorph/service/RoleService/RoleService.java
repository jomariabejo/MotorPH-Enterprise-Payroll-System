package com.jomariabejo.motorph.service.RoleService;

import com.jomariabejo.motorph.entity.Role;
import com.jomariabejo.motorph.repository.RoleRepository.RoleRepository;
import javafx.scene.control.SingleSelectionModel;

public class RoleService {
    private RoleRepository roleRepository;

    public RoleService() {
        this.roleRepository = new RoleRepository();
    }

    public int fetchRoleId(String string) {
        return roleRepository.fetchRoleId(string);
    }

    public String fetchRoleName(int roleID) {
        return roleRepository.fetchRoleName(roleID);
    }
}
