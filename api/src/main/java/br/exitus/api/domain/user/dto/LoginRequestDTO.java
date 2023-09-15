package br.exitus.api.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotEmpty
        String email,
        @NotEmpty
        String password
) {
}
