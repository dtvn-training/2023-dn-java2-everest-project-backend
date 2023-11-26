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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.utils.RegularExpression.EMAIL_REGEX;

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
    @Override
    public Page<Account> getAccountByEmailOrName(String emailOrName,  Pageable pageable) {
       if(emailOrName == null || emailOrName.isEmpty()){
           return accountRepository.findAll(pageable);
       } else if(emailOrName.matches(EMAIL_REGEX)){
               return accountRepository.findByEmail(emailOrName, pageable);
       } else{
            return accountRepository.findByName(emailOrName, pageable);

       }
    }

    @Override
    @Transactional
    public void deleteAccount(Integer id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setDeleteFlag(true);
        accountRepository.save(existingAccount);
    }

    @Override
    public Account updatedAccount(Integer id, Account updatedAccount) {
        Optional<Account> optionalOldAccount = accountRepository.findById(id);
        if(optionalOldAccount.isPresent()){
            Account oldAccount  =  optionalOldAccount.get();
            BeanUtils.copyProperties(updatedAccount, oldAccount, "account_id");
            return accountRepository.save(oldAccount);
        }else {
            throw new ErrorException("Account not found with id: "  + id,  404);
        }
    }

}
