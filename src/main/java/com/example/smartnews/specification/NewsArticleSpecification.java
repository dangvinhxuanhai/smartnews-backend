package com.example.smartnews.specification;

import com.example.smartnews.entity.NewsArticle;
import com.example.smartnews.enums.ArticleStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class NewsArticleSpecification {
    public static Specification<NewsArticle> hasKeyword(String keyword){
        return ((root, query, cb) -> {
            if(keyword == null || keyword.isBlank()){
                return null;
            }
            return cb.like(cb.lower(root.get("title")),"%"+keyword.toLowerCase()+"%");
        });
    }
    public static Specification<NewsArticle> hasCategory(Integer categoryId){
        return ((root, query, cb) -> {
            if(categoryId == null){
                return null;
            }
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        });
    }
    public static Specification<NewsArticle> hasAuthor(Integer authorId){
        return ((root, query, cb) -> {
            if(authorId == null){
                return null;
            }
            return cb.equal(root.get("author").get("accountId"), authorId);
        });
    }

    public static Specification<NewsArticle> hasStatus(ArticleStatus status){
        return ((root, query, cb) -> {
            if(status == null){
                return null;
            }
            return cb.equal(root.get("status"), status);
        });
    }
    public static Specification<NewsArticle> hasTag(Integer tagId) {

        return (root, query, cb) -> {
            if (tagId == null) {
                return null;
            }
            query.distinct(true);
            return cb.equal(root.join("tags").get("tagId"), tagId);
        };
    }
    public static Specification<NewsArticle> createdAfter(
            LocalDateTime fromDate) {

        return (root, query, cb) -> {

            if (fromDate == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                    root.get("createdDate"),
                    fromDate
            );
        };
    }
    public static Specification<NewsArticle> createdBefore(
            LocalDateTime toDate) {

        return (root, query, cb) -> {

            if (toDate == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(
                    root.get("createdDate"),
                    toDate
            );
        };
    }
}
