package br.exitus.api.domain.user.dto;

import br.exitus.api.domain.role.Role;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.Shift;
import br.exitus.api.domain.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record SignupResponseDTO(
        String id,
        String email,
        Boolean active,
        String name,
        String fingerprint,
        Shift shift,
        String enrollment,
        LocalDate birth,
        List<RoleEnum> role
) {
    public SignupResponseDTO(User user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getActive(),
                user.getName(),
                user.getFingerprint(),
                user.getShift(),
                user.getEnrollment(),
                user.getBirthDate(),
                user.getRoles().stream().map(Role::getRoleEnum).collect(Collectors.toList())
        );
    }
}
