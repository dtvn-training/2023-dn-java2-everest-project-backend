package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.services.impl.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ResponseMessage<AccountResponseDTO>> registerAnAccount(
            @RequestBody @Valid AccountRegisterRequestDTO request, BindingResult bindingResult
    ) {
        AccountResponseDTO addedAccount = accountService.registerAnAccount(request);
        if(addedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    //TODO: Change message to constant variable
                    .body(new ResponseMessage("Add an account successfully", 200, addedAccount));
        }
     else {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage("Add an account failed", 500));
    }
    }


}
