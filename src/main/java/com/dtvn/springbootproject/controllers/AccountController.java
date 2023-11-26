package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.services.implementations.AccountServiceImpl;
import com.dtvn.springbootproject.utils.AppContants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;

//    @GetMapping
//    public List<AccountResponse> getAccounts(@RequestParam(defaultValue = "0") int page,
//                                             @RequestParam(defaultValue = "3") int size) {
//        return accountService.getAccounts(page, size);
//    }
    @PostMapping
    public ResponseEntity<AccountResponseDTO> registerAnAccount(
            @RequestBody AccountRegisterRequestDTO request
    ) {
        return ResponseEntity.ok(accountServiceImpl.registerAnAccount(request));
    }
    @GetMapping
    public Page<Account> getAccounts(@RequestParam(value = "emailOrName", required = false) String emailOrName,
                                     @RequestParam(value = "pageNo", defaultValue = AppContants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppContants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return accountServiceImpl.getAccountByEmailOrName(emailOrName, pageable);
    }
}
