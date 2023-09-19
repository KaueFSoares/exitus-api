package br.exitus.api.constant.variable;

public class TokenServiceVAR {

    public static final String SECRET = "${api.security.token.secret}";
    public static final String ISSUER = "ExitusAPI";
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 2; // hours
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 30; // days
    public static final String TOKEN_TYPE = "Bearer";
}
