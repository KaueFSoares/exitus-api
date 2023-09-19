package br.exitus.api.service;

import br.exitus.api.domain.user.User;
import br.exitus.api.domain.user.dto.CodeResponseDTO;
import br.exitus.api.infra.exception.AuthException;
import br.exitus.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public UserService(
            AuthService authService,
            UserRepository userRepository
    ) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    public CodeResponseDTO code() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authService.isGuarded(user) || authService.isEmployee(user)) {
            return new CodeResponseDTO(user.getFingerprint());
        }

        throw new AuthException("Only guarded or employee users can get a code");
    }

    public CodeResponseDTO refreshCode() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authService.isGuarded(user) || authService.isEmployee(user)) {
            user.setFingerprint(UUID.randomUUID().toString());

            userRepository.save(user);

            return new CodeResponseDTO(user.getFingerprint());
        }

        throw new AuthException("Only guarded or employee users can refresh a code");
    }

}
