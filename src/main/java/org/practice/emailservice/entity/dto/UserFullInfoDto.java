package org.practice.emailservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.practice.emailservice.entity.model.Letter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserFullInfoDto {private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Instant createdAt;
    private List<Letter> myLetters;
    private List<Letter> sendLetters;
}
