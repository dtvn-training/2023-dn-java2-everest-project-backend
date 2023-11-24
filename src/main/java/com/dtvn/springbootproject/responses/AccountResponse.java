package com.dtvn.springbootproject.responses;

import com.dtvn.springbootproject.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private int account_id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String address;
    private String phone;
}
