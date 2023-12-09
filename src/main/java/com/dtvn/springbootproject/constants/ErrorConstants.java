package com.dtvn.springbootproject.constants;

public class ErrorConstants {
    // Required Errors
    public static final String ERROR_EMAIL_REQUIRED = "Email is required";
    public static final String ERROR_PASSWORD_REQUIRED = "Password is required";
    public static final String ERROR_FIRSTNAME_REQUIRED = "Firstname is required";
    public static final String ERROR_LASTNAME_REQUIRED = "Lastname is required";
    public static final String ERROR_ADDRESS_REQUIRED = "Address is required";
    public static final String ERROR_ROLE_REQUIRED = "Role is required";
    public static final String ERROR_PHONE_REQUIRED = "Phone number is required";

    // Length Errors
    public static final String ERROR_EMAIL_MAX_LENGTH = "Email exceeds maximum allowed length";
    public static final String ERROR_PASSWORD_MAX_LENGTH = "Password exceeds maximum allowed length";
    public static final String ERROR_FIRSTNAME_MAX_LENGTH = "Firstname exceeds maximum allowed length";
    public static final String ERROR_LASTNAME_MAX_LENGTH = "Lastname exceeds maximum allowed length";
    public static final String ERROR_ADDRESS_MAX_LENGTH = "Address exceeds maximum allowed length";
    public static final String ERROR_PHONE_MAX_LENGTH = "Phone number exceeds maximum allowed length";

    // Invalid Errors
    public static final String ERROR_EMAIL_INVALID = "Email is not valid";

    public static final String ERROR_PASSWORD_INVALID = "Password is invalid";
    public static final String ERROR_FIRSTNAME_INVALID = "Firstname is invalid";
    public static final String ERROR_LASTNAME_INVALID = "Lastname is invalid";
    public static final String ERROR_ADDRESS_INVALID = "Address is invalid";

    public static final String ERROR_PHONE_FORMAT_INVALID = "Phone number is not in a valid format";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already exists";

    // Others errors

    public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
    public static final String ERROR_SAVE_ACCOUNT = "Failed to save the account.";
    public static final String ERROR_TOKEN_INVALID = "Token is invalid or expired";
    public static final String ERROR_CANNOT_RETRIEVE_AUTHENTICATED_USER = "Cannot retrieve authenticated user.";
    public static final String USER_NOT_USER_DETAILS = "Authenticated user is not an instance of UserDetails.";
    public static final String ERROR_LOGIN_BAD_CREDENTIALS = "Email or password is incorrect.";
    public static final String ERROR_INTERNAL_SERVER = "Internal server error.";
    public static final String ERROR_ACCOUNT_HAS_BEEN_DELETED = "This account has been deleted.";

    // Unknown Error
    public static final String ERROR_UNKNOWN = "Undetermined error, please contact administrator for support.";
}
