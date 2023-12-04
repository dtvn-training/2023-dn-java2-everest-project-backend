package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.config.JwtService;
import com.dtvn.springbootproject.constants.AuthConstants;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Role;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.repositories.RoleRepository;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import static com.dtvn.springbootproject.constants.AppConstants.*;
import static com.dtvn.springbootproject.constants.ErrorConstants.ERROR_EMAIL_ALREADY_EXISTS;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<ResponseMessage<AccountResponseDTO>> registerAnAccount(
            @RequestBody AccountRegisterRequestDTO request
    ) {
        if(accountRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(ERROR_EMAIL_ALREADY_EXISTS, HTTP_BAD_REQUEST));
        }
        AccountResponseDTO addedAccount = accountService.registerAnAccount(request);
        if (addedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(ACCOUNT_REGISTER_SUCCESS, HTTP_OK, addedAccount));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(ACCOUNT_REGISTER_FAILED, HTTP_INTERNAL_SERVER_ERROR));
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<ResponseMessage<Page<AccountDTO> >> getAccounts(@RequestParam(value = "emailOrName", required = false) String emailOrName,
                                               @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) String strPageNo,
                                               @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) String strPageSize) {

        if(!accountService.isInteger(strPageNo))
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.PAGENO_INVALID, AppConstants.ACCOUNT_BAD_REQUEST));
        else if(!accountService.isInteger(strPageSize)){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.PAGESIZE_INVALID, AppConstants.ACCOUNT_BAD_REQUEST));
        }
        int pageNo = Integer.parseInt(strPageNo);
        int pageSize = Integer.parseInt(strPageSize);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage<Page<AccountDTO>>(AppConstants.ACCOUNT_GET_ALL_SUCCESS, AppConstants.ACCOUNT_SUCCESS_CODE,
                        accountService.getAccountByEmailOrName(emailOrName, pageable)));
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping()
    public ResponseEntity<ResponseMessage<AccountDTO>> deleteAccount(
            @RequestParam(value = "id", required = true) String AccountId,
            @RequestHeader("Authorization") String bearerToken)  {

        //Delete "bearer" in token
        bearerToken = bearerToken.replace(AuthConstants.BEARER_PREFIX, "");
        final String currentUserEmail = jwtService.extractUsername(bearerToken);

        try {
            Integer id = Integer.parseInt(AccountId);
            Optional<Account> accountDelete = accountRepository.findById(id);
            //Check if account has been deleted
            if(accountDelete.get().isDeleteFlag())
                return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ACCOUNT_IS_DELETED , AppConstants.ACCOUNT_SUCCESS_CODE));

//            If the account is deleted, it is the current account
            if(accountDelete.get().getEmail().equals(currentUserEmail)){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(ACCOUNT_DELETE_FAILD, AppConstants.ACCOUNT_SUCCESS_CODE));
            }

            //delete account
            accountService.deleteAccount(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_DELETE_SUCCESS, AppConstants.ACCOUNT_SUCCESS_CODE));


        }  catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_ID_INVALID, AppConstants.ACCOUNT_BAD_REQUEST));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_NOT_FOUND, AppConstants.RESOURCE_NOT_FOUND_CODE));
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping
        public ResponseEntity<ResponseMessage<AccountDTO>> updateAccount(@RequestParam(value = "id", required = true) Integer accountId,
                                                 @RequestBody AccountDTO updatedAccount){
        if(accountRepository.existsByEmail(updatedAccount.getEmail())){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(ERROR_EMAIL_ALREADY_EXISTS, HTTP_BAD_REQUEST));
        }
        AccountDTO accountUpdated = accountService.updatedAccount(accountId, updatedAccount);
        if (accountUpdated != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppConstants.ACCOUNT_UPDATE_SUCCESS, AppConstants.ACCOUNT_SUCCESS_CODE,accountUpdated));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppConstants.ACCOUNT_NOT_FOUND, AppConstants.RESOURCE_NOT_FOUND_CODE));
        }
    }
    @GetMapping("/roles")
    public ResponseEntity<ResponseMessage<List<Role>>> getAllRole() {
        List<Role> listRole;
        try {
            listRole = roleRepository.findAll();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppConstants.ROLES_GET_ALL_FAILED, RESOURCE_NOT_FOUND_CODE));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessage(AppConstants.ROLES_GET_ALL_SUCCESS, ACCOUNT_SUCCESS_CODE, listRole));
    }


}
