package org.practice.emailservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.practice.emailservice.entity.dto.*;
import org.practice.emailservice.entity.request.SendLetterRequest;
import org.practice.emailservice.service.LetterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/send-letter")
    @Operation(summary = "Send letter to user", description = "Write the email you want to send to")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<LetterDto> sendLetter(@RequestBody @Valid SendLetterRequest sendLetterRequest,
                                                @RequestParam(value = "files", required = false) MultipartFile[] files) {
        try {
            return new ResponseEntity<>(letterService.sendLetter(sendLetterRequest, files), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-letter-byEmail/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<LetterDto>> getLetterByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(letterService.getLetter(email));
    }

    @GetMapping("/get-letter-byId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<LetterDto> getLetterById(@PathVariable("id") String id) {
        return ResponseEntity.ok(letterService.getLetterById(id));
    }

    @DeleteMapping("/from-me/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Delete letter from me", description = "If you have sent a message, you can delete it only for yourself")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<?> deleteLetterFromMe(@PathVariable String id){
        letterService.deleteLetterFromMe(id);
        return ResponseEntity.ok("Deleted letter from me");
    }

    @DeleteMapping("/from-all/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Delete letter from all", description = "If you have sent a message, you can delete it for all")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<?> deleteLetterFromMAll(@PathVariable String id){
        letterService.deleteLetterFromAll(id);
        return ResponseEntity.ok("Deleted letter from all");
    }

    @GetMapping("/get-by-start-ofTopic/{topic}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Get letter", description = "There you can get a letter containing the value of your string")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<LetterDto>> getLetterByTopicStartOf(@PathVariable("topic") String topic) {
        return ResponseEntity.ok(letterService.getByStartOfTheTopic(topic));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<LetterDto>> getLetters() {
        return ResponseEntity.ok(letterService.getAllTopic());
    }

    @GetMapping("/check")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<UserFullInfoDto> checkMyProfile() {
        return ResponseEntity.ok(letterService.checkMyProfile());
    }

    @GetMapping("/my-letters")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<MyLetters>> getMyLetters() {
        return ResponseEntity.ok(letterService.myLetters());
    }

    @GetMapping("/send-letters")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<SendLetters>> getSendLetters() {
        return ResponseEntity.ok(letterService.sendLetters());
    }
}
