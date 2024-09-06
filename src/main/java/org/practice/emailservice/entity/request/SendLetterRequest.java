package org.practice.emailservice.entity.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendLetterRequest {
    @Valid
    @Email(message = "Isn't email")
    @NotBlank(message = "Email is blank")
    @Size(max = 40, message = "Email is too long")
    private String emailTo;

    @Size(max = 25, message = "Topic is too long")
    private String topic;

    @Size(max = 100, message = "Text is too long")
    private String text;
}
