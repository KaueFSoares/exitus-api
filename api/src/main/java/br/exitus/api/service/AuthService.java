package br.exitus.api.service;

import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final RoleService roleService;

    @Autowired
    public AuthService(RoleService roleService) {
        this.roleService = roleService;
    }

    public Boolean isGuarded(User user) {
        var roles = user.getRoles();

        var guardedRole = roleService.getRoleByEnum(RoleEnum.GUARDED);

        return roles.contains(guardedRole);
    }

    public Boolean isEmployee(User user) {
        var roles = user.getRoles();

        var employeeRole = roleService.getRoleByEnum(RoleEnum.EMPLOYEE);

        return roles.contains(employeeRole);
    }

    public Boolean isAdmin(User user) {
        var roles = user.getRoles();

        var adminRole = roleService.getRoleByEnum(RoleEnum.ADMIN);

        return roles.contains(adminRole);
    }

    public Boolean isGuardian(User user) {
        var roles = user.getRoles();

        var guardianRole = roleService.getRoleByEnum(RoleEnum.GUARDIAN);

        return roles.contains(guardianRole);
    }

}
