package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.Shift;
import br.exitus.api.domain.user.User;
import br.exitus.api.domain.user.dto.LoginRequestDTO;
import br.exitus.api.domain.user.dto.SignupRequestDTO;
import br.exitus.api.repository.RoleRepository;
import br.exitus.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class AuthControllerTest {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MockMvc mockMvc;
    private final PasswordEncoder passwordEncoder;
    private final JacksonTester<LoginRequestDTO> loginRequestDTO;

    @Autowired
    AuthControllerTest(
            RoleRepository roleRepository,
            UserRepository userRepository,
            MockMvc mockMvc,
            PasswordEncoder passwordEncoder,
            JacksonTester<LoginRequestDTO> loginRequestDTO
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
        this.passwordEncoder = passwordEncoder;
        this.loginRequestDTO = loginRequestDTO;
    }

    private MockHttpServletResponse getResponse(String jsonContent) throws Exception {
        return mockMvc.perform(
                post(RouteVAR.FULL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
        ).andReturn().getResponse();
    }

    private User createInactiveUser() {
        var dto = new SignupRequestDTO(
                "inactive@inactive.com",
                "inactive",
                RoleEnum.ADMIN,
                "inactive",
                "inactive",
                LocalDate.now(),
                Shift.MORNING
        );

        var user = new User(dto);

        user.setActive(false);
        user.setRoles(Set.of(roleRepository.findByName(dto.role())));
        user.setPassword(passwordEncoder.encode(dto.password()));

        return user;
    }

    @Test
    @DisplayName("Must return 200 when login with valid credentials")
    void login1() throws Exception {

        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        "admin@admin.com",
                        "admin"
                )
        ).getJson();

        MockHttpServletResponse response = getResponse(jsonContent);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Must return 401 when login with invalid credentials")
    void login2() throws Exception {

        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        "admin@admin.com",
                        "admin1"
                )
        ).getJson();

        MockHttpServletResponse response = getResponse(jsonContent);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Must return 400 when login with invalid email")
    void login3() throws Exception {

        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        "",
                        "admin"
                )
        ).getJson();

        MockHttpServletResponse response = getResponse(jsonContent);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Must return 400 when login with invalid password")
    void login4() throws Exception {

        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        "admin@admin.com",
                        ""
                )
        ).getJson();

        MockHttpServletResponse response = getResponse(jsonContent);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Must return 404 when login with non-existent email")
    void login5() throws Exception {

        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        "test@test.com",
                        "test"
                )
        ).getJson();

        MockHttpServletResponse response = getResponse(jsonContent);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Must return 403 when login with inactive account")
    void login6() throws Exception {

        userRepository.save(createInactiveUser());

        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        "inactive@inactive.com",
                        "inactive"
                )
        ).getJson();

        MockHttpServletResponse response = getResponse(jsonContent);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}