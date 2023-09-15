package br.exitus.api.domain.role;

import br.exitus.api.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    @PostConstruct
    public void seed() {
        if (roleRepository.count() == 0) {
            for (RoleEnum roleEnum : RoleEnum.values()){
                var role = new Role();
                role.setRoleEnum(roleEnum);
                roleRepository.save(role);
            }
            System.out.println("------ RoleSeeder: created roles ------");
        } else {
            System.out.println("------ RoleSeeder: roles already created ------");
        }
    }

}
