package br.exitus.api.repository;

import br.exitus.api.domain.role.Role;
import br.exitus.api.domain.role.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r FROM Role r WHERE r.roleEnum = :roleEnum")
    Role findByName(RoleEnum roleEnum);
}
