package com.example.andrewapp.authentication;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationErrors {
    public static final String EMAIL_NOT_FOUND = "EMAIL_NOT_FOUND";
    public static final String EMAIL_EXISTS = "EMAIL_EXISTS";
    public static final String INVALID_ID_TOKEN = "INVALID_ID_TOKEN";//The user's credential is no longer valid. The user must sign in again.
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String USER_DISABLED = "USER_DISABLED";
    public static final String OPERATION_NOT_ALLOWED = "OPERATION_NOT_ALLOWED";
    public static final String INVALID_IDP_RESPONSE = "INVALID_IDP_RESPONSE";
    public static final String EXPIRED_OOB_CODE = "EXPIRED_OOB_CODE";
    public static final String INVALID_OOB_CODE = "INVALID_OOB_CODE";
    public static final String FEDERATED_USER_ID_ALREADY_LINKED = "FEDERATED_USER_ID_ALREADY_LINKED";
    public static final String CREDENTIAL_TOO_OLD_LOGIN_AGAIN = "CREDENTIAL_TOO_OLD_LOGIN_AGAIN";
    public static final String WEAK_PASSWORD = "WEAK_PASSWORD";
    public static final String TOO_MANY_ATTEMPTS_TRY_LATER = "TOO_MANY_ATTEMPTS_TRY_LATER";
    public static Map<String, String> errorHashMap;

    public AuthenticationErrors() {
        setErrorHashMap(new HashMap<>());
    }

    public static void setErrorHashMap(Map<String, String> errorHashMap) {
        errorHashMap.put(EMAIL_NOT_FOUND, "There is no user with this email. The user may have been deleted.");
        errorHashMap.put(EMAIL_EXISTS, "This email address is already in use.");
        errorHashMap.put(USER_DISABLED, "This account has been disabled by the administrator.");
        errorHashMap.put(INVALID_PASSWORD, "The password entered is invalid.");
        errorHashMap.put(INVALID_ID_TOKEN, "The current session has expired. Please sign in again.");
        errorHashMap.put(OPERATION_NOT_ALLOWED, "Operation not allowed. This sign method has been disabled.");
        errorHashMap.put(INVALID_IDP_RESPONSE, "Supplied auth credentials were invalid or expired. Please try again.");
        errorHashMap.put(EXPIRED_OOB_CODE, "The auth credential expired. Please try again.");
        errorHashMap.put(INVALID_OOB_CODE, "The auth credential is invalid. Please try again.");
        errorHashMap.put(FEDERATED_USER_ID_ALREADY_LINKED, "The account is already connected to a different user.");
        errorHashMap.put(CREDENTIAL_TOO_OLD_LOGIN_AGAIN, "The auth credentials have expired. Please login again");
        errorHashMap.put(WEAK_PASSWORD, "The password is weak. It must be 6 characters long or more.");
        errorHashMap.put(TOO_MANY_ATTEMPTS_TRY_LATER, "We have blocked all requests from this device due to unusual activity. Try again later.");

        AuthenticationErrors.errorHashMap = errorHashMap;
    }


}
