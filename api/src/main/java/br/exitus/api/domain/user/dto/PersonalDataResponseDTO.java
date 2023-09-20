package br.exitus.api.domain.user.dto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public record PersonalDataResponseDTO(
        String name,
        Set<GuardedDetailsForGuardiansDTO> guardeds
) {
}
