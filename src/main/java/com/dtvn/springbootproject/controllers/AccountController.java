package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.responses.AccountResponse;
import com.dtvn.springbootproject.requests.RegisterAnAccountRequest;
import com.dtvn.springbootproject.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

//    @GetMapping
//    public List<AccountResponse> getAccounts(@RequestParam(defaultValue = "0") int page,
//                                             @RequestParam(defaultValue = "3") int size) {
//        return accountService.getAccounts(page, size);
//    }
    @PostMapping
    public ResponseEntity<AccountResponse> registerAnAccount(
            @RequestBody RegisterAnAccountRequest request
    ) {
        return ResponseEntity.ok(accountService.registerAnAccount(request));
    }


}
