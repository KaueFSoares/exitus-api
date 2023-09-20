package br.exitus.api.constant.variable;

import java.net.URI;

public class RouteVAR {

    public static final String FRONTEND = "http://localhost:5173";

    private static final String API = "/api";

    // AuthController
    public static final String AUTH = API + "/auth";
    public static final String LOGIN = "/login";
    public static final String SIGNUP = "/signup";
    public static final String REFRESH = "/refresh-token";

    public static final String FULL_REFRESH = AUTH + REFRESH;
    public static final String FULL_LOGIN = AUTH + LOGIN;
    public static final String FULL_SIGNUP = AUTH + SIGNUP;

    // UserController
    public static final String USER = API + "/user";
    public static final String CODE = "/code";
    public static final String UPDATE_CODE = "/update-code";
    public static final String PERSONAL_DATA = "personal-data";
}
