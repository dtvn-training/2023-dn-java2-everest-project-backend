package com.dtvn.springbootproject.services.interfaces;

import com.dtvn.springbootproject.requests.RegisterAnAccountRequest;
import com.dtvn.springbootproject.responses.AccountResponse;

public interface AccountService {
    AccountResponse registerAnAccount(RegisterAnAccountRequest request);
}
