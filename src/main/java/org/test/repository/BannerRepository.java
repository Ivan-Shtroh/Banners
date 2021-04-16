package org.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.test.entity.Banner;
import org.test.entity.Category;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long>,
        JpaSpecificationExecutor<Banner> {
    List<Banner> findAllByCategoryId(@NotNull Category category);
    Banner findByContent(String content);
}
