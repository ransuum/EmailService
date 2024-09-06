package org.practice.emailservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.practice.emailservice.entity.dto.BanUserDto;
import org.practice.emailservice.entity.request.BanUserRequest;
import org.practice.emailservice.service.BanUsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BanUserController {
    private final BanUsersService banUsersService;

    @PostMapping("/create-ban")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<BanUserDto> createBanUsers(@RequestBody @Valid BanUserRequest banUsers){
        return new ResponseEntity<>(banUsersService.createBanUsers(banUsers), HttpStatus.CREATED);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<?> deleteBanUserByEmail(@PathVariable String email){
        banUsersService.deleteBanUsers(email);
        return ResponseEntity.ok("Deleted".toUpperCase());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<?> deleteBanUserById(@PathVariable String id){
        banUsersService.deleteById(id);
        return ResponseEntity.ok("Deleted".toUpperCase());
    }

    @GetMapping("/get-my-bans")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<BanUserDto>> getMyBanUsers() {
        return ResponseEntity.ok(banUsersService.getBanUsersThatIBanned());
    }

}

