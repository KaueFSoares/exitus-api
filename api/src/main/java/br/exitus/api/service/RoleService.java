package br.exitus.api.service;

import br.exitus.api.domain.role.Role;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByEnum(RoleEnum roleEnum) {
        return roleRepository.findRoleByRoleEnum(roleEnum).orElse(null);
    }

}
