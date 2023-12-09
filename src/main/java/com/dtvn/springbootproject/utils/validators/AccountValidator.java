package com.dtvn.springbootproject.utils.validators;

import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.FieldValueLengthConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;
import static com.dtvn.springbootproject.utils.RegularExpression.*;

@Component
@RequiredArgsConstructor
public class AccountValidator {
    private final AccountRepository accountRepository;

    public void validateRegisterRequest(AccountRegisterRequestDTO request) {
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validateName(request.getFirstname(), "Firstname");
        validateName(request.getLastname(), "Lastname");
        validatePhoneNumber(request.getPhone());
        validateAddress(request.getAddress());
    }

    private void validateEmail(String email) {
        if (email.isEmpty()){
            throw new ErrorException(ERROR_EMAIL_REQUIRED, HTTP_BAD_REQUEST);
        }
        if (email.length() >= MAX_EMAIL_LENGTH){
            throw new ErrorException(ERROR_EMAIL_MAX_LENGTH, HTTP_BAD_REQUEST);
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new ErrorException(ERROR_EMAIL_INVALID, HTTP_BAD_REQUEST);
        }
    }

        private void validatePassword(String password) {
            if (password.isEmpty()){
                throw new ErrorException(ERROR_PASSWORD_REQUIRED, HTTP_BAD_REQUEST);
            }
            PasswordValidator.PasswordValidationResult validationResult = PasswordValidator.validatePassword(password);

    //        if (!validationResult.isValid()) {
    //            validationResult.getErrors().forEach(error -> {
    //                throw new ErrorException(error, HTTP_BAD_REQUEST);
    //            });
    //        }
            if (!validationResult.isValid()) {
                List<String> errors = validationResult.getErrors();
                String formattedErrors = String.join(", ", errors);
                throw new ErrorException(ERROR_PASSWORD_INVALID, HTTP_BAD_REQUEST,formattedErrors);
            }
        }

    private void validateName(String name, String fieldName) {
        if (name.isEmpty() && fieldName.contains("Firstname")){
            throw new ErrorException(ERROR_FIRSTNAME_REQUIRED, HTTP_BAD_REQUEST);
        }
        if (name.isEmpty() && fieldName.contains("Lastname")) {
            throw new ErrorException(ERROR_LASTNAME_REQUIRED, HTTP_BAD_REQUEST);
        }
        if (name.length() > MAX_FIRSTNAME_LENGTH && fieldName.contains("Firstname")){
            throw new ErrorException(ERROR_FIRSTNAME_MAX_LENGTH, HTTP_BAD_REQUEST);
        }
        if (name.length() > MAX_LASTNAME_LENGTH && fieldName.contains("Lastname")){
            throw new ErrorException(ERROR_LASTNAME_MAX_LENGTH, HTTP_BAD_REQUEST);
        }

        if (fieldName.contains("Firstname") && !name.matches(NAME_REGEX)) {
            throw new ErrorException(ERROR_FIRSTNAME_INVALID, HTTP_BAD_REQUEST);
        }
        if (fieldName.contains("Lastname") && !name.matches(NAME_REGEX)) {
            throw new ErrorException(ERROR_LASTNAME_INVALID, HTTP_BAD_REQUEST);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()){
            throw new ErrorException(ERROR_PHONE_REQUIRED, HTTP_BAD_REQUEST);
        }
        if (phoneNumber.length() > MAX_PHONE_LENGTH) {
            throw new ErrorException(ERROR_PHONE_MAX_LENGTH, HTTP_BAD_REQUEST);
        }
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new ErrorException(ERROR_PHONE_FORMAT_INVALID, HTTP_BAD_REQUEST);
        }
    }

    private void validateAddress(String address) {
        if (address.isEmpty()) {
            throw new ErrorException(ERROR_ADDRESS_REQUIRED, HTTP_BAD_REQUEST);
        }
        if (address.length() > MAX_ADDRESS_LENGTH) {
            throw new ErrorException(ERROR_ADDRESS_MAX_LENGTH, HTTP_BAD_REQUEST);
        }
        if (!address.matches(ADDRESS_REGEX)) {
            throw new ErrorException(ERROR_ADDRESS_INVALID, HTTP_BAD_REQUEST);
        }
    }
}
