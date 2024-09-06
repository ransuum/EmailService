package org.practice.emailservice.entity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "letters")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Letter implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "toUser_id",referencedColumnName = "id")
    private Users userTo;

    @ManyToOne
    @JoinColumn(name = "byUser_id",referencedColumnName = "id")
    private Users userBy;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "letter", orphanRemoval = true)
    private List<FileInLetter> files = new ArrayList<>();

}
