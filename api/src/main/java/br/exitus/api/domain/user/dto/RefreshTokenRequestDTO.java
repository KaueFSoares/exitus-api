package br.exitus.api.domain.user.dto;

import br.exitus.api.constant.message.AuthMessages;
import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequestDTO(
        @NotEmpty(message = AuthMessages.REFRESH_TOKEN_REQUIRED)
        String refresh_token
) {
}
