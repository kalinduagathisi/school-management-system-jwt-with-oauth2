package com.example.constants;

// This class holds constants related to OAuth2 configuration and application-wide values
public class OAuth2Constant {

    // OAuth2 client IDs and client secret
    public static final String USER_CLIENT_ID = "USER";
    public static final String ADMIN_CLIENT_ID = "ADMIN";
    public static final String CLIENT_SECRET = "";

    // OAuth2 grant types
    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String IMPLICIT = "implicit";

    // OAuth2 scopes
    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String TRUST = "trust";

    // OAuth2 token validity in seconds
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

    // OAuth2 token signing key
    public static final String TOKEN_SIGN_IN_KEY = "w*KLT+Q9$L8Sa@BBLPL";

    // Other constants
    public static final String TOKEN_SIGN_IN_KEY_2 = "..."; // The actual value has been truncated for brevity
    public static final String OPEN_API_KEY = "swlc";
    public static final String SERVICE_ERROR = "Service error";
}
