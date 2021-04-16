package org.test.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.test.entity.Category;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CategorySpecification implements Specification<Category> {
    private final Category filter;

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
        List<Predicate> predicates = new ArrayList<>();
        if(filter.getRequestName()!= null){
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("requestName")), "%" + filter.getRequestName().toLowerCase() + "%"));
        }
        Predicate where = criteriaQuery
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])))
                .distinct(true)
                .getRestriction();

        return where;
    }
}
