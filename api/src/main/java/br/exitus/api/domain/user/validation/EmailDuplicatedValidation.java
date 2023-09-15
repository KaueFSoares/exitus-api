package br.exitus.api.domain.user.validation;

import br.exitus.api.domain.user.dto.SignupRequestDTO;
import br.exitus.api.infra.exception.DuplicateException;
import br.exitus.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import br.exitus.api.constant.message.UserValidationMessages;
import org.springframework.stereotype.Component;

@Component
public class EmailDuplicatedValidation implements IUserDataValidation{

    private final UserRepository userRepository;

    @Autowired
    public EmailDuplicatedValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(SignupRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateException(UserValidationMessages.EMAIL_DUPLICATED);
        }
    }
}
