package org.practice.emailservice.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.practice.emailservice.entity.dto.AuthResponseDto;
import org.practice.emailservice.entity.model.RefreshToken;
import org.practice.emailservice.entity.model.Users;
import org.practice.emailservice.entity.request.SignUpRequest;
import org.practice.emailservice.enums.TokenType;
import org.practice.emailservice.exception.NotFoundException;
import org.practice.emailservice.jwtConfig.JwtTokenGenerator;
import org.practice.emailservice.repo.RefreshTokenRepo;
import org.practice.emailservice.repo.UsersRepo;
import org.practice.emailservice.security.LogoutHandlerService;
import org.practice.emailservice.utils.mapper.MapStruct;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {
    private final UsersRepo usersRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDto registerUser(SignUpRequest signUpRequest, HttpServletResponse httpServletResponse) {

        try {

            usersRepo.findByEmail(signUpRequest.getEmail()).ifPresent(users -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
            });

            signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            Users userDetails = MapStruct.INSTANCE.requestToUser(signUpRequest);
            userDetails.setCreatedAt(Instant.now());

            if (signUpRequest.getEmail().equals("manager@manager.com")) userDetails.setRoles("ROLE_MANAGER");
            else if (signUpRequest.getEmail().equals("admin@admin.com")) userDetails.setRoles("ROLE_ADMIN");
            else userDetails.setRoles("ROLE_USER");

            Authentication authentication = createAuthenticationObject(userDetails);

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            Users savedUserDetails = usersRepo.save(userDetails);
            saveUserRefreshToken(userDetails, refreshToken);

            creatRefreshTokenCookie(httpServletResponse, refreshToken);

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();


        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            var users = usersRepo.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            creatRefreshTokenCookie(response, refreshToken);

            saveUserRefreshToken(users, refreshToken);
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(users.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();


        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    private void saveUserRefreshToken(Users users, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .user(users)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        Users users = refreshTokenEntity.getUser();

        Authentication authentication = createAuthenticationObject(users);

        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(users.getUsername())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(Users users) {
        String username = users.getEmail();
        String password = users.getPassword();
        String roles = users.getRoles();

        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public Users getUserById(String id) {
        return usersRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
}