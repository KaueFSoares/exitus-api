package br.exitus.api.service;

import br.exitus.api.constant.message.AuthMessages;
import br.exitus.api.constant.variable.TokenServiceVAR;
import br.exitus.api.domain.role.RoleEnum;
import br.exitus.api.domain.user.User;
import br.exitus.api.domain.user.dto.LoginResponseDTO;
import br.exitus.api.infra.exception.AuthException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Service
public class TokenService {
    @Value(TokenServiceVAR.SECRET)
    private String secret;

    public LoginResponseDTO generateToken(User user) {
        return getLoginResponseDTO(user.getEmail(), getRoles(user));
    }

    public LoginResponseDTO generateToken(Authentication authentication) {
        return getLoginResponseDTO(authentication.getName(), getRoles(authentication));
    }

    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(TokenServiceVAR.ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new AuthException(AuthMessages.TOKEN_VALIDATION_ERROR);
        }

    }

    private LoginResponseDTO getLoginResponseDTO(String name, Set<RoleEnum> roles) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            var accessToken =  JWT.create()
                    .withIssuer(TokenServiceVAR.ISSUER)
                    .withSubject(name)
                    .withExpiresAt(getAccessTokenExpirationDate())
                    .sign(algorithm);

            var refreshToken =  JWT.create()
                    .withIssuer(TokenServiceVAR.ISSUER)
                    .withSubject(name)
                    .withExpiresAt(getRefreshTokenExpirationDate())
                    .sign(algorithm);


            return new LoginResponseDTO(
                    accessToken,
                    TokenServiceVAR.TOKEN_TYPE,
                    getAccessTokenExpirationDate().toEpochMilli(),
                    refreshToken,
                    getRefreshTokenExpirationDate().toEpochMilli(),
                    roles
            );

        } catch (JWTCreationException exception){
            throw new AuthException(AuthMessages.TOKEN_GENERATION_ERROR);
        }
    }

    private Instant getAccessTokenExpirationDate() {
        return LocalDateTime.now().plusHours(TokenServiceVAR.ACCESS_TOKEN_EXPIRATION_TIME).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant getRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusDays(TokenServiceVAR.REFRESH_TOKEN_EXPIRATION_TIME).toInstant(ZoneOffset.of("-03:00"));
    }

    private Set<RoleEnum> getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream().map(authority -> RoleEnum.valueOf(authority.getAuthority())).collect(java.util.stream.Collectors.toSet());
    }

    private Set<RoleEnum> getRoles(User user) {
        return user.getRoles().stream().map(role -> RoleEnum.valueOf(role.getRoleEnum().name())).collect(java.util.stream.Collectors.toSet());
    }
}
