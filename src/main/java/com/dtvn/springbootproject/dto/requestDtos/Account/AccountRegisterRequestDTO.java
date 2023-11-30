package com.dtvn.springbootproject.dto.requestDtos.Account;

import com.dtvn.springbootproject.exceptions.ErrorException;
import lombok.*;
import org.springframework.web.ErrorResponse;

import javax.validation.constraints.*;

import static com.dtvn.springbootproject.utils.RegularExpression.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequestDTO {
    @NotBlank(message = "First name is required")
    @Pattern(regexp = NAME_REGEX, message = "Invalid first name")
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = NAME_REGEX, message = "Invalid last name")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = EMAIL_REGEX, message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    @Pattern.List({
            @Pattern(regexp = ".*\\d.*", message = "Password should contain at least one digit."),
            @Pattern(regexp = ".*[a-z].*", message = "Password should contain at least one lowercase letter."),
            @Pattern(regexp = ".*[A-Z].*", message = "Password should contain at least one uppercase letter."),
            @Pattern(regexp = ".*[@#$%^&+=!].*", message = "Password should contain at least one special character (@#$%^&+=!)."),
            @Pattern(regexp = "\\S+", message = "Password should not contain whitespace characters.")
    })
    private String password;


    @NotBlank(message = "Role is required.")
    private String role;

    @NotBlank(message = "Address is required.")
    @Pattern(regexp = ADDRESS_REGEX, message = "Invalid Address.")
    private String address;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = "Invalid Phone number.")
    private String phone;
}
