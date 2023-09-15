package br.exitus.api.domain.user.dto;

import br.exitus.api.domain.role.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignupRequestDTO(
        @NotEmpty
        String email,
        @NotEmpty
        String password,
        @NotNull
        RoleEnum role
) {
}



