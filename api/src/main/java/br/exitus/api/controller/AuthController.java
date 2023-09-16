package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.domain.user.User;
import br.exitus.api.domain.user.dto.LoginRequestDTO;
import br.exitus.api.domain.user.dto.LoginResponseDTO;
import br.exitus.api.domain.user.dto.SignupRequestDTO;
import br.exitus.api.domain.user.dto.SignupResponseDTO;
import br.exitus.api.domain.user.validation.UserValidationService;
import br.exitus.api.infra.exception.NotFoundException;
import br.exitus.api.repository.RoleRepository;
import br.exitus.api.repository.UserRepository;
import br.exitus.api.service.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@RequestMapping(RouteVAR.AUTH)
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserValidationService userValidationService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userValidationService = userValidationService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping(RouteVAR.LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {

        if (!userRepository.existsByEmail(dto.email())) throw new NotFoundException("User not found.");

        var token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var authentication = authenticationManager.authenticate(token);

        var JWT= tokenService.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponseDTO(JWT));

    }

    @Transactional
    @PostMapping(RouteVAR.SIGNUP)
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody @Valid SignupRequestDTO dto, UriComponentsBuilder uriComponentsBuilder) {

        userValidationService.validate(dto);

        var user = new User(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));

        var role = roleRepository.findByName(dto.role());
        user.setRoles(Set.of(role));

        userRepository.save(user);

        var uri = uriComponentsBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new SignupResponseDTO(user));

    }

}
