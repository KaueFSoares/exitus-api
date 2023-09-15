package br.exitus.api.service;

import br.exitus.api.constant.message.AuthMessages;
import br.exitus.api.constant.variable.TokenServiceVAR;
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

@Service
public class TokenService {
    @Value(TokenServiceVAR.SECRET)
    private String secret;

    public String generateToken (Authentication authentication) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(TokenServiceVAR.ISSUER)
                    .withSubject(authentication.getName())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new AuthException(AuthMessages.TOKEN_GENERATION_ERROR);
        }

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

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(TokenServiceVAR.EXPIRATION_TIME).toInstant(ZoneOffset.of("-03:00"));
    }
}
