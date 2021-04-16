package org.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.test.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {

    Category findByRequestName(String requestName);
}
