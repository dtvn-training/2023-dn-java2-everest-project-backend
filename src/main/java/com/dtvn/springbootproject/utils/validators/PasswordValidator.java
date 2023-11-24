package com.dtvn.springbootproject.utils.validators;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    public static PasswordValidationResult validatePassword(String password) {
        PasswordValidationResult result = new PasswordValidationResult();

        if (password.length() < 8) {
            result.addError("Password should be at least 8 characters long.");
        }

        if (!password.matches(".*\\d.*")) {
            result.addError("Password should contain at least one digit.");
        }

        if (!password.matches(".*[a-z].*")) {
            result.addError("Password should contain at least one lowercase letter.");
        }

        if (!password.matches(".*[A-Z].*")) {
            result.addError("Password should contain at least one uppercase letter.");
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            result.addError("Password should contain at least one special character (@#$%^&+=!).");
        }

        if (password.matches("\\s+")) {
            result.addError("Password should not contain whitespace characters.");
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
