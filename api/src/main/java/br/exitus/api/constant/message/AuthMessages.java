package br.exitus.api.constant.message;

public class AuthMessages {

    //Config related error messages
    public static final String AUTHENTICATION_MANAGER_ERROR = "Error on getting authentication manager.";


    //Validation related error messages
    public static final String EMPTY_EMAIL = "Email cannot be empty.";
    public static final String EMPTY_PASSWORD = "Password cannot be empty.";
    public static final String INVALID_EMAIL = "Invalid email.";
    public static final String NULL_ROLE = "Role cannot be null.";


    //Auth related error messages
    public static final String TOKEN_GENERATION_ERROR = "Error on generating token.";
    public static final String TOKEN_VALIDATION_ERROR = "Error on validating token.";
}
