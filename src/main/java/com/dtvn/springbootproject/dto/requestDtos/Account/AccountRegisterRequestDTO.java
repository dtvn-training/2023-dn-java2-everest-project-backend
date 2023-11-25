package com.dtvn.springbootproject.dto.requestDtos.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequestDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
    private String address;
    private String phone;
}
