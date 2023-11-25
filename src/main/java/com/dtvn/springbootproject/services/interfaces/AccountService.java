package com.dtvn.springbootproject.services.interfaces;

import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;

public interface AccountService {
    AccountResponseDTO registerAnAccount(AccountRegisterRequestDTO request);
}
