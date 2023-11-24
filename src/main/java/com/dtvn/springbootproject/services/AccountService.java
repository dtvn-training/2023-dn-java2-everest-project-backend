package com.dtvn.springbootproject.services;

import com.dtvn.springbootproject.requests.RegisterAnAccountRequest;
import com.dtvn.springbootproject.responses.AccountResponse;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.enums.Role;
import com.dtvn.springbootproject.exceptions.EmailAlreadyExistsException;
import com.dtvn.springbootproject.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

//    public List<AccountResponse> getAccounts(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<AccountResponse> accountPage = accountRepository.findAllResponse(pageable);
//
//        // Chuyển đổi từ AccountResponse sang List<AccountResponse>
//        List<AccountResponse> accountResponses = accountPage.getContent();
//
//        return accountResponses;
//    }
    public AccountResponse registerAnAccount(RegisterAnAccountRequest request) {

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists",400);
        }
        var account = Account.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();
        accountRepository.save(account);
        return AccountResponse.builder()
                .account_id(account.getAccountId())
                .firstname(account.getFirstname())
                .lastname(account.getLastname())
                .email(account.getEmail())
                .role(account.getRole())
                .address(account.getAddress())
                .phone(account.getPhone())
                .build();
    }
}
