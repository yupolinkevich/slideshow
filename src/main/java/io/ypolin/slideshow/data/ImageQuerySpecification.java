package io.ypolin.slideshow.data;

import io.ypolin.slideshow.data.entity.Image;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class ImageQuerySpecification {
    public static Specification<Image> urlContainsAny(List<String> keywords) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = keywords.stream()
                    .map(keyword -> criteriaBuilder.like(root.get("url"), "%" + keyword + "%"))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }

    public static Specification<Image> durationEquals(long duration){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("duration"), duration);
    }
}
