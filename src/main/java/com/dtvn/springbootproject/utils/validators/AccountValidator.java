package com.dtvn.springbootproject.utils.validators;

import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
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
        if (accountRepository.existsByEmail(email)) {
            throw new ErrorException(ERROR_EMAIL_ALREADY_EXISTS,400);
        }
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new ErrorException(ERROR_EMAIL_NOT_VALID, 400);
        }
    }

    private void validatePassword(String password) {
        PasswordValidator.PasswordValidationResult validationResult = PasswordValidator.validatePassword(password);

        if (!validationResult.isValid()) {
            validationResult.getErrors().forEach(error -> {
                throw new ErrorException(error, 400);
            });
        }
    }

    private void validateName(String name, String fieldName) {
        if (name == null || !name.matches(NAME_REGEX)) {
            throw new ErrorException("Invalid " + fieldName, 400);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new ErrorException(ERROR_PHONE_FORMAT, 400);
        }
    }

    private void validateAddress(String address) {
        if (address == null || !address.matches(ADDRESS_REGEX)) {
            throw new ErrorException(ERROR_ADDRESS_INVALID, 400);
        }
    }
}
