package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.Shift;
import br.exitus.api.domain.user.User;
import br.exitus.api.domain.user.UserSeeder;
import br.exitus.api.domain.user.dto.LoginRequestDTO;
import br.exitus.api.domain.user.dto.RefreshTokenRequestDTO;
import br.exitus.api.domain.user.dto.SignupRequestDTO;
import br.exitus.api.repository.RoleRepository;
import br.exitus.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
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

import java.io.IOException;
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
    private final JacksonTester<RefreshTokenRequestDTO> refreshTokenRequestDTO;
    private final JacksonTester<SignupRequestDTO> signupRequestDTO;
    private final UserSeeder userSeeder;


    @Autowired
    AuthControllerTest(
            RoleRepository roleRepository,
            UserRepository userRepository,
            MockMvc mockMvc,
            PasswordEncoder passwordEncoder,
            JacksonTester<LoginRequestDTO> loginRequestDTO,
            JacksonTester<RefreshTokenRequestDTO> refreshTokenRequestDTO,
            JacksonTester<SignupRequestDTO> signupRequestDTO,
            UserSeeder userSeeder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
        this.passwordEncoder = passwordEncoder;
        this.loginRequestDTO = loginRequestDTO;
        this.refreshTokenRequestDTO = refreshTokenRequestDTO;
        this.userSeeder = userSeeder;
        this.signupRequestDTO = signupRequestDTO;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userSeeder.seed();
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

    private User createTestUser() {
        var dto = new SignupRequestDTO(
                "test@test.com",
                "test",
                RoleEnum.GUARDED,
                "test",
                "test",
                LocalDate.now(),
                Shift.MORNING
        );

        var user = new User(dto);

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

    @Test
    @DisplayName("Must return 200 when refresh token with valid refresh token")
    void refreshToken1() throws Exception {

        var jsonContent = refreshTokenRequestDTO.write(
                new RefreshTokenRequestDTO(
                        "Bearer " + getRefreshToken()
                )
        ).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_REFRESH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Must return 400 when refresh token with invalid refresh token")
    void refreshToken2() throws Exception {

        var jsonContent = refreshTokenRequestDTO.write(
                new RefreshTokenRequestDTO(
                        "Bearer " + getRefreshToken() + "trash"
                )
        ).getJson();

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_REFRESH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Must return 201 when signup with valid credentials")
    public void signup1() throws Exception {

        var jsonContent = getJsonContent("test@test.com", "test", "test");

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAccessToken(RoleEnum.ADMIN))
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Must return 400 when signup with invalid email")
    public void signup2() throws Exception {

        var jsonContent = getJsonContent("test", "test", "test");

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAccessToken(RoleEnum.ADMIN))
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Must return 400 when signup with invalid password")
    public void signup3() throws Exception {

        var jsonContent = getJsonContent("test@test.com", "", "test");

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAccessToken(RoleEnum.ADMIN))
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Must return 400 when signup with invalid role")
    public void signup4() throws Exception {

        var jsonContent = getJsonContent("test@test.com", "test", "test").replace("GUARDED", "GUARDED1");

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAccessToken(RoleEnum.ADMIN))
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Must return 400 when signup with invalid name")
    public void signup5() throws Exception {

        var jsonContent = getJsonContent("test@test.com", "test", "");

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAccessToken(RoleEnum.ADMIN))
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Must return 400 when signup with invalid shift")
    public void signup6() throws Exception {

        var jsonContent = getJsonContent("test@test.com", "test", "test").replace("MORNING", "MORNING1");

        MockHttpServletResponse response = mockMvc.perform(
                post(RouteVAR.FULL_SIGNUP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getAccessToken(RoleEnum.ADMIN))
                        .content(jsonContent)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private String getJsonContent(String mail, String password, String name) throws IOException {
        return signupRequestDTO.write(
                new SignupRequestDTO(
                        mail,
                        password,
                        RoleEnum.GUARDED,
                        name,
                        "test",
                        LocalDate.now(),
                        Shift.MORNING
                )
        ).getJson();
    }
    private String getRefreshToken() throws Exception {
        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        RoleEnum.ADMIN.name().toLowerCase() + "@" + RoleEnum.ADMIN.name().toLowerCase() + ".com",
                        RoleEnum.ADMIN.name().toLowerCase()
                )
        ).getJson();

        var response = mockMvc.perform(
                post(RouteVAR.FULL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
        ).andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readTree(response.getContentAsString()).get("refresh_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
    private String getAccessToken(RoleEnum role) throws Exception {
        var jsonContent = loginRequestDTO.write(
                new LoginRequestDTO(
                        role.name().toLowerCase() + "@" + role.name().toLowerCase() + ".com",
                        role.name().toLowerCase()
                )
        ).getJson();

        var response = mockMvc.perform(
                post(RouteVAR.FULL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
        ).andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readTree(response.getContentAsString()).get("access_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}