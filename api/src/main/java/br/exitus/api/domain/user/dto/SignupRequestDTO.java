package br.exitus.api.domain.user.dto;

import br.exitus.api.constant.message.AuthMessages;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.Shift;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SignupRequestDTO(
        @NotEmpty(message = AuthMessages.EMPTY_EMAIL)
        @Email(message = AuthMessages.INVALID_EMAIL)
        String email,

        @NotEmpty(message = AuthMessages.EMPTY_PASSWORD)
        String password,

        @NotNull(message = AuthMessages.NULL_ROLE)
        RoleEnum role,

        @NotEmpty(message = AuthMessages.EMPTY_NAME)
        String name,

        String enrollment,

        LocalDate birth,

        Shift shift
) {
}



