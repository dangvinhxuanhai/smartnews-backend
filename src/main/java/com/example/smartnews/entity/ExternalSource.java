package com.example.smartnews.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ExternalSource")
@Getter
@Setter
public class ExternalSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SourceId")
    private Integer sourceId;

    @Column(
            name = "Code",
            nullable = false,
            unique = true,
            length = 50
    )
    private String code;

    @Column(
            name = "Name",
            nullable = false,
            unique = true,
            length = 150
    )
    private String name;

    @Column(name = "WebsiteUrl", length = 500)
    private String websiteUrl;

    @Column(name = "RssUrl", length = 500)
    private String rssUrl;

    @Column(name = "LogoUrl", length = 500)
    private String logoUrl;

    @Column(name = "CountryCode", length = 10)
    private String countryCode;

    @Column(name = "LanguageCode", length = 10)
    private String languageCode;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {

        if (isActive == null) {
            isActive = true;
        }

        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }
}