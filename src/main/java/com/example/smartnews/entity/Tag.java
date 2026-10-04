package com.example.smartnews.entity;

import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "Tag")
@Data
public class Tag {
    @Id
    @Column(name = "TagId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(name = "TagName")
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<NewsArticle> articles;
}
