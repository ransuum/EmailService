package org.practice.emailservice.entity.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LetterDto {
    private String id;
    private String topic;
    private String text;
    private UsersDto userBy;
    private UsersDto userTo;
    private Instant createdAt;
    private List<FileInLetterDto> files;
}
