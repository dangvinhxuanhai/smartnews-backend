package com.example.smartnews.entity;

import com.example.smartnews.enums.ExternalArticleReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ExternalArticle")
@Getter
@Setter
public class ExternalArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ExternalArticleId")
    private Integer externalArticleId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "SourceId",
            nullable = false
    )
    private ExternalSource source;

    @Column(
            name = "SourceArticleKey",
            length = 150
    )
    private String sourceArticleKey;

    @Column(
            name = "OriginalCategory",
            length = 100
    )
    private String originalCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId")
    private Category category;

    @Column(
            name = "Title",
            nullable = false,
            length = 500
    )
    private String title;

    @Column(
            name = "Summary",
            columnDefinition = "NVARCHAR(MAX)"
    )
    private String summary;

    @Column(
            name = "Content",
            columnDefinition = "NVARCHAR(MAX)"
    )
    private String content;

    @Column(
            name = "Url",
            nullable = false,
            unique = true,
            length = 700
    )
    private String url;

    @Column(name = "ImageUrl", length = 700)
    private String imageUrl;

    @Column(name = "Author", length = 255)
    private String author;

    @Column(name = "PublishedAt")
    private LocalDateTime publishedAt;

    @Column(
            name = "ImportedAt",
            nullable = false
    )
    private LocalDateTime importedAt;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "ReviewStatus",
            nullable = false,
            length = 20
    )
    private ExternalArticleReviewStatus reviewStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewedBy")
    private SystemAccount reviewedBy;


    @Column(name = "ReviewedAt")
    private LocalDateTime reviewedAt;


    @Column(name = "EditorNote", length = 1000)
    private String editorNote;

    @PrePersist
    public void prePersist() {

        if (importedAt == null) {
            importedAt = LocalDateTime.now();
        }
        if (reviewStatus == null) {
            reviewStatus = ExternalArticleReviewStatus.Pending;
        }
    }
}
