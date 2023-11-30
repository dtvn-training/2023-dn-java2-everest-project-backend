package com.dtvn.springbootproject.utils.validators;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.dtvn.springbootproject.constants.FieldValueLengthConstants.*;

public class PasswordValidator {

    public static PasswordValidationResult validatePassword(String password) {
        PasswordValidationResult result = new PasswordValidationResult();
        if (password.isEmpty()) {
            result.addError("Password is required");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            result.addError("Password should be at least 8 characters long");
        }

        if (password.length() >= MAX_PASSWORD_LENGTH) {
            result.addError("Password must only be a maximum of 60 characters long");
        }

        if (!password.matches(".*\\d.*")) {
            result.addError("Password should contain at least one digit");
        }

        if (!password.matches(".*[a-z].*")) {
            result.addError("Password should contain at least one lowercase letter");
        }

        if (!password.matches(".*[A-Z].*")) {
            result.addError("Password should contain at least one uppercase letter");
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            result.addError("Password should contain at least one special character (@#$%^&+=!)");
        }

        if (password.matches(".*\\s+.*")) {
            result.addError("Password should not contain whitespace characters");
        }

        if (password.startsWith(" ") || password.endsWith(" ")) {
            result.addError("Password should not start or end with whitespace characters");
        }
        return result;
    }

    @Getter
    public static class PasswordValidationResult {
        private final List<String> errors;

        public PasswordValidationResult() {
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }
    }
}
