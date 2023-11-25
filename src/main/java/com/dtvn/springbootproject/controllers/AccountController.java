package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.services.implementations.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
