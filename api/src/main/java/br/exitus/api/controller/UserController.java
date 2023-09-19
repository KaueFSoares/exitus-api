package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.domain.user.User;
import br.exitus.api.domain.user.dto.CodeResponseDTO;
import br.exitus.api.infra.exception.AuthException;
import br.exitus.api.repository.UserRepository;
import br.exitus.api.service.AuthService;
import br.exitus.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(RouteVAR.USER)
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(
            AuthService authService,
            UserService userService,
            UserRepository userRepository
    ) {
        this.authService = authService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping(RouteVAR.CODE)
    public ResponseEntity<CodeResponseDTO> code() {
        System.out.println("chegoua aqui");
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authService.isGuarded(user) || authService.isEmployee(user)) {
            return ResponseEntity.ok(new CodeResponseDTO(user.getFingerprint()));
        }

        throw new AuthException("Only guarded or employee users can get a code");
    }

    @GetMapping(RouteVAR.UPDATE_CODE)
    public ResponseEntity<CodeResponseDTO> refreshCode() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authService.isGuarded(user) || authService.isEmployee(user)) {
            user.setFingerprint(UUID.randomUUID().toString());

            userRepository.save(user);

            return ResponseEntity.ok(new CodeResponseDTO(user.getFingerprint()));
        }

        throw new AuthException("Only guarded or employee users can refresh a code");
    }

}
