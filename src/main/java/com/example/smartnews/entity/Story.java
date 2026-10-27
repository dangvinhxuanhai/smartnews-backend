package com.example.smartnews.entity;

import com.example.smartnews.enums.StoryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Story")
@Getter
@Setter
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StoryId")
    private Integer storyId;

    @Column(
            name = "Title",
            nullable = false,
            length = 255
    )
    private String title;

    @Column(
            name = "Summary",
            columnDefinition = "NVARCHAR(MAX)"
    )
    private String summary;

    @Column(
            name = "HeroImageUrl",
            length = 700
    )
    private String heroImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "Status",
            nullable = false,
            length = 20
    )
    private StoryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy")
    private SystemAccount createdBy;

    @Column(
            name = "ViewCount",
            nullable = false
    )
    private Integer viewCount;

    @Column(
            name = "CreatedDate",
            nullable = false
    )
    private LocalDateTime createdDate;

    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @Column(name = "PublishedDate")
    private LocalDateTime publishedDate;


    @ManyToMany
    @JoinTable(
            name = "StoryExternalArticle",
            joinColumns = @JoinColumn(name = "StoryId"),
            inverseJoinColumns = @JoinColumn(name = "ExternalArticleId")
    )
    private Set<ExternalArticle> externalArticles = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "StoryNewsArticle",
            joinColumns = @JoinColumn(name = "StoryId"),
            inverseJoinColumns = @JoinColumn(name = "ArticleId")
    )
    private Set<NewsArticle> newsArticles = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "StoryTag",
            joinColumns = @JoinColumn(name = "StoryId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private Set<Tag> tags =
            new HashSet<>();


    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = StoryStatus.Draft;
        }
        if (viewCount == null) {
            viewCount = 0;
        }
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
