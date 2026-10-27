package com.example.smartnews.specification;

import com.example.smartnews.entity.Story;
import com.example.smartnews.enums.StoryStatus;
import org.springframework.data.jpa.domain.Specification;

public class StorySpecification {

    public static Specification<Story> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("summary")), pattern)
            );
        };
    }


    public static Specification<Story> hasStatus(StoryStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }


    public static Specification<Story> hasCategory(Integer categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return null;
            }
            return cb.equal(root.get("category").get("categoryId"), categoryId);
        };
    }
}
