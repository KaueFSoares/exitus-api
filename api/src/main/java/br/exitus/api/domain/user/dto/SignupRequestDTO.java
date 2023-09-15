package br.exitus.api.domain.user.dto;

import br.exitus.api.constant.message.AuthMessages;
import br.exitus.api.domain.role.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignupRequestDTO(
        @NotEmpty(message = AuthMessages.EMPTY_EMAIL)
        @Email(message = AuthMessages.INVALID_EMAIL)
        String email,
        @NotEmpty(message = AuthMessages.EMPTY_PASSWORD)
        String password,
        @NotNull(message = AuthMessages.NULL_ROLE)
        RoleEnum role
) {
}



