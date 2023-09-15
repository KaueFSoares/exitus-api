package br.exitus.api.domain.user.validation;

import br.exitus.api.domain.user.dto.SignupRequestDTO;

public interface IUserDataValidation {
    void validate(SignupRequestDTO dto);
}
