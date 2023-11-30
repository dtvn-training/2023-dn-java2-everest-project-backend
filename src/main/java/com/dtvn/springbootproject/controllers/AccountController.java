package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.dtvn.springbootproject.constants.AppConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ResponseMessage<AccountResponseDTO>> registerAnAccount(
            @RequestBody AccountRegisterRequestDTO request
    ) {
        AccountResponseDTO addedAccount = accountService.registerAnAccount(request);
        if (addedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(ACCOUNT_REGISTER_SUCCESS, HTTP_OK, addedAccount));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(ACCOUNT_REGISTER_FAILED, HTTP_INTERNAL_SERVER_ERROR));
        }
    }


}
