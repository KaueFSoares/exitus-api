package br.exitus.api.domain.user.dto;

import br.exitus.api.constant.message.AuthMessages;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotEmpty(message = AuthMessages.EMPTY_EMAIL)
        String email,
        @NotEmpty(message = AuthMessages.EMPTY_PASSWORD)
        String password
) {
}
