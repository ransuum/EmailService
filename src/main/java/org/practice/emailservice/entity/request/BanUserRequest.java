package org.practice.emailservice.entity.request;

import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.practice.emailservice.enums.BanType;

@AllArgsConstructor
@Data
@Builder
public class BanUserRequest {
    @Valid
    @Email(message = "Isn't email")
    @NotBlank(message = "Email is blank")
    @Size(max = 40, message = "Email is too long")
    private String email;

    @NotBlank(message = "banType is blank")
    @Size(max = 19, message = "BanType is too long")
    private BanType banType;

    @NotBlank(message = "Reason is blank")
    @Size(max = 24, message = "Reason is too long")
    private String reason;
}
