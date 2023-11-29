package com.dtvn.springbootproject.services.implementations;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountDTO;
import com.dtvn.springbootproject.entities.Role;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.RoleRepository;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.constants.AppContants;
import com.dtvn.springbootproject.utils.validators.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final ModelMapper mapper = new ModelMapper();
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
                .accountId(account.getAccountId())
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
    public Page<AccountDTO> getAccountByEmailOrName(String emailOrName, Pageable pageable) {
//       if(emailOrName == null || emailOrName.isEmpty()){
//           Page<Account> allAccount = accountRepository.getAllAccount(pageable);
//           return allAccount.map(account -> mapper.map(account, AccountDTO.class));
//       } else if(emailOrName.matches(EMAIL_REGEX)){
//               return accountRepository.findByEmail(emailOrName, pageable);
//       } else{
//            return accountRepository.findByName(emailOrName, pageable);
//       }

        if(emailOrName == null || emailOrName.isEmpty()){
            Page<Account> allAccount = accountRepository.getAllAccount(pageable);
            return allAccount.map(account -> mapper.map(account, AccountDTO.class));
        } else {
            Page<Account> listAccount = accountRepository.findAccountByEmailOrName(emailOrName,pageable);
            return listAccount.map(account -> mapper.map(account,AccountDTO.class));
        }
    }

    @Override
    @Transactional
    public void deleteAccount(Integer id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(AppContants.ACCOUNT_NOT_FOUND));
        existingAccount.setDeleteFlag(true);
        accountRepository.save(existingAccount);
    }
    @Override
    public AccountDTO updatedAccount(Integer id, AccountDTO updatedAccount) {
        Optional<Account> optionalOldAccount = accountRepository.findById(id);
        if(optionalOldAccount.isPresent()){
            Account oldAccount  =  optionalOldAccount.get();
            oldAccount.setEmail(updatedAccount.getEmail());
            oldAccount.setFirstname(updatedAccount.getFirstname());
            oldAccount.setLastname(updatedAccount.getLastname());
            oldAccount.setAddress(updatedAccount.getAddress());
            oldAccount.setPhone(updatedAccount.getPhone());
            //find role id by role name
            Optional<Role> roleUpdate = roleRepository.findByRoleName(updatedAccount.getRole());
            if(roleUpdate.isPresent()){
                oldAccount.setRole(roleUpdate.get());
            }else{
                throw new ErrorException(ERROR_ROLE_NOT_FOUND, AppContants.RESOURCE_NOT_FOUND_CODE);
            }
            return mapper.map(accountRepository.save(oldAccount),AccountDTO.class);
        }else {
            throw new ErrorException(AppContants.ACCOUNT_NOT_FOUND,  AppContants.RESOURCE_NOT_FOUND_CODE);
        }
    }

    @Override
    public boolean isInteger(String number) {
        return false;
    }
}
