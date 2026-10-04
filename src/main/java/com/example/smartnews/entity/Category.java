package com.example.smartnews.entity;

import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "Category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    private Integer categoryId;

    @Column(name = "CategoryName")
    private String categoryName;

    @Column(name = "IsActive")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "ParentId")
    private  Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Category> children;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<NewsArticle> articles;
}
