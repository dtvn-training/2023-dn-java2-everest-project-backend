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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> registerAnAccount(
            @RequestBody AccountRegisterRequestDTO request
    ) {
        return ResponseEntity.ok(accountServiceImpl.registerAnAccount(request));
    }
    @GetMapping
    public Page<AccountResponseDTO> getAccounts(@RequestParam(value = "emailOrName", required = false) String emailOrName,
                                     @RequestParam(value = "pageNo", defaultValue = AppContants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppContants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return accountServiceImpl.getAccountByEmailOrName(emailOrName, pageable);
    }
    @PatchMapping()
    public ResponseEntity<String> deleteAccount(@RequestParam(value = "id", required = true) Integer id) {
        try {
            accountServiceImpl.deleteAccount(id);
            return new ResponseEntity<>("Delete successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping
    public ResponseEntity<Account> updateAccount(@RequestParam(value = "id", required = true) Integer accountId,
                                                 @RequestBody AccountResponseDTO updatedAccount){
        Account accountUpdated = accountServiceImpl.updatedAccount(accountId, updatedAccount);
        if (accountUpdated != null) {
            return ResponseEntity.ok(accountUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
