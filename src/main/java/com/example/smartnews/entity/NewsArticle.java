package com.example.smartnews.entity;

import com.example.smartnews.enums.ArticalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "NewsArticle")
@Data
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ArticleId")
    private Integer articleId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Content", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;
    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private ArticalStatus status;

    @Column(name = "ViewCount")
    private Integer viewCount;
    @Column(name = "ImageUrl")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "AuthorId")
    private SystemAccount author;

    @ManyToMany
    @JoinTable(
            name = "ArticleTag",
            joinColumns = @JoinColumn(name = "ArticleId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private List<Tag> tags;
}
