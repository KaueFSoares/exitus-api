package br.exitus.api.domain.user.validation;

import br.exitus.api.domain.user.dto.SignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserValidationService {

    private final List<IUserDataValidation> validations;

    @Autowired
    public UserValidationService(List<IUserDataValidation> validations) {
        this.validations = validations;
    }

    public void validate(SignupRequestDTO dto) {
        validations.forEach(validation -> validation.validate(dto));
    }

}
