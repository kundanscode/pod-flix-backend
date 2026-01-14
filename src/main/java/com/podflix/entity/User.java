package com.podflix.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = false;

    @Column(name = "token_version")
    @Builder.Default
    private int tokenVersion = 0;

    @Column(name = "create_datetime")
    private java.time.LocalDateTime createDatetime;

    @Column(name = "last_update_datetime")
    private java.time.LocalDateTime lastUpdateDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = java.time.LocalDateTime.now();
        lastUpdateDatetime = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdateDatetime = java.time.LocalDateTime.now();
    }
}
