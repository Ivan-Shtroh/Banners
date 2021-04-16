package org.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.test.entity.Banner;
import org.test.repository.BannerRepository;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    BannerRepository bannerRepository;

    @Autowired
    public void setBannerRepository(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @Override
    public Page<Banner> getAllBySpecification(Specification<Banner> specification, Pageable pageable) {
        return bannerRepository.findAll(specification, pageable);
    }

    @Override
    public List<Banner> getAll() {
        return bannerRepository.findAll();
    }

    @Override
    public void hide(Banner banner) {
        banner.setDeleted(true);
    }

    @Override
    public void save(Banner object) {
        bannerRepository.save(object);
    }

    @Override
    public Banner getOne(Long id) {
        return bannerRepository.findById(id).get();
    }

    @Override
    public Banner getByContent(String content) {
        return bannerRepository.findByContent(content);
    }


}
