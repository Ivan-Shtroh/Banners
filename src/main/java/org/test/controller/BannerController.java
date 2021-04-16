package org.test.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.entity.Banner;
import org.test.entity.Category;
import org.test.service.BannerService;
import org.test.specification.BannerSpecification;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class BannerController {

    BannerService bannerService;

    @Autowired
    public void setBannerService(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * Метоб выдает список баннеров по названию баннера или части названия без учета регистра
     *
     * @param name     - шаблон для поиска
     * @param pageable - размер страницы по-умолчанию
     * @return Возвращает страницу баннеров удовлетворяющих условиям фильтра
     */
    @GetMapping("/banners")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault(page = 0, size = 15) Pageable pageable
    ) {
        Banner filter = new Banner();
        filter.setName(name);

        Page<Banner> page = bannerService.getAllBySpecification(new BannerSpecification(filter), pageable);
        return ResponseEntity.ok(page.get().collect(Collectors.toList()));
    }

    /**
     * Добавление нового баннера с проверкой категории     *
     * @param banner принимает на вход объект для создания
     * @return возвращает записанный объект либо сообщение что категория не прошла проверку
     */
    @PostMapping("/banners")
    public ResponseEntity<?> createBanner(
            @RequestBody @Valid Banner banner) {
        Category category = banner.getCategoryId();
        if(category == null){
            return new ResponseEntity("Category does not exist", HttpStatus.BAD_REQUEST);
        }
        if (!category.isDeleted()) {
            bannerService.save(banner);
            return ResponseEntity.ok(banner);
        } else return new ResponseEntity("Category is deleted", HttpStatus.BAD_REQUEST);
    }

    /**
     * Изменение банера, при изменении баннер помечается как актуальный,
     * так же категория проверяется на видимость
     * @param banner на вход поступает объект Баннер
     * @return возвращается объект баннер либо сообщение, что категория удалена
     */
    @PutMapping("/banners")
    public ResponseEntity<?> updateBanner(@RequestBody @Valid Banner banner){
        Category category = banner.getCategoryId();
        if (!category.isDeleted()) {
            banner.setDeleted(false);
            bannerService.save(banner);
            return ResponseEntity.ok(banner);
        } else return new ResponseEntity("Category is deleted", HttpStatus.BAD_REQUEST);


    }

    /**
     * Баннер помечается в БД как удаленный
     * @param id на вход берется id баннера
     * @return
     */
    @DeleteMapping("/banners/del/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable("id") Long id) {
        Banner banner = bannerService.getOne(id);
        bannerService.hide(banner);
        bannerService.save(banner);

        return ResponseEntity.ok(bannerService.getOne(id));
    }


}
