package br.exitus.api.controller;

import br.exitus.api.constant.variable.RouteVAR;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.dto.LoginRequestDTO;
import br.exitus.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class UserControllerTest {

    private final MockMvc mockMvc;
    private final JacksonTester<LoginRequestDTO> loginRequestDTO;
    private final UserRepository userRepository;

    @Autowired
    UserControllerTest(
            MockMvc mockMvc,
            JacksonTester<LoginRequestDTO> loginRequestDTO,
            UserRepository userRepository
    ) {
        this.mockMvc = mockMvc;
        this.loginRequestDTO = loginRequestDTO;
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("Must return 200 when it is a valid request")
    void code1() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDED))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Must return the user fingerprint when it is a valid request")
    void code2() throws Exception {

        var user = userRepository.findByEmail(RoleEnum.GUARDED.name().toLowerCase() + "@" + RoleEnum.GUARDED.name().toLowerCase() + ".com").orElseThrow(() -> new Exception("User not found"));

        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDED))
        ).andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        String fingerprint = "";

        try {
             fingerprint = mapper.readTree(response.getContentAsString()).get("fingerprint").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(fingerprint).isEqualTo(user.getFingerprint());
    }

    @Test
    @DisplayName("Must return 401 when it is an invalid request - admin user")
    void code3() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.ADMIN))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Must return 401 when it is an invalid request - guardian user")
    void code4() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDIAN))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Must return 403 when it is an invalid request - without token")
    void code5() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.CODE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Must return 200 when it is a valid request")
    void refreshCode1() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.UPDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDED))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Must return a different fingerprint when it is a valid request")
    void refreshCode2() throws Exception {

        var user = userRepository.findByEmail(RoleEnum.GUARDED.name().toLowerCase() + "@" + RoleEnum.GUARDED.name().toLowerCase() + ".com").orElseThrow(() -> new Exception("User not found"));

        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.UPDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDED))
        ).andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        String fingerprint = "";

        try {
            fingerprint = mapper.readTree(response.getContentAsString()).get("fingerprint").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(fingerprint).isNotEqualTo(user.getFingerprint());
    }

    @Test
    @DisplayName("Must return the new fingerprint when it is a valid request")
    void refreshCode3() throws Exception {


        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.UPDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDED))
        ).andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();

        String fingerprint = "";

        try {
            fingerprint = mapper.readTree(response.getContentAsString()).get("fingerprint").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }


        var user = userRepository.findByEmail(RoleEnum.GUARDED.name().toLowerCase() + "@" + RoleEnum.GUARDED.name().toLowerCase() + ".com").orElseThrow(() -> new Exception("User not found"));

        assertThat(fingerprint).isEqualTo(user.getFingerprint());
    }

    @Test
    @DisplayName("Must return 401 when it is an invalid request - admin user")
    void refreshCode4() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.UPDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.ADMIN))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Must return 401 when it is an invalid request - guardian user")
    void refreshCode5() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.UPDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + getToken(RoleEnum.GUARDIAN))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Must return 403 when it is an invalid request - without token")
    void refreshCode6() throws Exception {
        var response = mockMvc.perform(
                get(RouteVAR.USER+RouteVAR.UPDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private String getToken(RoleEnum role) throws Exception {
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