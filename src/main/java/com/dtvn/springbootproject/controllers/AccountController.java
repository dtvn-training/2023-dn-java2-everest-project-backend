package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.services.implementations.AccountServiceImpl;
import com.dtvn.springbootproject.constants.AppContants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> registerAnAccount(
            @RequestBody AccountRegisterRequestDTO request
    ) {
        return ResponseEntity.ok(accountService.registerAnAccount(request));
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public Page<AccountDTO> getAccounts(@RequestParam(value = "emailOrName", required = false) String emailOrName,
                                               @RequestParam(value = "pageNo", defaultValue = AppContants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = AppContants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return accountService.getAccountByEmailOrName(emailOrName, pageable);
    }
    @PatchMapping()
    public ResponseEntity<ResponseMessage<AccountDTO>> deleteAccount(@RequestParam(value = "id", required = true) String AccountId) {

        try {
            Integer id = Integer.parseInt(AccountId);
            accountService.deleteAccount(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppContants.ACCOUNT_DELETE_SUCCESS, AppContants.ACCOUNT_SUCCESS_CODE));
        }  catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppContants.ACCOUNT_ID_INVALID, AppContants.ACCOUNT_BAD_REQUEST));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppContants.ACCOUNT_NOT_FOUND, AppContants.RESOURCE_NOT_FOUND_CODE));
        }
    }
    @PutMapping
        public ResponseEntity<ResponseMessage<AccountDTO>> updateAccount(@RequestParam(value = "id", required = true) Integer accountId,
                                                 @RequestBody AccountDTO updatedAccount){
        AccountDTO accountUpdated = accountService.updatedAccount(accountId, updatedAccount);
        if (accountUpdated != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppContants.ACCOUNT_UPDATE_SUCCESS, AppContants.ACCOUNT_SUCCESS_CODE,accountUpdated));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppContants.ACCOUNT_NOT_FOUND, AppContants.RESOURCE_NOT_FOUND_CODE));
        }
    }

}
