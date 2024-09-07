package org.practice.emailservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.practice.emailservice.enums.BanType;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BanUserDto {
    private String id;
    private UsersDto banUser;
    private String reason;
    private BanType banType;
    private Instant banDate;
}
