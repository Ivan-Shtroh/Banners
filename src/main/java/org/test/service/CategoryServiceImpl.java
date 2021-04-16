package org.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.test.entity.Banner;
import org.test.entity.Category;
import org.test.repository.BannerRepository;
import org.test.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository){this.categoryRepository = categoryRepository;}

    BannerRepository bannerRepository;
    @Autowired
    public void setBannerRepository(BannerRepository bannerRepository){
        this.bannerRepository = bannerRepository;
    }

    @Override
    public void hide(Category object) {
        object.setDeleted(true);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category object) {
        categoryRepository.save(object);
    }

    @Override
    public Page<Category> getAllBySpecification(Specification<Category> specification, Pageable pageable) {
        return categoryRepository.findAll(specification, pageable);
    }

    @Override
    public Category getOne(Long id) {
        return categoryRepository.findById(id).get();
    }

    /**
     * Проверяется наличие у категории неудаленных банеров
     * @param id категория определяется по id
     * @return разрешено удаление или нет
     */
    @Override
    public boolean deleteAllowed(Long id) {
        boolean deleteAllowed = true;
        List<Banner> banners = bannerRepository.findAllByCategoryId(categoryRepository.getOne(id));
        for (Banner banner: banners) {
            if (!banner.isDeleted()) {
                deleteAllowed = false;
                break;
            }
        }

        return deleteAllowed;
    }
}
