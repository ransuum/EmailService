package org.practice.emailservice.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.practice.emailservice.validator.ValidEmailTag;

@AllArgsConstructor
@Data
@Builder
public class SignUpRequest {
    @Valid
    @Email(message = "Isn't email")
    @NotBlank(message = "Email is blank")
    @Size(max = 40, message = "Email is too long")
    @ValidEmailTag(tag = "seeyaa.com", message = "Email must end with @seeyaa.com")
    private String email;

    @NotBlank(message = "Username is blank")
    @Size(max = 14, message = "Username is too long")
    private String username;

    @Valid
    @NotBlank(message = "Password is blank")
    @Size(min = 8, max = 30, message = "Password size should be from 9 to 30 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*#?&_-]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;

    @NotBlank(message = "firstname is blank")
    @Pattern(regexp = "^[\\p{L}\\p{M} ,.'-]+$", message = "Incorrect first name")
    private String firstname;

    @NotBlank(message = "lastname is blank")
    @Pattern(regexp = "^[\\p{L}\\p{M} ,.'-]+$", message = "Incorrect last name")
    private String lastname;

    @NotBlank(message = "address is blank")
    @JsonProperty("address")
    private String address;

    @Valid
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    @JsonProperty("phone")
    private String phone;

}
