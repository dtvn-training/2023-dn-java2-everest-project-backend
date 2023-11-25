package com.dtvn.springbootproject.services.implementations;

import com.dtvn.springbootproject.entities.Role;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.RoleRepository;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.services.interfaces.AccountService;
import com.dtvn.springbootproject.utils.validators.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;

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
        Role role = roleRepository.findByRoleName(request.getRole())
                .orElseThrow(() -> new ErrorException(ERROR_ROLE_NOT_FOUND, 404));

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
            throw new IllegalStateException("Failed to save the account.", e);
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
            throw new IllegalStateException("Cannot retrieve authenticated user.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (Account) principal;
        }

        throw new IllegalStateException("Authenticated user is not an instance of UserDetails.");
    }

}
