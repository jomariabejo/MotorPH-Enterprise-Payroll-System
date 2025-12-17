package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.repository.UserRoleRepository;
import com.jomariabejo.motorph.utility.PermissionChecker;

import java.util.Collection;
import java.util.List;

public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public boolean ensureUserHasRole(Integer userId, Integer roleId) {
        boolean created = userRoleRepository.ensureAssignment(userId, roleId);
        if (created) {
            PermissionChecker.clearUserCache(userId);
        }
        return created;
    }

    public List<Role> getRolesForUser(Integer userId) {
        return userRoleRepository.findRolesForUser(userId);
    }

    public void setRolesForUser(Integer userId, Collection<Integer> roleIds) {
        userRoleRepository.setRolesForUser(userId, roleIds);
        PermissionChecker.clearUserCache(userId);
    }
}


