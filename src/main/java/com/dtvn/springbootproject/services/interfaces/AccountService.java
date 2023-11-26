package com.dtvn.springbootproject.services.interfaces;

import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    AccountResponseDTO registerAnAccount(AccountRegisterRequestDTO request);
    Page<Account> getAccountByEmailOrName(String emailOrName, Pageable pageable);


}
