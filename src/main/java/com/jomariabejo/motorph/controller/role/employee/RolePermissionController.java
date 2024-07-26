package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import lombok.Getter;
import lombok.Setter;

public class RolePermissionController {

    @Getter
    @Setter
    private SystemAdministratorNavigationController systemAdministratorNavigationController;

    public RolePermissionController() {
    }
}
