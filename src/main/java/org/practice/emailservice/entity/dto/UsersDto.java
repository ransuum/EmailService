package org.practice.emailservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsersDto {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Instant createdAt;
}
