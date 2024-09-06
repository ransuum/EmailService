package org.practice.emailservice.entity.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "ban_users")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BanUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "account_who_banned_you_id",referencedColumnName = "id")
    @JsonIgnore
    private Users personalAccount;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "banUser_id",referencedColumnName = "id")
    private Users banUser;

    @Column(nullable = false)
    private Instant banDate;
}
