package org.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.test.entity.Banner;
import java.util.List;

public interface BannerService {
    Page<Banner> getAllBySpecification(Specification<Banner> specification, Pageable pageable);

    List<Banner> getAll();

    void hide(Banner object);

    void save(Banner object);

    Banner getOne(Long id);

    Banner getByContent(String content);

}
