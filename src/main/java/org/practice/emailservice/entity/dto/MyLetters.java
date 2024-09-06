package org.practice.emailservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MyLetters {
    private String id;
    private String topic;
    private String text;
    private UsersDto userBy;
    private Instant createdAt;
    private List<FileInLetterDto> files;
}
