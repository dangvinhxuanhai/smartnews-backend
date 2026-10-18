package com.example.smartnews.entity;

import com.example.smartnews.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SystemAccount")
@Data
public class SystemAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Integer accountId;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private UserRole role;

    @Column(name = "Status")
    private Boolean status;

    @Column(name = "IsVerified")
    private Boolean isVerified;

    @Column(name = "VerificationToken")
    private String verificationToken;

    @Column(name = "Provider")
    private String provider;

    @Column(name = "ProviderId")
    private String providerId;
}
