package br.exitus.api.domain.user.dto;

import br.exitus.api.domain.role.RoleEnum;

import java.util.Set;

public record LoginResponseDTO(
        String access_token,
        String token_type,
        Long access_token_expires_at,
        String refresh_token,
        Long refresh_token_expires_at,
        Set<RoleEnum> roles
) {
}
