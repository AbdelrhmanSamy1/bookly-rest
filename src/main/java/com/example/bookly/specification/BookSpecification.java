package com.example.bookly.specification;

import com.example.bookly.dto.BookFilterDto;
import com.example.bookly.entity.Book;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    private static List<Predicate> predicates;

    private   BookSpecification() {}

    public static Specification<Book> withFilters(BookFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + filter.getTitle().toLowerCase() + "%"
                        )
                );
            }
            if (filter.getCategoryName() != null && !filter.getCategoryName().isBlank()) {
                Join<Object, Object> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(
                        criteriaBuilder.like(
                                        criteriaBuilder.lower(categoryJoin.get("name")),
                                        "%" + filter.getCategoryName().toLowerCase() + "%"
                        )
                );
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(
                                criteriaBuilder.lessThanOrEqualTo(
                                        root.get("price"), filter.getMaxPrice())
                        );
            }
            if (filter.getMinPrice() != null) {
                predicates.add(
                                criteriaBuilder.greaterThanOrEqualTo(
                                        root.get("price"), filter.getMinPrice())
                        );
            }
            if (filter.getPublishedYear() != null) {
                predicates.add(criteriaBuilder.equal(root.get("publishedYear"), filter.getPublishedYear()));
            }
            if (filter.getInStock() != null && filter.getInStock()) {
                predicates.add(
                        criteriaBuilder.greaterThan(root.get("stockQuantity"), 0)
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
