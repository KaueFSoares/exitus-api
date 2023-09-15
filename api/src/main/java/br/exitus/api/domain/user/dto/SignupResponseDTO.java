package br.exitus.api.domain.user.dto;

import br.exitus.api.domain.role.Role;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public record SignupResponseDTO(
        String id,
        String email,
        List<RoleEnum> role
) {
    public SignupResponseDTO(User user) {
        this(user.getId(), user.getEmail(), user.getRoles().stream().map(Role::getRoleEnum).collect(Collectors.toList()));
    }
}
