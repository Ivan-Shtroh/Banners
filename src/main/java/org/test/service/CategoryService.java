package org.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.test.entity.Category;

import java.util.List;

public interface CategoryService {
    void hide(Category object);

    List<Category> getAll();

    void save(Category object);

    Page<Category> getAllBySpecification(Specification<Category> specification, Pageable pageable);

    Category getOne(Long id);


    boolean deleteAllowed(Long id);
}
