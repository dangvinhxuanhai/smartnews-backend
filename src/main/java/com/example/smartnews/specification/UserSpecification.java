package com.example.smartnews.specification;

import com.example.smartnews.entity.SystemAccount;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<SystemAccount> hasKeyword(String keyword){
        return (root, query, cb)->{
            if(keyword == null || keyword.isBlank()){
                return null;
            }
            String pattern = "%"+keyword.trim().toLowerCase()+"%";
            return cb.or(
                    cb.like(root.get("email"),pattern),
                    cb.like(root.get("name"),pattern)
            );
        };
    }
    public static Specification<SystemAccount> hasRole(
            String role
    ) {
        return (root, query, cb) -> {

            if (role == null || role.isBlank()) {
                return null;
            }
            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<SystemAccount> hasStatus(
            Boolean status
    ) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
}
