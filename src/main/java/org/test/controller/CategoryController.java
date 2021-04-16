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
import org.test.entity.Category;
import org.test.service.BannerService;
import org.test.service.CategoryService;
import org.test.specification.CategorySpecification;

import javax.validation.Valid;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class CategoryController {
    CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService){this.categoryService = categoryService;}

    BannerService bannerService;

    @Autowired
    public void setBannerService(BannerService bannerService){this.bannerService = bannerService;}


    /**
     * Поиск категории по requestName
     * @param requestName часть или полное значение requestName
     * @param pageable страница по умолчанию
     * @return возвращает список найденых категорий
     */
    @GetMapping("/categories")
    public ResponseEntity<?> getAll(
            @RequestParam(value = "requestName", required = false) String requestName,
            @PageableDefault(page = 0, size = 15) Pageable pageable
    ){
        Category filter = new Category();
        filter.setRequestName(requestName);

        Page<Category> page = categoryService.getAllBySpecification(new CategorySpecification(filter), pageable);
        return ResponseEntity.ok(page.get().collect(Collectors.toList()));
    }

    /**
     * Добавление новой категории
     * @param category на вход берется объект категории
     * @return возвращается созданный объект
     */
    @PostMapping("/add_categories")
    public  ResponseEntity<?> createCategory(
            @RequestBody @Valid Category category){
        categoryService.save(category);

        return ResponseEntity.ok(category);
    }

    /**
     * Изменение категории
     * @param category на вход берется изменяемый объект с указанием ID, если id не указан,
     *                 создастся новый объект, объект помечается как актуальный
     * @return возвращает измененный объект
     */
    @PutMapping("/edit_categories")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid Category category){
        if(!category.isDeleted()){
            category.setDeleted(false);}
        categoryService.save(category);
        return ResponseEntity.ok(category);

    }

    /**
     * Объект помечается как удаленный, производится проверка на наличие неудаленных банеров
     * @param id - в пути прописывается id объекта
     * @return
     */
    @DeleteMapping("/categories/del/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        if (categoryService.deleteAllowed(id)){
            Category category = categoryService.getOne(id);
            categoryService.hide(category);
            categoryService.save(category);
            return ResponseEntity.ok(categoryService.getOne(id));
        }
        else return new ResponseEntity("Error! Some banners in the category have not been removed", HttpStatus.BAD_REQUEST);
    }
}
