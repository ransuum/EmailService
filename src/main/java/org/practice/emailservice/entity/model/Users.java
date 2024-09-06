package org.practice.emailservice.entity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@org.springframework.data.relational.core.mapping.Table(name = "users")
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String roles;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "userBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Letter> sendLetters = new ArrayList<>();

    @OneToMany(mappedBy = "userTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Letter> myLetters = new ArrayList<>();

    @OneToMany(mappedBy = "banUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BanUsers> banUsers = new ArrayList<>();

    @OneToMany(mappedBy = "personalAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BanUsers> banYou = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    private String address;

}
