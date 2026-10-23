package com.example.smartnews.specification;

import com.example.smartnews.entity.ExternalArticle;
import com.example.smartnews.enums.ExternalArticleReviewStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ExternalArticleSpecification {
    public static Specification<ExternalArticle> hasKeyword(
            String keyword
    ) {

        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("content")), pattern)
            );
        };
    }


    public static Specification<ExternalArticle> hasSource(
            Integer sourceId
    ) {
        return (root, query, cb) -> {
            if (sourceId == null) {
                return null;
            }
            return cb.equal(root.get("source").get("sourceId"), sourceId);
        };
    }


    public static Specification<ExternalArticle> hasCategory(
            Integer categoryId
    ) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return null;
            }
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        };
    }


    public static Specification<ExternalArticle> hasOriginalCategory(
            String originalCategory
    ) {
        return (root, query, cb) -> {
            if (originalCategory == null || originalCategory.isBlank()) {
                return null;
            }
            return cb.equal(
                    cb.lower(root.get("originalCategory")),
                    originalCategory
                            .trim()
                            .toLowerCase()
            );
        };
    }

    public static Specification<ExternalArticle> hasReviewStatus(ExternalArticleReviewStatus status) {

        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("reviewStatus"), status);
        };
    }


    public static Specification<ExternalArticle> publishedAfter(
            LocalDateTime date
    ) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThanOrEqualTo(root.get("publishedAt"), date);
    }


    public static Specification<ExternalArticle> publishedBefore(
            LocalDateTime date
    ) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("publishedAt"), date);
    }
}
