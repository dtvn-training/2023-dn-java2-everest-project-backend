package com.dtvn.springbootproject.services.impl;

import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.entities.Role;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.RoleRepository;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.utils.validators.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountValidator accountValidator;

    @Override
    public AccountResponseDTO registerAnAccount(AccountRegisterRequestDTO request) {

        accountValidator.validateRegisterRequest(request);

        //validate role
        if (request.getRole() == null) {
            throw new ErrorException(ERROR_ROLE_REQUIRED,HTTP_BAD_REQUEST);
        }
        Role role = roleRepository.findByRoleName(request.getRole())
                .orElseThrow(() -> new ErrorException(ERROR_ROLE_NOT_FOUND, HTTP_NOT_FOUND));

        Account createdBy = getAuthenticatedAccount();

        var account = Account.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .address(request.getAddress())
                .phone(request.getPhone())
                .createdBy(createdBy)
                .build();
        try {
            accountRepository.save(account);
        }
        catch (Exception e) {
            throw new ErrorException(ERROR_SAVE_ACCOUNT, HTTP_INTERNAL_SERVER_ERROR);
        }

        return AccountResponseDTO.builder()
                .account_id(account.getAccountId())
                .firstname(account.getFirstname())
                .lastname(account.getLastname())
                .email(account.getEmail())
                .role(account.getRole().getRoleName())
                .address(account.getAddress())
                .phone(account.getPhone())
                .build();
    }

    //get who has just created an account
    private Account getAuthenticatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ErrorException(ERROR_CANNOT_RETRIEVE_AUTHENTICATED_USER,HTTP_INTERNAL_SERVER_ERROR);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (Account) principal;
        }
        throw new ErrorException(USER_NOT_USER_DETAILS,HTTP_FORBIDDEN);
    }

}
